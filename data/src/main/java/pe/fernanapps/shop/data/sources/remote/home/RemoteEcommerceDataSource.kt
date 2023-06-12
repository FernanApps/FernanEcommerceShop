package pe.fernanapps.shop.data.sources.remote.home


import pe.fernanapps.shop.data.sources.EcommerceDataSource
import pe.fernanapps.shop.domain.NetworkResult
import pe.fernanapps.shop.domain.model.product.Category
import pe.fernanapps.shop.domain.model.product.Offer
import pe.fernanapps.shop.domain.model.product.Product
import javax.inject.Inject

class RemoteEcommerceDataSource @Inject constructor(
    private val productService: ProductService
): EcommerceDataSource.Remote {
    override suspend fun getOffers(): NetworkResult<List<Offer>> = productService.getOffers()
    override suspend fun getProducts(categoryId:String?, page: Int, size: Int): NetworkResult<List<Product>> =
        productService.getProducts(categoryId, page, size)
    override suspend fun getCategories(): NetworkResult<List<Category>> =  productService.getCategories()

}