package pe.fernanapps.shop.model

import com.google.gson.annotations.SerializedName
import pe.fernanapps.shop.domain.model.product.Offer
import pe.fernanapps.shop.domain.model.product.Product

data class OfferCollection(
    @SerializedName("code") val code: String,
    @SerializedName("description") val description: String,
    @SerializedName("product_id") val productId: String,
    @SerializedName("image") val image: String,
    @SerializedName("title") val title: String,
) {
    fun toDomain(product: Product) = Offer(code, description, product, image, title)
}