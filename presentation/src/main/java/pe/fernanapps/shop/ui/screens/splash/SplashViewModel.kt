package pe.fernanapps.shop.ui.screens.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import pe.fernanapps.shop.domain.DataState
import pe.fernanapps.shop.domain.usecases.login.CheckSessionAvailabilityUseCase
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val checkSessionAvailabilityUseCase: CheckSessionAvailabilityUseCase,
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>(true)
    val loading: LiveData<Boolean> = _loading

    private val _isSessionAvailable: MutableLiveData<DataState<Boolean>> = MutableLiveData()
    val isSessionAvailable: LiveData<DataState<Boolean>>
        get() = _isSessionAvailable


    var timeDefault = 3500L

    fun initSplashScreen() {
        viewModelScope.launch {
            delay(timeDefault)
            finishLoading()
        }
    }

    private fun finishLoading() {
        _loading.postValue(false)
    }


    fun checkSessionAvailability() {
        viewModelScope.launch {
            checkSessionAvailabilityUseCase().collect { dataState ->
                _isSessionAvailable.value = dataState
            }
        }
    }


}