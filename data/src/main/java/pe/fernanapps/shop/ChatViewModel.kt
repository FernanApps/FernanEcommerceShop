package pe.fernanapps.shop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pe.fernanapps.shop.domain.DataState
import pe.fernanapps.shop.domain.model.chat.Message
import pe.fernanapps.shop.domain.usecases.chat.GetChatsUseCase
import pe.fernanapps.shop.domain.usecases.chat.SendMessageUseCase
import pe.fernanapps.shop.domain.usecases.chat.SubscribeToMessagesUseCase
import pe.fernanapps.shop.domain.usecases.chat.UnsubscribeFromMessagesUseCase
import pe.fernanapps.shop.domain.usecases.user.GetUserLocalUseCase
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getUserLocalUseCase: GetUserLocalUseCase,
    private val getChatsUseCase: GetChatsUseCase,
    private val subscribeToMessagesUseCase: SubscribeToMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val unsubscribeFromMessagesUseCase: UnsubscribeFromMessagesUseCase,

    ) : ViewModel() {


    private val _chats = MutableLiveData<DataState<List<Message>>>()
    val chats: LiveData<DataState<List<Message>>> get() = _chats

    private val _userIdStatus = MutableLiveData<DataState<String>>()
    val userIdStatus: LiveData<DataState<String>> get() = _userIdStatus

    private var userId: String? = null

    private val _messageList = MutableLiveData<List<Message>>()
    val messageList: LiveData<List<Message>> get() = _messageList

    fun setUserId(userId: String) {
        this.userId = userId
    }

    fun getChats(){
        viewModelScope.launch {
            getChatsUseCase(userId!!).collect(){
                _chats.value = it
            }
        }
    }

    fun setChats(chats: List<Message>){
        val currentList = chats
        _messageList.value = currentList
    }


    fun subscribeToMessages() {
        viewModelScope.launch {
            subscribeToMessagesUseCase(userId!!).collect { newMessage ->
                val currentList = _messageList.value.orEmpty().toMutableList()
                currentList.add(newMessage)
                _messageList.value = currentList
            }
        }
    }

    fun sendMessage(message: String) {
        println("sending message $message --- $userId")
        viewModelScope.launch(Dispatchers.IO) {
            sendMessageUseCase(
                Message(
                    senderId = userId!!,
                    text = message,
                    chatId = userId!!
                )
            )

        }
    }

    override fun onCleared() {
        unsubscribeFromMessagesUseCase()
    }

    init {
        viewModelScope.launch {
            _userIdStatus.value = DataState.Loading
            val user = getUserLocalUseCase.invoke()
            user!!
            setUserId(user.id)
            _userIdStatus.value = DataState.Success(user.id)
            delay(1000)
            _userIdStatus.value = DataState.Finished
        }



//        messageService.messageReceived.observeForever { newMessage ->
//
//        }
    }

}








