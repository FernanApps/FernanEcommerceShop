package pe.fernanapps.shop.domain.usecases.chat

import pe.fernanapps.shop.domain.model.chat.Message
import pe.fernanapps.shop.domain.repository.MessageRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(private val messageRepository: MessageRepository) {
    suspend operator fun invoke(message: Message) = messageRepository.sendMessage(message)
}
