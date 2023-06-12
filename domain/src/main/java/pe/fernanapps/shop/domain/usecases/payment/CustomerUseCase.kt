package pe.fernanapps.shop.domain.usecases.payment

import pe.fernanapps.shop.domain.model.user.Card
import pe.fernanapps.shop.domain.repository.CustomerRepository
import javax.inject.Inject

class GetCustomerCardsUseCase @Inject constructor(private val customerRepository: CustomerRepository) {
    suspend operator fun invoke() =
        customerRepository.getCustomerCards()
}

class SaveCardUseCase @Inject constructor(private val customerRepository: CustomerRepository) {
    suspend operator fun invoke(card: Card) =
        customerRepository.saveCard(card)

}