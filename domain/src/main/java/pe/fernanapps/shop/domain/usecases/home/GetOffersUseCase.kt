package pe.fernanapps.shop.domain.usecases.home

import kotlinx.coroutines.flow.Flow
import pe.fernanapps.shop.domain.DataState
import pe.fernanapps.shop.domain.model.product.Offer
import pe.fernanapps.shop.domain.repository.ProductRepository
import javax.inject.Inject

class GetOffersUseCase @Inject constructor(private val repository: ProductRepository) {
    operator fun invoke(): Flow<DataState<List<Offer>>> {
        return repository.getAllOffersFromApi()
    }
}