package pe.fernanapps.shop.domain.usecases.favorite

import pe.fernanapps.shop.domain.model.product.Product
import pe.fernanapps.shop.domain.repository.ProductFavoriteRepository
import javax.inject.Inject

class GetAllProductsFavoriteUseCase @Inject constructor(private val productRepository: ProductFavoriteRepository) {
    suspend operator fun invoke(): List<Product> {
        return productRepository.getAllProducts()
    }
}

class InsertAllProductsFavoriteUseCase @Inject constructor(private val productRepository: ProductFavoriteRepository) {
    suspend operator fun invoke(productList: List<Product>) {
        productRepository.insertAllProducts(productList)
    }
}

class DeleteAllProductsFavoriteUseCase @Inject constructor(private val productRepository: ProductFavoriteRepository) {
    suspend operator fun invoke() {
        productRepository.deleteAllProducts()
    }
}

class DeleteProductFavoriteUseCase @Inject constructor(private val productRepository: ProductFavoriteRepository) {
    suspend operator fun invoke(product: Product) {
        productRepository.deleteProduct(product)
    }
}

class GetProductByIdFavoriteUseCase @Inject constructor(private val productRepository: ProductFavoriteRepository) {
    suspend operator fun invoke(productId: String): Product? {
        return productRepository.getProductById(productId)
    }
}

class SaveProductFavoriteUseCase @Inject constructor(private val productRepository: ProductFavoriteRepository) {
    suspend operator fun invoke(product: Product) {
        productRepository.saveProduct(product)
    }
}
