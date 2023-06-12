package pe.fernanapps.shop.data.sources.remote.orders

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import pe.fernanapps.shop.data.sources.local.user.UserPreference
import pe.fernanapps.shop.domain.DataState
import pe.fernanapps.shop.domain.model.order.OrderDetails
import pe.fernanapps.shop.domain.model.order.OrderItem
import pe.fernanapps.shop.domain.repository.OrderRepository
import javax.inject.Inject


class OrderRepositoryImp @Inject constructor(
    private val userPreference: UserPreference,
    private val orderService: OrderService,
) : OrderRepository {
    override fun getAllOrdersDetailsAndItems() = flow<DataState<List<OrderDetails>>> {
        emit(DataState.Loading)
        val userId = userPreference.getUserFromPreferences()!!.id
        try {
            val finalOrderDetailsList = mutableListOf<OrderDetails>()

            val orderDetailsList = orderService.getAllOrdersDetails(userId)
            if (orderDetailsList.isNotEmpty()) {
                orderDetailsList.forEach { orderDetails ->
                    val ordersItemList = orderService.getAllOrdersItem(orderDetails.id)
                    if (ordersItemList.isNotEmpty()) {
                        finalOrderDetailsList.add(orderDetails.copy(ordersItems = ordersItemList))
                    }
                }
            }
            if(finalOrderDetailsList.isNotEmpty()){
                // Order By Cread
                emit(DataState.Success(finalOrderDetailsList.sortedByDescending { it.createdAt }))
            }
            delay(1500)
        } catch (e: Exception) {

        }

        emit(DataState.Finished)
    }.catch {
        emit(DataState.Error(it))
    }



    override fun getAllOrdersDetails() = flow<DataState<List<OrderDetails>>> {
        emit(DataState.Loading)
        val userId = userPreference.getUserFromPreferences()!!.id
        try {
            val list = orderService.getAllOrdersDetails(userId)
            if (list.isNotEmpty()) {
                emit(DataState.Success(list))
            }
            delay(1500)
        } catch (e: Exception) {

        }

        emit(DataState.Finished)
    }.catch {
        emit(DataState.Error(it))
    }

    override fun getAllOrdersItem(orderId: String) = flow<DataState<List<OrderItem>>> {
        emit(DataState.Loading)
        try {
            val list = orderService.getAllOrdersItem(orderId)
            if (list.isNotEmpty()) {
                emit(DataState.Success(list))
            }
        } catch (e: Exception){

        }

        delay(1500)
        emit(DataState.Finished)

    }.catch {
        emit(DataState.Error(it))
    }


}