package pe.fernanapps.shop.data.sources.remote.payment.model
import com.google.gson.annotations.SerializedName
import pe.fernanapps.shop.domain.model.user.Card

data class CardApi(
    @SerializedName("brand")
    val brand: String?,
    @SerializedName("expiry_month")
    val expiryMonth: Int,
    @SerializedName("expiry_year")
    val expiryYear: Int,
    @SerializedName("id")
    val id: String,
    @SerializedName("number")
    val number: String,
    @SerializedName("cvc")
    val cvc: String
) {
    fun toDomain() = Card(
        id = this.id,
        number = this.number,
        expiryYear = this.expiryYear,
        expiryMonth = this.expiryMonth,
        brand = this.brand ?: "",
        cvc = this.cvc

    )
}

fun Card.toApi() = CardApi(
    id = this.id,
    number = this.number!!,
    expiryYear = this.expiryYear!!,
    expiryMonth = this.expiryMonth!!,
    brand = this.brand!!,
    cvc = this.cvc!!

)
