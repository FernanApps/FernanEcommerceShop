package pe.fernanapps.shop.model

import com.google.gson.annotations.SerializedName
import pe.fernanapps.shop.domain.model.product.Product
import pe.fernanapps.shop.domain.model.product.ProductCart
import pe.fernanapps.shop.utils.toModel

/*

	{
		"id": "CAT1PROD1",
		"title": "Casual Shirt",
		"subtitle": "Comfortable shirt for everyday wear",
		"description": "This casual shirt is perfect for a relaxed and effortless look.",
		"price": 24.99,
		"size": "[\"S\",\"M\",\"L\"]",
		"category": "cat1",
		"image": "https://cloud.approject"
	}

 */
data class ProductCollection(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("subtitle") val subtitle: String,
    @SerializedName("description") val description: String,
    @SerializedName("price") val price: Float,
    @SerializedName("size") val size: String,
    @SerializedName("category") val category: String,
    @SerializedName("image") val image: String,
) {
    override fun toString(): String {
        return "ProductCollection(id='$id', title='$title', subtitle='$subtitle', description='$description', price=$price, size='$size', category='$category', image='$image')"
    }


}

// Mappers
fun ProductCollection.toDomain() = Product(
    id,
    title,
    subtitle,
    description,
    price,
    size?.toModel<ArrayList<String>>() ?: arrayListOf(),
    image,
    category
)

fun ProductCart.toProductBase() = Product(
    id,
    title,
    subtitle,
    description,
    price,
    sizesAvailable.ifEmpty { arrayListOf() },
    image,
    categoryId
)
