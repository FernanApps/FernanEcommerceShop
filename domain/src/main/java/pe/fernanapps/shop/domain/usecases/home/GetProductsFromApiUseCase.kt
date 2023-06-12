package pe.fernanapps.shop.domain.usecases.home

import pe.fernanapps.shop.domain.repository.ProductRepository
import javax.inject.Inject

class GetProductsFromApiUseCase @Inject constructor(
    val repository: ProductRepository
) {
    operator fun invoke(categoryId: String? = null, page: Int = 0, size: Int = 25) = repository.getAllProductsFromApi(categoryId, page, size)
}

