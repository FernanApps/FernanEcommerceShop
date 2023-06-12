package pe.fernanapps.shop.domain.usecases.payment

import kotlinx.coroutines.flow.Flow
import pe.fernanapps.shop.domain.DataState
import pe.fernanapps.shop.domain.model.payment.Payment
import pe.fernanapps.shop.domain.model.user.Card
import pe.fernanapps.shop.domain.model.user.User
import pe.fernanapps.shop.domain.repository.PaymentRepository
import javax.inject.Inject


class ProcessPaymentUseCase @Inject constructor(private val paymentRepository: PaymentRepository) {
    suspend operator fun invoke(card: Card): Flow<DataState<Payment>> {
        return paymentRepository.createPayment(card)
    }
}

class GenerateOrderUseCase @Inject constructor(private val paymentRepository: PaymentRepository) {
    suspend operator fun invoke(payment: Payment): Flow<DataState<Boolean>> {
        return paymentRepository.generateOrder(payment)
    }
}
