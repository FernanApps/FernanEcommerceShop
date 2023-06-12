package pe.fernanapps.shop.domain.model.payment

import pe.fernanapps.shop.domain.PaymentStatusCode
import pe.fernanapps.shop.domain.model.To
import pe.fernanapps.shop.domain.model.order.OrderStatus
import java.util.Date

/**
------------------ payments ----------------------
id
client_id
order_id
amount
payment_type
created_at
currency

 */
data class Payment(
    override val id: String,
    val clientId: String = "",
    val orderId: String = "",
    val amount: Float,
    val paymentType: String,
    val createdAt: Long = Date().time,
    val currency: String,
    val status: PaymentStatusCode
): To


