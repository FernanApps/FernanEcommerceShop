package pe.fernanapps.shop.data.sources.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import pe.fernanapps.shop.data.sources.local.ConstantsDatabase
import pe.fernanapps.shop.domain.model.notifications.Notification
import pe.fernanapps.shop.domain.model.notifications.NotificationStatus
import pe.fernanapps.shop.domain.model.product.ProductCart








@Entity(tableName = ConstantsDatabase.TABLE_PRODUCT)
class ProductCartEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val subtitle: String,
    val description: String,
    val price: Float,
    val amount: Int = 1,
    @ColumnInfo(name = "sizes_available") val sizesAvailable: ArrayList<String>,
    @ColumnInfo(name = "size_selected") val sizeSelected: String,
    val color: Int,
    val image: String,
    @ColumnInfo(name = "category_id") val categoryId: String
)

fun ProductCartEntity.toCart() = ProductCart(
    id, title, subtitle, description, price, amount, sizesAvailable, sizeSelected, color, image, categoryId
)

fun ProductCart.toEntity() = ProductCartEntity(
    id, title, subtitle, description, price, amount, sizesAvailable, sizeSelected, color, image, categoryId
)