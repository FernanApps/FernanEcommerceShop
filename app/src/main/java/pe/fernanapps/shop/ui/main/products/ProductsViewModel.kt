package pe.fernanapps.shop.ui.main.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pe.fernanapps.shop.domain.DataState
import pe.fernanapps.shop.domain.model.product.Product
import pe.fernanapps.shop.domain.usecases.home.GetProductsFromApiUseCase
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsFromApiUseCase,
) : ViewModel() {

    private val _productsStatus = MutableLiveData<DataState<List<Product>>>()
    val productsStatus: LiveData<DataState<List<Product>>> get() = _productsStatus

    private val _loadProducts = MutableLiveData<Boolean>(false)
    val loadProducts: LiveData<Boolean> get() = _loadProducts

    private val _products = MutableLiveData<MutableList<Product>>()
    val products: LiveData<MutableList<Product>> get() = _products

    private val currentProducts =  mutableListOf<Product>()


    var productSize = 25
    private var page = 0

    private var isEnableForNewData = true


    private fun nextPage() {
        page += 1
    }



    fun addProducts(products: List<Product>) {
        currentProducts.addAll(products)
        isEnableForNewData = true
        _products.value = currentProducts
    }


    fun getProducts(categoryId: String? = null) {
        viewModelScope.launch {
            getProductsUseCase(categoryId, page, productSize).collect { result ->
                _productsStatus.value = result
            }
        }
    }

    fun onScrolled(dy: Int, childCount: Int, itemCount: Int, findFirstVisibleItemPosition: Int) {
        //check for scroll dow
        if (dy > 0) {
            if (isEnableForNewData) {
                if (childCount + findFirstVisibleItemPosition >= itemCount) {
                    nextPage()
                    isEnableForNewData = false
                    _loadProducts.value = true
                }
            }
        }
    }


    /**
     *
     */

    private val _productsOriginal get() = currentProducts

    private val _productsFiltered = MutableLiveData<List<Product>>()
    val productsFiltered: LiveData<List<Product>> get() = _productsFiltered


    fun filterProducts(query: String) {
        _productsOriginal.filter { it.title.contains(query, true) }.let { filteredList ->
            _productsFiltered.value = filteredList
        }
    }

}