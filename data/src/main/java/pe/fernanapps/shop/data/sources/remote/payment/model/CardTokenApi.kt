package pe.fernanapps.shop.data.sources.remote.payment.model


import com.google.gson.annotations.SerializedName

data class CardTokenApi(
    @SerializedName("token")
    val token: String?
)