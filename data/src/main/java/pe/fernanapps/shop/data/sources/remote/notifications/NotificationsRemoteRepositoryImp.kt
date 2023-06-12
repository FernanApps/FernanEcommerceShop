package pe.fernanapps.shop.data.sources.remote.notifications

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import pe.fernanapps.shop.data.sources.local.user.UserPreference
import pe.fernanapps.shop.domain.DataState
import pe.fernanapps.shop.domain.model.notifications.Notification
import pe.fernanapps.shop.domain.repository.NotificationsRemoteRepository
import javax.inject.Inject


class NotificationsRemoteRepositoryImp @Inject constructor(
    private val userPreference: UserPreference,
    private val notificationsService: NotificationsService,
) : NotificationsRemoteRepository {
    override fun getAll() = flow<DataState<List<Notification>>> {
        emit(DataState.Loading)
        try {
            println("NOTIF_SERVICE ::: getAll")
            val user = userPreference.getUserFromPreferences()!!
            val list = notificationsService.getNotifications(user.id)
            println("NOTIF_SERVICE ::: getAll222")
            emit(DataState.Success(list.map {
                it.toDomain()
            }))

        } catch (e: Exception) {
            e.printStackTrace()
            emit(DataState.Error(e))
        }

        delay(1500)
        emit(DataState.Finished)

    }.catch {
        it.printStackTrace()
        emit(DataState.Error(it))
    }

}