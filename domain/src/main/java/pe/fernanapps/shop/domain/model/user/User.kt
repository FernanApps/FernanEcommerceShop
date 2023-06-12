package pe.fernanapps.shop.domain.model.user

import pe.fernanapps.shop.domain.Constants.INFO_NOT_SET
import pe.fernanapps.shop.domain.model.To

data class User (
    override val id: String = INFO_NOT_SET,
    val email: String = INFO_NOT_SET,
    val name: String = INFO_NOT_SET,
    val phone: String = INFO_NOT_SET,
    val address: Address = Address()
): To

data class Address constructor(
    val city: String? = null,
    val country: String? = null, // two-character country code
    val line1: String? = null,
    val line2: String? = null,
    val postalCode: String? = null,
    val state: String? = null
)


data class Card constructor(
    val number: String? = null,
    val expiryMonth: Int? = null,
    val expiryYear: Int? = null,
    val cvc: String? = null,
    val brand: String = "",
    val id: String = ""
)