package pe.fernanapps.shop.data.sources.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import pe.fernanapps.shop.data.sources.local.dao.NotificationDao
import pe.fernanapps.shop.data.sources.local.dao.ProductCartDao
import pe.fernanapps.shop.data.sources.local.dao.ProductFavoriteDao
import pe.fernanapps.shop.data.sources.local.entity.NotificationEntity
import pe.fernanapps.shop.data.sources.local.entity.ProductCartEntity
import pe.fernanapps.shop.data.sources.local.entity.ProductFavoriteEntity
import pe.fernanapps.shop.utils.StringListConverter


@Database(
    entities = [
        ProductCartEntity::class,
        ProductFavoriteEntity::class,
        NotificationEntity::class
    ],
    version = 1
)
@TypeConverters(StringListConverter::class)
abstract class EcommerceDatabase : RoomDatabase() {

    abstract fun getProductDao(): ProductCartDao
    abstract fun getFavoriteDao(): ProductFavoriteDao
    abstract fun getNotificationDao(): NotificationDao

}