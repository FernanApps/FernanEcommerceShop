package pe.fernanapps.shop.ui.main.cart

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class IntervalColorViewModel @Inject constructor() : ViewModel() {

    private val _currentColor = MutableLiveData<Int>()
    val currentColor: LiveData<Int> get() = _currentColor

    private var currentColorIndex = 0
    private var colorJob: Job? = null

    fun startColorChange(timeMillis: Long = 1500) {
        colorJob?.cancel() // Cancelar el trabajo anterior si existe
        colorJob = CoroutineScope(Dispatchers.Main).launch {
            while (isActive) {
                val color = getColorFromIndex(currentColorIndex)
                _currentColor.value = color
                delay(timeMillis)
                currentColorIndex = (currentColorIndex + 1) % 2
            }
        }
    }

    fun stopColorChange() {
        colorJob?.cancel()
    }

    private var firstColor: Int? = null
    private var secondColor: Int? = null

    fun setFirstColor(color: Int) {
        firstColor = color
    }

    fun setSecondColor(color: Int) {
        secondColor = color
    }

    private fun getColorFromIndex(index: Int): Int {
        return if (index == 0) {
            firstColor ?: Color.RED
        } else {
            secondColor ?: Color.GREEN
        }
    }

    override fun onCleared() {
        super.onCleared()
        colorJob?.cancel()
    }
}
