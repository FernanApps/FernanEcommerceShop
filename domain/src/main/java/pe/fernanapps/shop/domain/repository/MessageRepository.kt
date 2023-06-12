package pe.fernanapps.shop.domain.repository

import pe.fernanapps.shop.domain.model.chat.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    suspend fun subscribeToMessages(userId: String): Flow<Message>
    suspend fun sendMessage(message: Message)
    fun unsubscribeFromMessages()
}