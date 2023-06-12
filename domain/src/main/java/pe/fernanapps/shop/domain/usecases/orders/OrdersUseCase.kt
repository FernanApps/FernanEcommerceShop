package pe.fernanapps.shop.domain.usecases.orders

import kotlinx.coroutines.flow.Flow
import pe.fernanapps.shop.domain.DataState
import pe.fernanapps.shop.domain.model.order.OrderDetails
import pe.fernanapps.shop.domain.model.order.OrderItem
import pe.fernanapps.shop.domain.repository.OrderRepository
import javax.inject.Inject


class GetAllOrdersDetailsAndItemsUseCase @Inject constructor(private val orderRepository: OrderRepository) {
    operator fun invoke(): Flow<DataState<List<OrderDetails>>> {
        return orderRepository.getAllOrdersDetailsAndItems()
    }
}


class GetAllOrdersDetailsUseCase @Inject constructor(private val orderRepository: OrderRepository) {
    operator fun invoke(): Flow<DataState<List<OrderDetails>>> {
        return orderRepository.getAllOrdersDetails()
    }
}

class GetAllOrdersItemUseCase @Inject constructor(private val orderRepository: OrderRepository) {
    operator fun invoke(orderId: String): Flow<DataState<List<OrderItem>>> {
        return orderRepository.getAllOrdersItem(orderId)
    }
}
