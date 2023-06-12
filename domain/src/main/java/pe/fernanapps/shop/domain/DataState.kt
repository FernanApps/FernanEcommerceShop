package pe.fernanapps.shop.domain



sealed class SimpleState <out R>{
    data class Success<out T>(val data: T): SimpleState<T>()
    data class Error<out T>(val message: T): SimpleState<T>()
}

sealed class DataState <out R>{
    data class Success<out T>(val data: T): DataState<T>()
    data class Progress(val status: String): DataState<Nothing>(){}
    data class Error(val exception: Throwable): DataState<Nothing>()
    object Loading : DataState<Nothing>()
    object Finished: DataState<Nothing>()
}

sealed class NetworkResult<T : Any> {
    class Success<T: Any>(val data: T) : NetworkResult<T>()
    class Failure<T: Any>(val code: Int, val message: String?) : NetworkResult<T>()
    class Exception<T: Any>(val e: Throwable) : NetworkResult<T>()
}

sealed class LoginError : Exception() {
    object AuthInvalidUserException : LoginError()
    object AuthWeakPasswordException : LoginError()
    object AuthInvalidCredentialsException : LoginError()
    object AuthUserCollisionException : LoginError()
    object AuthRecentLoginRequiredException : LoginError()
    object AuthActionCodeException : LoginError()
}


enum class PaymentStatusCode(val code: String) {
    Canceled("canceled"),
    Processing("processing"),
    //RequiresAction("requires_action"),
    //RequiresConfirmation("requires_confirmation"),
    //RequiresPaymentMethod("requires_payment_method"),
    Failed("failed"),
    Succeeded("succeeded");

    // only applies to Payment Intents
    //RequiresCapture("requires_capture");

    override fun toString(): String {
        return code
    }

    internal companion object {
        internal fun fromCode(code: String?): PaymentStatusCode? {
            return PaymentStatusCode.values().firstOrNull { it.code == code }
        }
    }
}