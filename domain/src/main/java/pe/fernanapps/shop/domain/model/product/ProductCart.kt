package pe.fernanapps.shop.domain.model.product

import pe.fernanapps.shop.domain.model.To
import javax.swing.text.html.HTML.Tag.P

/*



 */


open class ProductCart(
    override val id: String,
    val title: String,
    val subtitle: String,
    val description: String,
    val price: Float,
    open val amount: Int = 1,
    val sizesAvailable: ArrayList<String>,
    val sizeSelected: String,
    open val color: Int,
    val image: String,
    val categoryId: String
): To {



    fun finalPrice() = (amount * price)

    override fun toString(): String {
        return "ProductCart(id='$id', title='$title', subtitle='$subtitle', description='$description', price=$price, amount=$amount, sizesAvailable=$sizesAvailable, sizeSelected='$sizeSelected', color=$color, image='$image', categoryId='$categoryId')"
    }


}

