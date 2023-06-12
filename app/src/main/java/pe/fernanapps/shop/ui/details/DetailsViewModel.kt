package pe.fernanapps.shop.ui.details

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pe.fernan.list.selector.SelectorModel
import pe.fernanapps.shop.di.DetailViewModelModule
import pe.fernanapps.shop.domain.model.product.Product
import pe.fernanapps.shop.domain.model.product.ProductCart
import pe.fernanapps.shop.domain.usecases.cart.DeleteProductInCartUseCase
import pe.fernanapps.shop.domain.usecases.cart.InsertProductInCartUseCase
import pe.fernanapps.shop.domain.usecases.details.GetProductByIdUseCase
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val insertProductInCartUseCase: InsertProductInCartUseCase,
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val deleteProductInCartUseCase: DeleteProductInCartUseCase,
    @DetailViewModelModule.ProductData private val _productInitial: Product
) : ViewModel() {

    private val _product = MutableLiveData<Product>()
    val product: LiveData<Product> get() = _product

    private val _productAmount = MutableLiveData<Int>(0)
    val productAmount: LiveData<Int> get() = _productAmount

    private val _productPrice = MutableLiveData<Float>()
    val productPrice: LiveData<Float> get() = _productPrice

    private val _productInCart = MutableLiveData<Boolean>(false)
    val productInCart: LiveData<Boolean> get() = _productInCart

    private val _sizeAdapterPositionSelected = MutableLiveData<Int>(-1)
    val sizeAdapterPositionSelected: LiveData<Int> get() = _sizeAdapterPositionSelected

    private val _colorAdapterPositionSelected = MutableLiveData<Int>(-1)
    val colorAdapterPositionSelected: LiveData<Int> get() = _colorAdapterPositionSelected

    private var productSize = ""
    private var productColor = -2

    fun setProductSize(selector: SelectorModel) {
        this.productSize = selector.textString
        updateProductIfExistingInCart()
    }

    fun setProductColor(selector: SelectorModel) {
        this.productColor = selector.backgroundUnSelected
        updateProductIfExistingInCart()
    }



    private val colorList
        get() = listOf(
            SelectorModel(
                backgroundSelected = Color.WHITE,
                backgroundUnSelected = Color.WHITE,
                strokeColorUnSelected = Color.GRAY,
                strokeWidthUnSelected = 1f
            ),
            SelectorModel(
                backgroundSelected = Color.BLACK,
                backgroundUnSelected = Color.BLACK,
                strokeWidthUnSelected = 0f
            ),
            SelectorModel(
                backgroundSelected = Color.parseColor("#222743"),
                backgroundUnSelected = Color.parseColor("#222743"),
                strokeWidthUnSelected = 0f
            ),
            SelectorModel(
                backgroundSelected = Color.parseColor("#1F75F7"),
                backgroundUnSelected = Color.parseColor("#1F75F7"),
                strokeWidthUnSelected = 0f
            ),
        )

    private val _productSizes = MutableLiveData<List<SelectorModel>>()
    val productSizes: LiveData<List<SelectorModel>> get() = _productSizes

    private val _productColors = MutableLiveData<List<SelectorModel>>()
    val productColors: LiveData<List<SelectorModel>> get() = _productColors



    private fun setProductPrice() {
        _productAmount.value?.let { amount ->
            _productPrice.value = amount.times(_productInitial.price)
        }

    }

    fun descProductAmount() {
        _productAmount.value?.let {
            if (it > 1) {
                _productAmount.value = it - 1
                setProductPrice()
                updateProductIfExistingInCart()
            }
        }
    }

    fun addProductAmount() {
        _productAmount.value = _productAmount.value?.plus(1)
        setProductPrice()
        updateProductIfExistingInCart()

    }


    fun productAddOrDeleteInCart() {

        viewModelScope.launch {
            val productCartFinal = ProductCart(
                id = _productInitial.id,
                title = _productInitial.title,
                subtitle = _productInitial.subtitle ?: "",
                description = _productInitial.description ?: "",
                price = _productInitial.price,
                amount = _productAmount.value!!,
                sizesAvailable = _productInitial.size,
                sizeSelected = productSize,
                color = productColor,
                image = _productInitial.image,
                categoryId = _productInitial.category
            )
            // Check If Existing
            if (checkProductInCart()) {
                // Yes Existing --> Now Delete
                deleteProductInCartUseCase(productCartFinal)
            } else {
                // Not Existing --> Then Add
                insertProductInCartUseCase(productCartFinal)
            }

            _productInCart.postValue(checkProductInCart())


        }

    }

    private fun updateProductIfExistingInCart(){
        viewModelScope.launch {
            if (checkProductInCart()){
                val productCartFinal = ProductCart(
                    id = _productInitial.id,
                    title = _productInitial.title,
                    subtitle = _productInitial.subtitle ?: "",
                    description = "description",
                    price = _productInitial.price,
                    amount = _productAmount.value!!,
                    sizesAvailable = _productInitial.size,
                    sizeSelected = productSize,
                    color = productColor,
                    image = _productInitial.image,
                    categoryId = _productInitial.category
                )
                deleteProductInCartUseCase(productCartFinal)
                insertProductInCartUseCase(productCartFinal)
            }
        }

    }

    private suspend fun checkProductInCart(): Boolean {
        return getProductByIdUseCase(_productInitial.id) != null
    }

    init {
        _productAmount.value = 1
        _product.value = _productInitial
        _productPrice.value = _productInitial.price
        _productInCart.value = false
        // temp
        val sizeList =  _productInitial.size.map {
            SelectorModel(
                textString = it,
                strokeColorUnSelected = Color.parseColor("#DDDDDD")
            )
        }

        _productSizes.value = sizeList.ifEmpty { emptyList() }
        _productColors.value = colorList

        viewModelScope.launch {
            if(checkProductInCart()){
                _productInCart.postValue(true)
                getProductByIdUseCase(_productInitial.id)?.let { product ->
                    println("getProductByIdUseCase")
                    println(product.toString())

                    val colorPosition = colorList.indexOfFirst {  it.backgroundSelected == product.color  }
                    _colorAdapterPositionSelected.postValue(colorPosition)

                    if(sizeList.isNotEmpty()){
                        val sizePosition = sizeList.indexOfFirst {  it.textString == product.sizeSelected  }
                        _sizeAdapterPositionSelected.postValue(sizePosition)

                        productSize = sizeList.getOrNull(sizePosition)?.textString ?: sizeList[0].textString
                        productColor = colorList.getOrNull(sizePosition)?.backgroundUnSelected ?: colorList[0].backgroundUnSelected

                    }

                    _productAmount.postValue(product.amount)
                    _productPrice.postValue(product.amount.times(_productInitial.price))

                }

            } else {
                _colorAdapterPositionSelected.postValue(0)
                _sizeAdapterPositionSelected.postValue(0)
                _productInCart.postValue(false)

            }



        }

    }
}