package pe.fernanapps.shop.domain.usecases.notifcations

import kotlinx.coroutines.flow.Flow
import pe.fernanapps.shop.domain.DataState
import pe.fernanapps.shop.domain.model.notifications.Notification
import pe.fernanapps.shop.domain.repository.NotificationsLocalRepository
import pe.fernanapps.shop.domain.repository.NotificationsRemoteRepository
import javax.inject.Inject

/* TODO LOCAL  */
class SaveNotificationLocalUseCase @Inject constructor(private val notificationsLocalRepository: NotificationsLocalRepository) {

    suspend operator fun invoke(notification: Notification) {
        notificationsLocalRepository.save(notification)
    }
}

class SaveAllNotificationsLocalUseCase @Inject constructor(private val notificationsLocalRepository: NotificationsLocalRepository) {

    suspend operator fun invoke(notifications: List<Notification>) {
        notificationsLocalRepository.saveAll(notifications)
    }
}

class GetAllNotificationsLocalUseCase @Inject constructor(private val notificationsLocalRepository: NotificationsLocalRepository) {

    suspend operator fun invoke(): Flow<List<Notification>> {
        return notificationsLocalRepository.getAll()
    }
}

/* TODO REMOTE  */

class GetNotificationsRemoteUseCase @Inject constructor(private val notificationsRemoteRepository: NotificationsRemoteRepository) {
    operator fun invoke(): Flow<DataState<List<Notification>>> {
        return notificationsRemoteRepository.getAll()
    }
}