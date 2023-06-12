package pe.fernanapps.shop.domain.usecases.cart

import pe.fernanapps.shop.domain.model.product.ProductCart
import pe.fernanapps.shop.domain.repository.ProductCartRepository
import javax.inject.Inject

class GetProductsFromCartUseCase @Inject constructor(
    val repository: ProductCartRepository
) {
    suspend operator fun invoke() = repository.getAllProducts()
}


class DeleteProductInCartUseCase @Inject constructor(
    private val repository: ProductCartRepository
) {
    suspend operator fun invoke(product: ProductCart) = repository.deleteProduct(product)
}


class InsertProductInCartUseCase @Inject constructor(
    private val repository: ProductCartRepository
) {
    suspend operator fun invoke(product: ProductCart) = repository.saveProduct(product)
}


class DeleteAllProductInCartUseCase @Inject constructor(
    private val repository: ProductCartRepository
) {
    suspend operator fun invoke() = repository.deleteAllProducts()
}