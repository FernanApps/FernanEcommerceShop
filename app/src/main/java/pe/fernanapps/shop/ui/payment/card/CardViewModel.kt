package pe.fernanapps.shop.ui.payment.card

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pe.fernanapps.shop.domain.DataState
import pe.fernanapps.shop.domain.model.user.Card
import pe.fernanapps.shop.domain.usecases.payment.SaveCardUseCase
import javax.inject.Inject

@HiltViewModel
class CardViewModel @Inject constructor(
    private val saveCardUseCase: SaveCardUseCase,
) : ViewModel() {
    private val _saveCard = MutableLiveData<DataState<Boolean>>()
    val saveCard: LiveData<DataState<Boolean>> get() = _saveCard

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    fun saveCard(cardNumber: String, cardExpDate: String, cardCVC: String) {
        viewModelScope.launch {


            /*

            data class Card constructor(
                val number: String? = null,
                val expiryMonth: Int? = null,
                val expiryYear: Int? = null,
                val cvc: String? = null,
                val brand: String = "",
                val id: String = ""
            )
             */
            if (cardNumber.isEmpty()) {
                _message.value = "Insert number card"
                return@launch
            }



            if (!cardExpDate.contains("/")) {
                _message.value = "Insert correct exp date"
                return@launch
            }

            val cutting = cardExpDate.split("/").map {
                it.replace(" ", "")
            }
            if (cutting.size != 2) {
                _message.value = "Insert correct exp date"
                return@launch
            }

            if (cardCVC.isEmpty()) {
                _message.value = "Insert correct CVC"
            }

            val finalCardNumber = cardNumber.replace(" ", "")
            val (expiryMonth, expiryYear) = cutting


            val card = Card(
                id = "",
                number = finalCardNumber,
                expiryMonth = expiryMonth.toInt(),
                expiryYear = expiryYear.toInt(),
                cvc = cardCVC
            )
            saveCardUseCase(card).collect {
                _saveCard.postValue(it)
            }

        }
    }

}