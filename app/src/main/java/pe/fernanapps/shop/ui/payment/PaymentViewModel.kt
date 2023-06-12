package pe.fernanapps.shop.ui.payment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pe.fernanapps.shop.domain.DataState
import pe.fernanapps.shop.domain.model.payment.Payment
import pe.fernanapps.shop.domain.model.user.Card
import pe.fernanapps.shop.domain.usecases.payment.GenerateOrderUseCase
import pe.fernanapps.shop.domain.usecases.payment.GetCustomerCardsUseCase
import pe.fernanapps.shop.domain.usecases.payment.ProcessPaymentUseCase
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val getAllCardsUseCase: GetCustomerCardsUseCase,
    private val processPaymentUseCase: ProcessPaymentUseCase,
    private val generateOrderUseCase: GenerateOrderUseCase
) : ViewModel() {

    private val _cards = MutableLiveData< DataState<List<Card>>>()
    val cards: LiveData<DataState<List<Card>>> get() = _cards


    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    private val _finishPayment = MutableLiveData<DataState<Boolean>>()
    val finishPayment: LiveData<DataState<Boolean>> get() = _finishPayment

    private val _paymentResult = MutableLiveData<DataState<Payment>>()
    val paymentResult: LiveData<DataState<Payment>> = _paymentResult

    private val _showBackCard = MutableLiveData<Boolean>()
    val showBackCard: LiveData<Boolean> get() = _showBackCard



    fun generateOrder(payment: Payment) {
        viewModelScope.launch {
            generateOrderUseCase(payment).collect {
                _finishPayment.value = it
            }

        }
    }


    fun processPayment(card: Card) {
        viewModelScope.launch {
            processPaymentUseCase(card).collect {
                _paymentResult.value = it
            }

        }
    }

//    fun evaluateAndPayment(card: Card) {
//        with(card) {
//            if (number.isNullOrBlank()) {
//                _message.value = "number is Empty"
//            } else if (expiryMonth == null) {
//                _message.value = "expiryMonth is Empty"
//            } else if (expiryYear == null) {
//                _message.value = "expiryYear is Empty"
//            } else if (cvc.isNullOrBlank()) {
//                _showBackCard.value = true
//                _message.value = "cvc is Empty"
//            } else {
//                processPayment(card)
//            }
//        }
//
//    }

    fun getCards(){
        viewModelScope.launch {
            getAllCardsUseCase().collect {
                _cards.value = it
            }
        }
    }

    init {
        getCards()
    }


}