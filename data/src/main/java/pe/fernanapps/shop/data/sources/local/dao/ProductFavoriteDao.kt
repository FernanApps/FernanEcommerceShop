package pe.fernanapps.shop.data.sources.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pe.fernanapps.shop.data.sources.local.ConstantsDatabase
import pe.fernanapps.shop.data.sources.local.entity.ProductCartEntity
import pe.fernanapps.shop.data.sources.local.entity.ProductFavoriteEntity

private const val TABLE = ConstantsDatabase.TABLE_FAVORITE

@Dao
interface ProductFavoriteDao {

    @Query("SELECT * FROM $TABLE")
    suspend fun getAllProducts(): List<ProductFavoriteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllProducts(product: List<ProductFavoriteEntity>)

    @Query("DELETE FROM $TABLE")
    suspend fun deleteAllProducts()

    @Delete
    suspend fun deleteProduct(product: ProductFavoriteEntity)

    @Query("SELECT * FROM $TABLE WHERE id = :id")
    suspend fun getProductById(id: String): ProductFavoriteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProduct(product: ProductFavoriteEntity)
}