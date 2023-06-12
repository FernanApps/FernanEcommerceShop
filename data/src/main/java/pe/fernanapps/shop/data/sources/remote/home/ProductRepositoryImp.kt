package pe.fernanapps.shop.data.sources.remote.home

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import pe.fernanapps.shop.data.sources.EcommerceDataSource
import pe.fernanapps.shop.domain.DataState
import pe.fernanapps.shop.domain.NetworkResult
import pe.fernanapps.shop.domain.model.product.Category
import pe.fernanapps.shop.domain.model.product.Offer
import pe.fernanapps.shop.domain.model.product.Product
import pe.fernanapps.shop.domain.repository.ProductRepository
import javax.inject.Inject

class ProductRepositoryImp @Inject constructor(
    private val remote: EcommerceDataSource.Remote,
) : ProductRepository {
    override fun getAllOffersFromApi(): Flow<DataState<List<Offer>>> = flow {
        flowNetworkResulToDataState(remote.getOffers()).collect { emit(it) }
    }

    override fun getAllProductsFromApi(
        categoryId: String?,
        page: Int,
        size: Int,
    ): Flow<DataState<List<Product>>> = flow {
        flowNetworkResulToDataState(remote.getProducts(categoryId, page, size)).collect { emit(it) }
    }

    override fun getAllCategoriesFromApi(): Flow<DataState<List<Category>>> = flow {
        flowNetworkResulToDataState(remote.getCategories()).collect { emit(it) }
    }


}

private inline fun <reified T : Any> flowNetworkResulToDataState(networkGeneric: NetworkResult<T>):
        Flow<DataState<T>> = flow {
    emit(DataState.Loading)
    try {
        when (val response: NetworkResult<T> = networkGeneric) {
            is NetworkResult.Exception -> emit(DataState.Error(response.e))
            is NetworkResult.Failure -> emit(DataState.Error(Exception("Failure Network : ${response.code} > ${response.message}")))
            is NetworkResult.Success -> emit(DataState.Success(response.data))
        }
    } catch (e: Exception) {
        emit(DataState.Error(e))
    }
    emit(DataState.Finished)
}.catch {
    emit(DataState.Error(it))
}

