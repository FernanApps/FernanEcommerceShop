package pe.fernanapps.shop.data.sources.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pe.fernanapps.shop.data.sources.EcommerceDataSource
import pe.fernanapps.shop.data.sources.local.dao.NotificationDao
import pe.fernanapps.shop.data.sources.local.dao.ProductCartDao
import pe.fernanapps.shop.data.sources.local.dao.ProductFavoriteDao
import pe.fernanapps.shop.data.sources.local.entity.toCart
import pe.fernanapps.shop.data.sources.local.entity.toDomain
import pe.fernanapps.shop.data.sources.local.entity.toEntity
import pe.fernanapps.shop.data.sources.local.entity.toFavoriteEntity
import pe.fernanapps.shop.domain.model.notifications.Notification
import pe.fernanapps.shop.domain.model.product.Product
import pe.fernanapps.shop.domain.model.product.ProductCart
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val productCartDao: ProductCartDao,
    private val favoriteDao: ProductFavoriteDao,
    private val notificationDao: NotificationDao,
) : EcommerceDataSource.Local {

    // Cart Products
    override suspend fun getAllProductsInCart(): List<ProductCart> {
        return productCartDao.getAllProducts().map {
            it.toCart()
        }
    }

    override suspend fun insertAllProductsInCart(products: List<ProductCart>) {
        productCartDao.insertAllProducts(products.map { it.toEntity() })
    }

    override suspend fun deleteAllProductsInCart() {
        productCartDao.deleteAllProducts()
    }

    override suspend fun deleteProductInCart(product: ProductCart) {
        productCartDao.deleteProduct(product.toEntity())
    }

    override suspend fun getProductInCartById(productId: String): ProductCart? {
        return productCartDao.getProductById(productId)?.toCart()
    }

    override suspend fun saveProductInCart(product: ProductCart) {
        productCartDao.saveProduct(product.toEntity())
    }


    // Favorite Products

    override suspend fun getAllProductsFavorite(): List<Product> {
        return favoriteDao.getAllProducts().map { it.toDomain().copy(favorite = true) }
    }

    override suspend fun insertAllProductsFavorite(products: List<Product>) {
        favoriteDao.insertAllProducts(products.map { it.toFavoriteEntity() })
    }

    override suspend fun deleteAllProductsFavorite() {
        favoriteDao.deleteAllProducts()
    }

    override suspend fun deleteProductFavorite(product: Product) {
        favoriteDao.deleteProduct(product.toFavoriteEntity())
    }

    override suspend fun getProductFavoriteById(productId: String): Product? {
        return (favoriteDao.getProductById(productId)?.toDomain())?.copy(favorite = true)
    }

    override suspend fun saveProductFavorite(product: Product) {
        return favoriteDao.saveProduct(product.toFavoriteEntity())
    }

    override suspend fun getAllNotifications(): Flow<List<Notification>> {
        return notificationDao.getAll().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun save(notification: Notification) {
        notificationDao.save(notification.toEntity())
    }

    override suspend fun saveAll(list: List<Notification>) {
        notificationDao.insertAll(list.map { it.toEntity() })
    }

}