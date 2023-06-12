package pe.fernanapps.shop.domain.model.product

import pe.fernanapps.shop.domain.model.To

data class Offer(
    val code: String,
    val description: String,
    val product: Product,
    val image: String,
    val title: String,
) {
    override fun toString(): String {
        return "Offer(code='$code', description='$description', product=$product, image='$image', title='$title')"
    }
}


