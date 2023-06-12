package pe.fernanapps.shop.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pe.fernanapps.shop.domain.DataState
import pe.fernanapps.shop.domain.model.product.Offer
import pe.fernanapps.shop.domain.model.product.Product
import pe.fernanapps.shop.domain.usecases.home.GetOffersUseCase
import pe.fernanapps.shop.domain.usecases.home.GetProductsFromApiUseCase
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getOffersUseCase: GetOffersUseCase,
    private val getProductsUseCase: GetProductsFromApiUseCase
) : ViewModel() {


    private val _offers = MutableLiveData<DataState<List<Offer>>>()
    val offers: LiveData<DataState<List<Offer>>> get() = _offers


    private val _products = MutableLiveData<DataState<List<Product>>>()
    val products: LiveData<DataState<List<Product>>> get() = _products

    private val _isTitleVisible = MutableLiveData<Boolean>(false)
    val isTitleVisible: LiveData<Boolean> get() = _isTitleVisible

    fun setTitleVisible(value: Boolean){
        _isTitleVisible.value = value
    }

    fun getOffers() {

        viewModelScope.launch {
            getOffersUseCase().collect { result ->
                _offers.value = result
            }
//            loginUseCase(email, password)
//                .onEach { dataState ->
//                    _loginState.value = dataState
//                }.launchIn(viewModelScope)
        }
    }

    fun getProducts() {
        viewModelScope.launch {
            getProductsUseCase().collect { result ->
                _products.value = result

            }
        }
    }

    init {
        getOffers()
        getProducts()
    }

}