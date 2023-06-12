package pe.fernanapps.shop.ui.main.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pe.fernanapps.shop.domain.DataState
import pe.fernanapps.shop.domain.model.order.OrderDetails
import pe.fernanapps.shop.domain.model.order.OrderStatus
import pe.fernanapps.shop.domain.usecases.orders.GetAllOrdersDetailsAndItemsUseCase
import pe.fernanapps.shop.domain.usecases.orders.GetAllOrdersDetailsUseCase
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val getAllOrdersDetailsUseCase: GetAllOrdersDetailsUseCase,
    private val getAllOrdersDetailsAndItemsUseCase: GetAllOrdersDetailsAndItemsUseCase,
) : ViewModel() {

    private val _sizeOrdersDetails = MutableLiveData<Int>(0)
    val sizeOrdersDetails: LiveData<Int> get() = _sizeOrdersDetails

    private val _ordersDetailsWithItems = MutableLiveData<DataState<List<OrderDetails>>>()
    val ordersDetailsWithItems: LiveData<DataState<List<OrderDetails>>> get() = _ordersDetailsWithItems

    private val _ordersDetailsWithItemFiltered = MutableLiveData<List<OrderDetails>>()
    val ordersDetailsWithItemsFiltered: LiveData<List<OrderDetails>> get() = _ordersDetailsWithItemFiltered


    fun getSizeOrderDetails(){
        viewModelScope.launch {
            getAllOrdersDetailsUseCase().collect{
                when(it){
                    is DataState.Success -> {
                        _sizeOrdersDetails.value = it.data.size
                    }
                    else -> Unit
                }

            }
        }
    }

    fun getAllOrderDetailsWithItems() {
        viewModelScope.launch {
            getAllOrdersDetailsAndItemsUseCase().collect {
                _ordersDetailsWithItems.value = it
            }
        }
    }

    private var currentList: List<OrderDetails>? = null

    private val _currentOrderDetails = MutableLiveData<OrderDetails>()
    val currentOrderDetails: LiveData<OrderDetails> get() = _currentOrderDetails

    fun setCurrentOrderDetailsList(list: List<OrderDetails>) {
        currentList = ArrayList(list)
    }
    fun setCurrentOrderDetailsAndObserve(order: OrderDetails) {
        _currentOrderDetails.value = order
    }


    fun filteredListOrdersNotCompleted() {
        viewModelScope.launch(Dispatchers.IO) {
            val final =
                currentList?.filterNot { it.status == OrderStatus.CANCELLED || it.status == OrderStatus.DELIVERED }
            _ordersDetailsWithItemFiltered.postValue(final ?: emptyList())
        }
    }

    fun filteredListOrdersCompleted() {
        viewModelScope.launch(Dispatchers.IO) {
            val final =
                currentList?.filter { it.status == OrderStatus.DELIVERED }
            _ordersDetailsWithItemFiltered.postValue(final ?: emptyList())
        }
    }
}