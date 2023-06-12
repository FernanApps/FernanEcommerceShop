package pe.fernanapps.shop.data.sources.local

import pe.fernanapps.shop.domain.model.product.ProductCart
import pe.fernanapps.shop.domain.repository.ProductCartRepository
import javax.inject.Inject

class ProductCartRepositoryImp @Inject constructor(
    private val local: LocalDataSource,
) : ProductCartRepository {

    override suspend fun getAllProducts() = local.getAllProductsInCart()

    override suspend fun insertAllProducts(productList: List<ProductCart>) =
        local.insertAllProductsInCart(productList)

    override suspend fun deleteAllProducts() = local.deleteAllProductsInCart()

    override suspend fun deleteProduct(product: ProductCart) =
        local.deleteProductInCart(product)

    override suspend fun getProductById(product: String) = local.getProductInCartById(product)

    override suspend fun saveProduct(product: ProductCart) = local.saveProductInCart(product)

}


