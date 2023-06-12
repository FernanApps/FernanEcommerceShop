package pe.fernanapps.shop.data.sources

import kotlinx.coroutines.flow.Flow
import pe.fernanapps.shop.domain.NetworkResult
import pe.fernanapps.shop.domain.model.notifications.Notification
import pe.fernanapps.shop.domain.model.product.Category
import pe.fernanapps.shop.domain.model.product.Offer
import pe.fernanapps.shop.domain.model.product.Product
import pe.fernanapps.shop.domain.model.product.ProductCart
import pe.fernanapps.shop.domain.repository.PreferencesRepository


interface LoginDataSource {
    interface Remote {
        suspend fun getOffers(): NetworkResult<List<Offer>>
        suspend fun getProducts(): NetworkResult<List<Product>>
        suspend fun getCategories(): NetworkResult<List<Category>>

    }

    interface Local: PreferencesRepository{
        override fun <T> getValue(key: String, defaultValue: T): T {
            TODO("Not yet implemented")
        }

        override fun <T> putValue(key: String, value: T) {
            TODO("Not yet implemented")
        }

    }
}




interface EcommerceDataSource {
    interface Remote {
        suspend fun getOffers(): NetworkResult<List<Offer>>
        suspend fun getProducts(categoryId:String?, page: Int, size: Int): NetworkResult<List<Product>>
        suspend fun getCategories(): NetworkResult<List<Category>>

    }

    interface Local {
        // Cart Products
        suspend fun getAllProductsInCart(): List<ProductCart>
        suspend fun insertAllProductsInCart(products: List<ProductCart>)
        suspend fun deleteAllProductsInCart()

        suspend fun deleteProductInCart(product: ProductCart)
        suspend fun getProductInCartById(productId: String): ProductCart?
        suspend fun saveProductInCart(product: ProductCart)


        // Favorites
        suspend fun getAllProductsFavorite(): List<Product>
        suspend fun insertAllProductsFavorite(products: List<Product>)
        suspend fun deleteAllProductsFavorite()

        suspend fun deleteProductFavorite(product: Product)
        suspend fun getProductFavoriteById(productId: String): Product?
        suspend fun saveProductFavorite(product: Product)

        suspend fun getAllNotifications(): Flow<List<Notification>>
        suspend fun save(notification: Notification)
        suspend fun saveAll(list: List<Notification>)
    }
}
