package pe.fernanapps.shop.domain.repository

import kotlinx.coroutines.flow.Flow
import pe.fernanapps.shop.domain.DataState
import pe.fernanapps.shop.domain.model.order.OrderDetails
import pe.fernanapps.shop.domain.model.order.OrderItem

interface OrderRepository {
    fun getAllOrdersDetailsAndItems(): Flow<DataState<List<OrderDetails>>>
    fun getAllOrdersDetails(): Flow<DataState<List<OrderDetails>>>
    fun getAllOrdersItem(orderId: String): Flow<DataState<List<OrderItem>>>
}