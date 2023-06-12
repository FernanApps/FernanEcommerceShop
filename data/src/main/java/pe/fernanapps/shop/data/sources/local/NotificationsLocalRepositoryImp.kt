package pe.fernanapps.shop.data.sources.local

import kotlinx.coroutines.flow.Flow
import pe.fernanapps.shop.domain.model.notifications.Notification
import pe.fernanapps.shop.domain.repository.NotificationsLocalRepository
import javax.inject.Inject

class NotificationsLocalRepositoryImp @Inject constructor(
    private val local: LocalDataSource
): NotificationsLocalRepository {
    override suspend fun save(notification: Notification) {
        local.save(notification)
    }

    override suspend fun saveAll(list: List<Notification>) {
        local.saveAll(list)
    }

    override suspend fun getAll(): Flow<List<Notification>> = local.getAllNotifications()


}