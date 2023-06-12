package pe.fernanapps.shop.ui.main.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pe.fernanapps.shop.domain.DataState
import pe.fernanapps.shop.domain.model.notifications.Notification
import pe.fernanapps.shop.domain.usecases.notifcations.GetAllNotificationsLocalUseCase
import pe.fernanapps.shop.domain.usecases.notifcations.GetNotificationsRemoteUseCase
import pe.fernanapps.shop.domain.usecases.notifcations.SaveAllNotificationsLocalUseCase
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val getNotificationsRemoteUseCase: GetNotificationsRemoteUseCase,
    private val saveAllNotificationsLocalUseCase: SaveAllNotificationsLocalUseCase,
    private val getAllNotificationsLocalUseCase: GetAllNotificationsLocalUseCase

    ): ViewModel() {

    private val _notifications = MutableLiveData<List<Notification>>()
    val notifications: LiveData<List<Notification>> get() = _notifications

    private val _notificationsStatus = MutableLiveData<DataState<List<Notification>>>()
    val notificationsStatus: LiveData<DataState<List<Notification>>> get() = _notificationsStatus

    fun getAllNotifications(){
        viewModelScope.launch {
            getNotificationsRemoteUseCase().collect {
                _notificationsStatus.value = it
            }
        }
    }

    fun saveInLocal(list: List<Notification>){
        viewModelScope.launch {
            saveAllNotificationsLocalUseCase(list)
            getAllLocalNotifications()
        }

    }

    fun getAllLocalNotifications() {
        viewModelScope.launch {
            getAllNotificationsLocalUseCase().collect{
                _notifications.value = it
            }
        }
    }

    init {
        getAllNotifications()
    }

}