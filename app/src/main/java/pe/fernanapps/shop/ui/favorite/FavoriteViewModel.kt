package pe.fernanapps.shop.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pe.fernanapps.shop.domain.model.product.Product
import pe.fernanapps.shop.domain.usecases.favorite.DeleteProductFavoriteUseCase
import pe.fernanapps.shop.domain.usecases.favorite.GetAllProductsFavoriteUseCase
import pe.fernanapps.shop.domain.usecases.favorite.SaveProductFavoriteUseCase
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val saveProductFavoriteUseCase: SaveProductFavoriteUseCase,
    private val deleteProductFavoriteUseCase: DeleteProductFavoriteUseCase,
    private val getAllProductsFavoriteUseCase: GetAllProductsFavoriteUseCase,
    private val deletAllProductsFavoriteUseCase: GetAllProductsFavoriteUseCase
) : ViewModel() {

    private val _productsWithFavorites = MutableLiveData<List<Product>>()
    val productsWithFavorites: LiveData<List<Product>> get() = _productsWithFavorites



    fun insert(product: Product) {
        viewModelScope.launch {
            saveProductFavoriteUseCase(product)
            getAllFavoritesAndUpdateListWithFavorites()
        }
    }

    fun delete(product: Product){
        viewModelScope.launch {
            deleteProductFavoriteUseCase(product)
            getAllFavoritesAndUpdateListWithFavorites()
        }
    }

    fun deleteAll(){
        viewModelScope.launch {
            deletAllProductsFavoriteUseCase()
        }
    }


    private var currentList: List<Product> = emptyList()
    fun setCurrentList(currentList: List<Product>){
        if(currentList.isNotEmpty()){
            this.currentList = currentList

        }
    }

    fun getAllFavoritesAndUpdateListWithFavorites(){
        viewModelScope.launch {
            val productFavorites = getAllProductsFavoriteUseCase()

            val currentProductMap: Map<String, Product> = currentList.associateBy { it.id }
            val productWithFavoriteMap: Map<String, Product> = currentProductMap + productFavorites.associateBy { it.id }

            _productsWithFavorites.postValue(ArrayList(productWithFavoriteMap.values))
        }
    }



}