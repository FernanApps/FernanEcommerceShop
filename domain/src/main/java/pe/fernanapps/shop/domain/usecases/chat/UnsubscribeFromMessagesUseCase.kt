package pe.fernanapps.shop.domain.usecases.chat

import pe.fernanapps.shop.domain.repository.MessageRepository
import javax.inject.Inject

class UnsubscribeFromMessagesUseCase @Inject constructor(private val messageRepository: MessageRepository) {
    operator fun invoke() = messageRepository.unsubscribeFromMessages()
}