package pe.fernanapps.shop.data.sources.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import pe.fernanapps.shop.data.sources.local.ConstantsDatabase
import pe.fernanapps.shop.domain.model.product.Product

@Entity(tableName = ConstantsDatabase.TABLE_FAVORITE)
class ProductFavoriteEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val subtitle: String,
    val description: String,
    val price: Float,
    val sizes: ArrayList<String>,
    val image: String,
    @ColumnInfo(name = "category_id") val categoryId: String,

    ) {

}

fun ProductFavoriteEntity.toDomain() = Product(
    id = id,
    title = title,
    subtitle = subtitle,
    description = description,
    price = price,
    size = sizes,
    image = image,
    category = categoryId,
)

fun Product.toFavoriteEntity() = ProductFavoriteEntity(
    id = id,
    title = title,
    subtitle = subtitle ?: "",
    description = description ?: "",
    price = price,
    sizes = size,
    image = image,
    categoryId = category,
)