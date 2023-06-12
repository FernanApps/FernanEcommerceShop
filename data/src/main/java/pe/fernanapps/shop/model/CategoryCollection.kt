package pe.fernanapps.shop.model

import com.google.gson.annotations.SerializedName
import pe.fernanapps.shop.domain.model.product.Category

// { "id": "cat1", "name": "Shirts" },
data class CategoryCollection(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("image") val image: String
) {
    fun toDomain() = Category(this.id, this.title, "", this.image)
}



