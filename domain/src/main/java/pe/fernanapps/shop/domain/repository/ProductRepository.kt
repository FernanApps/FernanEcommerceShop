package pe.fernanapps.shop.domain.repository

import kotlinx.coroutines.flow.Flow
import pe.fernanapps.shop.domain.DataState
import pe.fernanapps.shop.domain.model.order.OrderDetails
import pe.fernanapps.shop.domain.model.product.Category
import pe.fernanapps.shop.domain.model.product.Offer
import pe.fernanapps.shop.domain.model.product.Product

interface ProductRepository {
    fun getAllOffersFromApi(): Flow<DataState<List<Offer>>>
    fun getAllProductsFromApi(categoryId: String?, page: Int, size: Int): Flow<DataState<List<Product>>>
    fun getAllCategoriesFromApi(): Flow<DataState<List<Category>>>
}

