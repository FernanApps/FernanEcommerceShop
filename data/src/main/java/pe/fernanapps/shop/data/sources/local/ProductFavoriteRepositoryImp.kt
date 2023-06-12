package pe.fernanapps.shop.data.sources.local

import pe.fernanapps.shop.domain.model.product.Product
import pe.fernanapps.shop.domain.repository.ProductFavoriteRepository
import javax.inject.Inject

class ProductFavoriteRepositoryImp @Inject constructor(
    private val local: LocalDataSource,
) : ProductFavoriteRepository {

    override suspend fun getAllProducts() = local.getAllProductsFavorite()

    override suspend fun insertAllProducts(productList: List<Product>) =
        local.insertAllProductsFavorite(productList)

    override suspend fun deleteAllProducts() = local.deleteAllProductsFavorite()

    override suspend fun deleteProduct(product: Product) =
        local.deleteProductFavorite(product)

    override suspend fun getProductById(product: String) = local.getProductFavoriteById(product)

    override suspend fun saveProduct(product: Product) = local.saveProductFavorite(product)

}