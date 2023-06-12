package pe.fernanapps.shop.domain.repository

import kotlinx.coroutines.flow.Flow
import pe.fernanapps.shop.domain.DataState
import pe.fernanapps.shop.domain.model.notifications.Notification

interface NotificationsServiceRepository {
    fun subscribe(): Flow<Notification>
    fun unsubscribe()
}


interface NotificationsRemoteRepository {
    fun getAll(): Flow<DataState<List<Notification>>>
}

interface NotificationsLocalRepository {
    suspend fun save(notification: Notification)
    suspend fun saveAll(list: List<Notification>)
    suspend fun getAll(): Flow<List<Notification>>
}