package pe.fernanapps.shop.ui.main.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pe.fernanapps.shop.domain.DataState
import pe.fernanapps.shop.domain.model.product.Category
import pe.fernanapps.shop.domain.usecases.home.GetCategoriesUseCase
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    private val _categories = MutableLiveData<DataState<List<Category>>>()
    val categories: LiveData<DataState<List<Category>>> get() = _categories


    private val _categoriesOriginal = MutableLiveData<List<Category>>()

    private val _categoriesFiltered = MutableLiveData<List<Category>>()
    val categoriesFiltered: LiveData<List<Category>> get() = _categoriesFiltered

    fun setOriginalCategories(categories: List<Category>) {
        _categoriesOriginal.value = categories
    }

    fun getCategories() {

        viewModelScope.launch {
            getCategoriesUseCase().collect { result ->
                _categories.value = result
            }
//            loginUseCase(email, password)
//                .onEach { dataState ->
//                    _loginState.value = dataState
//                }.launchIn(viewModelScope)
        }
    }

    fun filterCategories(query: String) {
        _categoriesOriginal.value?.filter { it.title.contains(query, true) }?.let { filteredList ->
            _categoriesFiltered.value = filteredList
        }
    }

}