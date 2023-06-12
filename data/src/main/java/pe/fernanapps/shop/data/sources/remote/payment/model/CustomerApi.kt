package pe.fernanapps.shop.data.sources.remote.payment.model

import com.google.gson.annotations.SerializedName

data class CustomerApi(
    @SerializedName("id") val id: String = "",
    @SerializedName("customer_id") val customerId: String,
    @SerializedName("name") val name: String = "",
    @SerializedName("email") val email: String = ""
)
