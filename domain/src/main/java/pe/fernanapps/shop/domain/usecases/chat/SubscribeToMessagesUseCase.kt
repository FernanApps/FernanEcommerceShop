package pe.fernanapps.shop.domain.usecases.chat

import kotlinx.coroutines.flow.Flow
import pe.fernanapps.shop.domain.model.chat.Message
import pe.fernanapps.shop.domain.repository.MessageRepository
import javax.inject.Inject

class SubscribeToMessagesUseCase @Inject constructor(private val messageRepository: MessageRepository) {
    suspend operator fun invoke(userId: String): Flow<Message> = messageRepository.subscribeToMessages(userId)
}