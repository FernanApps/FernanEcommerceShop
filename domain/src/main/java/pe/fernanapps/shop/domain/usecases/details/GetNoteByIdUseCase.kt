package pe.fernanapps.shop.domain.usecases.details

import pe.fernanapps.shop.domain.repository.ProductCartRepository
import javax.inject.Inject

class GetProductByIdUseCase @Inject constructor(private val repository: ProductCartRepository) {

    suspend operator fun invoke(productId: String) = repository.getProductById(productId)

}