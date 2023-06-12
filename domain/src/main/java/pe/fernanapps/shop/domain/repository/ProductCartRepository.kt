package pe.fernanapps.shop.domain.repository

import pe.fernanapps.shop.domain.model.product.Product
import pe.fernanapps.shop.domain.model.product.ProductCart


interface ProductLocalRepository<T> {

    suspend fun getAllProducts(): List<T>
    suspend fun insertAllProducts(productList: List<T>)
    suspend fun deleteAllProducts()

    suspend fun deleteProduct(product: T)
    suspend fun getProductById(product: String): T?
    suspend fun saveProduct(product: T)

}

interface ProductCartRepository: ProductLocalRepository<ProductCart>
interface ProductFavoriteRepository: ProductLocalRepository<Product>


