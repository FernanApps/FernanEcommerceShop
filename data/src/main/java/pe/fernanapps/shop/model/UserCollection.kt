package pe.fernanapps.shop.model

import com.google.gson.annotations.SerializedName
import pe.fernanapps.shop.domain.Constants
import pe.fernanapps.shop.domain.model.user.Address
import pe.fernanapps.shop.domain.model.user.User


/**





postal_code
state
 */


data class UserCollection(
    @SerializedName("id") val id: String = Constants.INFO_NOT_SET,
    @SerializedName("email") val email: String = Constants.INFO_NOT_SET,
    @SerializedName("name") val name: String = Constants.INFO_NOT_SET,
    @SerializedName("phone") val phone: String = Constants.INFO_NOT_SET,

    @SerializedName("city") val city: String = Constants.INFO_NOT_SET,
    @SerializedName("country") val country: String = Constants.INFO_NOT_SET,
    @SerializedName("line_1") val line1: String = Constants.INFO_NOT_SET,
    @SerializedName("line_2") val line2: String = Constants.INFO_NOT_SET,
    @SerializedName("postal_code") val postalCode: String = Constants.INFO_NOT_SET,
    @SerializedName("state") val state: String = Constants.INFO_NOT_SET,
)

fun User.toCollection(): UserCollection {
    val address = this.address
    return UserCollection(
        id = this.id,
        email = this.email,
        name = this.name,
        phone = this.phone,
        city = address.city!!,
        country = address.country!!,
        line1 = address.line1!!,
        line2 = address.line2!!,
        postalCode = address.postalCode!!,
        state = address.state!!
    )
}

fun UserCollection.toDomain() = User(
    id = this.id,
    email = this.email,
    name = this.name,
    phone = this.phone,
    address = Address(
        city = this.city,
        country = this.country,
        line1 = this.line1,
        line2 = this.line2,
        postalCode = this.postalCode,
        state = this.state
    )
)