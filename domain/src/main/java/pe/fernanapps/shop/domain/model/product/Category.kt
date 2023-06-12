package pe.fernanapps.shop.domain.model.product

import pe.fernanapps.shop.domain.model.To

data class Category(
    override val id: String,
    val title: String,
    val description: String,
    val image: String
): To