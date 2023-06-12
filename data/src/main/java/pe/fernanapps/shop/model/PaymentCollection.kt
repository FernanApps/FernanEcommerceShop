package pe.fernanapps.shop.model

import com.google.gson.annotations.SerializedName
import pe.fernanapps.shop.domain.model.order.OrderDetails
import pe.fernanapps.shop.domain.model.order.OrderItem
import pe.fernanapps.shop.domain.model.order.OrderStatus
import pe.fernanapps.shop.domain.model.product.Product
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
data class PaymentCollection(
    @SerializedName("id") val id: String,
    @SerializedName("client_id") val clientId: String,
    @SerializedName("order_id") val orderId: String,
    @SerializedName("amount") val amount: Float,
    @SerializedName("payment_type") val paymentType: String,
    @SerializedName("created_at") val createdAt: Long = Date().time,
    @SerializedName("currency") val currency: String,
)


/**

------------------ order_details ----------------------
id
user_id
payment_id
total
created_at
status

 */
data class OrderDetailsCollection(
    @SerializedName("id") val id: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("payment_id") val paymentId: String,
    @SerializedName("total") val total: Float,
    @SerializedName("created_at") val createdAt: Long = Date().time,
    @SerializedName("status") val status: OrderStatus? = OrderStatus.PENDING,

    ) {
    fun toDomain() = OrderDetails(id, userId, paymentId, total, createdAt, status ?: OrderStatus.PENDING)
}


/**
------------------ order_item ---------------------- // Items In Cart
id
product_id
order_id // Order Details Id
quantity
created_at
sub_total
 */
data class OrderItemCollection(
    @SerializedName("id") val id: String,
    @SerializedName("product_id") val productId: String,
    @SerializedName("order_id") val orderId: String,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("size") val size: String,
    @SerializedName("created_at") val createdAt: Long = Date().time,
    @SerializedName("sub_total") val subTotal: Long,
)

fun OrderItemCollection.toDomain() = OrderItem(
    id = this.id,
    product = null,
    orderId = this.orderId,
    quantity = this.quantity,
    size = this.size,
    createdAt = this.createdAt,
    subTotal = this.subTotal
)
