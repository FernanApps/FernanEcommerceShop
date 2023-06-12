package pe.fernanapps.shop.data.sources.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pe.fernanapps.shop.data.sources.local.ConstantsDatabase
import pe.fernanapps.shop.data.sources.local.entity.ProductCartEntity

private const val TABLE = ConstantsDatabase.TABLE_PRODUCT

@Dao
interface ProductCartDao {

    @Query("SELECT * FROM $TABLE")
    suspend fun getAllProducts(): List<ProductCartEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllProducts(product: List<ProductCartEntity>)

    @Query("DELETE FROM $TABLE")
    suspend fun deleteAllProducts()

    @Delete
    suspend fun deleteProduct(product: ProductCartEntity)

    @Query("SELECT * FROM $TABLE WHERE id = :id")
    suspend fun getProductById(id: String): ProductCartEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProduct(product: ProductCartEntity)
}


