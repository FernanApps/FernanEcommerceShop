package pe.fernanapps.shop.ui.signUp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import pe.fernanapps.shop.domain.DataState
import pe.fernanapps.shop.domain.model.user.User
import pe.fernanapps.shop.domain.usecases.signup.SaveUserUseCase
import pe.fernanapps.shop.domain.usecases.signup.SignUpUseCase
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val saveUserUseCase: SaveUserUseCase
): ViewModel() {
    private val _signUpState: MutableLiveData<DataState<User>> = MutableLiveData()
    val signUpState : LiveData<DataState<User>>
        get() =_signUpState

    private val _saveUserState: MutableLiveData<DataState<Boolean>> = MutableLiveData()
    val saveUserState : LiveData<DataState<Boolean>>
        get() =_saveUserState

    fun signUp(user : User, password : String){
        viewModelScope.launch {
            signUpUseCase(user, password).collect{ dataState ->
                _signUpState.value = dataState
            }
//            signUpUseCase(user, password)
//                .onEach { dataState ->
//
//                }.launchIn(viewModelScope)
        }
    }

    fun saveUser(user: User){
        viewModelScope.launch {
            saveUserUseCase(user).collect{ dataState ->
                _saveUserState.value = dataState
            }
        }
    }
}