package pe.fernanapps.shop.data.sources.remote.payment.api


import okhttp3.ResponseBody
import pe.fernanapps.shop.data.sources.remote.payment.model.CardApi
import pe.fernanapps.shop.data.sources.remote.payment.model.CardTokenApi
import pe.fernanapps.shop.data.sources.remote.payment.model.CustomerApi
import pe.fernanapps.shop.data.sources.remote.payment.model.CustomerWithCardTokenApi
import pe.fernanapps.shop.domain.model.payment.Payment
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface BackendApi {

    @FormUrlEncoded
    @POST("ephemeral_keys")
    suspend fun createEphemeralKey(@FieldMap apiVersionMap: HashMap<String, String>): ResponseBody

    @FormUrlEncoded
    @POST("create_payment_intent")
    suspend fun createPaymentIntent(@FieldMap params: MutableMap<String, Any>): ResponseBody

    @FormUrlEncoded
    @POST("create_setup_intent")
    suspend fun createSetupIntent(@FieldMap params: MutableMap<String, String>): ResponseBody


    @GET("payments/{paymentId}")
    suspend fun completePayment(@Path("paymentId") paymentId: String): Response<Payment>

    @POST("customer")
    suspend fun createOrGetCustomer(@Body customerApi: CustomerApi): Response<CustomerApi>

    @POST("customer/exist")
    suspend fun checkCustomer(@Body customerApi: CustomerApi): Response<CustomerApi>

    @POST("customer/cards/list")
    suspend fun getCustomerCards(@Body request: CustomerApi): Response<List<CardApi>>

    @POST("card_token")
    suspend fun createCardToken(@Body request: CardApi): Response<CardTokenApi>

    @POST("customer/cards")
    suspend fun saveCard(@Body request: CustomerWithCardTokenApi): Response<ResponseBody>

}
