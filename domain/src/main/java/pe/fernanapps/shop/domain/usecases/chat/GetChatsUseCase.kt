package pe.fernanapps.shop.domain.usecases.chat

import pe.fernanapps.shop.domain.model.chat.Message
import pe.fernanapps.shop.domain.repository.ChatRepository
import pe.fernanapps.shop.domain.repository.MessageRepository
import javax.inject.Inject

class GetChatsUseCase @Inject constructor(private val chatRepository: ChatRepository) {
    suspend operator fun invoke(userId: String) = chatRepository.getChats(userId)
}
