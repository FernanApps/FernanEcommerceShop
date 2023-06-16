package pe.fernanapps.shop.ui.screens.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import pe.fernanapps.shop.domain.DataState
import pe.fernanapps.shop.domain.usecases.login.CheckSessionAvailabilityUseCase
import pe.fernanapps.shop.domain.usecases.login.LoginUseCase
import pe.fernanapps.shop.domain.usecases.logout.LogOutUseCase
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val checkSessionAvailabilityUseCase: CheckSessionAvailabilityUseCase,
    private val logOutUseCase: LogOutUseCase
): ViewModel() {

    private val _loginState: MutableLiveData<DataState<Boolean>> = MutableLiveData()
    val loginState: LiveData<DataState<Boolean>>
        get() = _loginState

    private val _isSessionAvailable: MutableLiveData<DataState<Boolean>> = MutableLiveData()
    val isSessionAvailable: LiveData<DataState<Boolean>>
        get() = _isSessionAvailable

    private val _logOutState: MutableLiveData<DataState<Boolean>> = MutableLiveData()
    val logOutState: LiveData<DataState<Boolean>>
        get() = _logOutState

    fun login(email: String, password: String){
        viewModelScope.launch {
            loginUseCase(email, password)
                .onEach { dataState ->
                    _loginState.value = dataState
                }.launchIn(viewModelScope)
        }
    }

    fun checkSessionAvailability(){
        viewModelScope.launch {
            checkSessionAvailabilityUseCase()
                .onEach { dataState ->
                    _isSessionAvailable.value = dataState
                }.launchIn(viewModelScope)
//            checkSessionAvailabilityUseCase().collect {dataState ->
//                _isSessionAvailable.value = dataState
//            }
        }
    }

    fun logOut(){
        viewModelScope.launch {
            logOutUseCase()
                .onEach { dataState ->
                    _logOutState.value = dataState
                }.launchIn(viewModelScope)
        }
    }
}