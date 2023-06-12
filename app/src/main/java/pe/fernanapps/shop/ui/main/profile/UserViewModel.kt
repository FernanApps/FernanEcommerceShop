package pe.fernanapps.shop.ui.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pe.fernanapps.shop.domain.Constants
import pe.fernanapps.shop.domain.DataState
import pe.fernanapps.shop.domain.SimpleState
import pe.fernanapps.shop.domain.model.user.User
import pe.fernanapps.shop.domain.usecases.user.GetUserLocalUseCase
import pe.fernanapps.shop.domain.usecases.user.SaveUserLocalUseCase
import pe.fernanapps.shop.domain.usecases.user.SaveUserRemoteUseCase
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val getUserLocalUseCase: GetUserLocalUseCase,
    private val saveUserRemoteUseCase: SaveUserRemoteUseCase,
    private val saveUserLocalUseCase: SaveUserLocalUseCase,
) : ViewModel() {

    private val _userIdStatus = MutableLiveData<DataState<User>>()
    val userIdStatus: LiveData<DataState<User>> get() = _userIdStatus

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> get() = _userName

    private val _addressStatus = MutableLiveData<SimpleState<String>>()
    val addressStatus: LiveData<SimpleState<String>> get() = _addressStatus

    private val _saveUserRemoteStatus = MutableLiveData<DataState<Boolean>>()
    val saveUserRemoteStatus: LiveData<DataState<Boolean>> get() = _saveUserRemoteStatus


    private var userId: String? = null
    fun setUserId(userId: String) {
        this.userId = userId
    }

    fun saveUserInRemote() {
        viewModelScope.launch {
            if (user != null) {
                saveUserRemoteUseCase(user!!).collect {
                    _saveUserRemoteStatus.value = it
                }
            }

        }
    }

    fun saveUserInLocal() {
        viewModelScope.launch {
            if (user != null) {
                saveUserLocalUseCase(user!!)
            }
        }
    }

    fun checkUserAddressAndPhone(): Pair<Boolean, String> {
        val address = user?.address
        return if (address == null) {
            false to "Not Found Address"
        } else if (address.city == null) {
            false to "Not Found City"

        } else if (address.line1 == null) {
            false to "Not Found line1"

        } else if (address.line2 == null) {
            false to "Not Found line2"

        } else if (address.postalCode == null) {
            false to "Not Found postalCode"

        } else if (address.state == null) {
            false to "Not Found state"

        } else if (address.country == null) {
            false to "Not Found country"

        } else if (user?.phone == Constants.INFO_NOT_SET) {
            false to "Not Found Phone"

        } else {
            true to "Okay"
        }
    }

    fun checkAddressAndPhone() {
        val (result, status) = checkUserAddressAndPhone()
        if(!result){
            _addressStatus.value = SimpleState.Error(status)
        } else {
            val address = user?.address!!
            val addressText =
                "${address.line1}, ${address.line2}\n${address.city}, ${address.state}, ${address.postalCode}\n${user?.phone}"
            _addressStatus.value = SimpleState.Success(addressText)
        }

    }

    var user: User? = null


    fun setName(text: String) {
        user = user?.copy(name = text)
    }

    fun setPhone(text: String) {
        user = user?.copy(phone = text)
    }

    fun setCity(text: String) {
        val address = user?.address?.copy(city = text)
        user = address?.let { user?.copy(address = it) }
    }

    fun setCountry(text: String) {
        val address = user?.address?.copy(country = text)
        user = address?.let { user?.copy(address = it) }
    }

    fun setLine1(text: String) {
        val address = user?.address?.copy(line1 = text)
        user = address?.let { user?.copy(address = it) }
    }

    fun setLine2(text: String) {
        val address = user?.address?.copy(line2 = text)
        user = address?.let { user?.copy(address = it) }
    }

    fun setPostalCode(text: String) {
        val address = user?.address?.copy(postalCode = text)
        user = address?.let { user?.copy(address = it) }
    }

    fun setState(text: String) {
        val address = user?.address?.copy(state = text)
        user = address?.let { user?.copy(address = it) }
    }

    fun getUser(){
        viewModelScope.launch {
            _userIdStatus.value = DataState.Loading
            user = getUserLocalUseCase.invoke()
            user!!
            setUserId(user!!.id)
            _userIdStatus.value = DataState.Success(user!!)
            delay(1000)
            _userIdStatus.value = DataState.Finished

            _userName.value = user!!.name
            checkAddressAndPhone()
        }

    }


    init {
        getUser()
    }
}