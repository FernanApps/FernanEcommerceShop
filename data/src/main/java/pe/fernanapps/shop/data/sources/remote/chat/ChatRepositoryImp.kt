package pe.fernanapps.shop.data.sources.remote.chat

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import pe.fernanapps.shop.domain.DataState
import pe.fernanapps.shop.domain.model.chat.Message
import pe.fernanapps.shop.domain.repository.ChatRepository
import javax.inject.Inject


class ChatRepositoryImp @Inject constructor(private val chatService: ChatService) : ChatRepository {
    override fun getChats(userId: String): Flow<DataState<List<Message>>> = flow {
        try {
            emit(DataState.Loading)
            val messages = chatService.getChats(userId).map { it.toDomain() }
            emit(DataState.Success(messages))
            delay(1000)
            emit(DataState.Finished)
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }.catch {
        emit(DataState.Error(it))
    }

}