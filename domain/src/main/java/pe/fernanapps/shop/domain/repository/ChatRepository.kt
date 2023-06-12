package pe.fernanapps.shop.domain.repository

import kotlinx.coroutines.flow.Flow
import pe.fernanapps.shop.domain.DataState
import pe.fernanapps.shop.domain.model.chat.Message

interface ChatRepository {
    fun getChats(userId: String): Flow<DataState<List<Message>>>
}