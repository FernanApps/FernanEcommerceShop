package pe.fernanapps.shop.ui.main.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pe.fernanapps.shop.domain.model.product.ProductCart
import pe.fernanapps.shop.domain.usecases.cart.DeleteAllProductInCartUseCase
import pe.fernanapps.shop.domain.usecases.cart.DeleteProductInCartUseCase
import pe.fernanapps.shop.domain.usecases.cart.GetProductsFromCartUseCase
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getProductsFromCartUseCase: GetProductsFromCartUseCase,
    private val deleteProductInCartUseCase: DeleteProductInCartUseCase,
    private val deleteAllProductInCartUseCase: DeleteAllProductInCartUseCase
) : ViewModel() {

    private val _products = MutableLiveData<List<ProductCart>>()
    val products: LiveData<List<ProductCart>> get() = _products

    private val _priceTotal = MutableLiveData<Float>()
    val priceTotal: LiveData<Float> get() = _priceTotal


    fun deleteAll(){
        viewModelScope.launch {
            deleteAllProductInCartUseCase()
        }
    }


    fun deleteProduct(productCart: ProductCart) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteProductInCartUseCase(productCart)
            getUltimateProductsInCart()
        }
    }


    fun getUltimateProductsInCart() {
        viewModelScope.launch {
            val products = getProductsFromCartUseCase() ?: emptyList()
            _products.postValue(products)
            setPriceTotal(products)
        }
    }

    private fun setPriceTotal(products: List<ProductCart>) {
        val priceTotal = products.sumOf { it.finalPrice().toDouble() }.toFloat()
//            // Price Total
//            var priceTotal = 0f
//            products.forEach {
//                priceTotal += it.finalPrice()
//            }

        _priceTotal.value = priceTotal
    }

}