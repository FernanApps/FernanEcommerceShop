package pe.fernanapps.shop.domain.repository

import kotlinx.coroutines.flow.Flow
import pe.fernanapps.shop.domain.DataState
import pe.fernanapps.shop.domain.model.payment.Payment
import pe.fernanapps.shop.domain.model.user.Card
import pe.fernanapps.shop.domain.model.user.User

interface PaymentRepository {
    suspend fun createPayment(card: Card): Flow<DataState<Payment>>
    suspend fun generateOrder(payment: Payment): Flow<DataState<Boolean>>
    suspend fun completePayment(paymentId: String): Flow<Result<Payment>>
}