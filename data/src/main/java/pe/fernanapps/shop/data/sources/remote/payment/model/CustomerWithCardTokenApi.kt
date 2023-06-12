package pe.fernanapps.shop.data.sources.remote.payment.model


import com.google.gson.annotations.SerializedName

data class CustomerWithCardTokenApi(
    @SerializedName("card_token")
    val cardToken: String?,
    @SerializedName("customer_id")
    val customerId: String?
)