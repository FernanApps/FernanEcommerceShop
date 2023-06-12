package pe.fernanapps.shop.data.sources.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import pe.fernanapps.shop.data.sources.local.ConstantsDatabase
import pe.fernanapps.shop.data.sources.local.entity.NotificationEntity
import pe.fernanapps.shop.data.sources.local.entity.ProductCartEntity

private const val TABLE = ConstantsDatabase.TABLE_NOTIFICATIONS

@Dao
interface NotificationDao {

    @Query("SELECT * FROM $TABLE")
    fun getAll(): Flow<List<NotificationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(product: List<NotificationEntity>)

    @Query("DELETE FROM $TABLE")
    suspend fun deleteAll()

    @Delete
    suspend fun deleteItem(product: NotificationEntity)

    @Query("SELECT * FROM $TABLE WHERE created_at = :date")
    suspend fun getProductByDate(date: Long): NotificationEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(item: NotificationEntity)
}