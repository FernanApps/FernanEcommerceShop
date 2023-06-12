package pe.fernanapps.shop.domain.model.order

import pe.fernanapps.shop.domain.model.product.Product
import java.io.Serializable
import java.util.Date

/**
------------------ order_details ----------------------
    id
    user_id
    payment_id
    total
    created_at

*/
data class OrderDetails(
    val id: String,
    val userId: String,
    val paymentId: String,
    val total: Float,
    val createdAt: Long,
    val status: OrderStatus,
    val ordersItems: List<OrderItem> = emptyList()
): Serializable



enum class OrderStatus(val status: String) {
    PENDING("Pending"),
    PROCESSING("Processing"),
    CONFIRMED("Confirmed"),
    SHIPPED("Shipped"),
    DELIVERED("Delivered"),
    CANCELLED("Cancelled")
}
// PENDING PROCESSING CONFIRMED SHIPPED DELIVERED CANCELLED


data class OrderItem(
    val id: String,
    val product: Product? = null,
    val orderId: String,
    val quantity: Int,
    val size: String,
    val createdAt: Long = Date().time,
    val subTotal: Long,
)