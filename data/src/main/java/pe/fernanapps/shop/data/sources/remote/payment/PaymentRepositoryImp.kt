package pe.fernanapps.shop.data.sources.remote.payment

import pe.fernanapps.shop.domain.repository.PaymentRepository

import com.stripe.android.Stripe
import com.stripe.android.confirmPaymentIntent
import com.stripe.android.model.Address
import com.stripe.android.model.ConfirmPaymentIntentParams
import com.stripe.android.model.MandateDataParams
import com.stripe.android.model.PaymentMethod
import com.stripe.android.model.PaymentMethodCreateParams
import com.stripe.android.model.PaymentMethodOptionsParams
import com.stripe.android.model.StripeIntent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.single
import org.json.JSONObject
import pe.fernanapps.shop.data.sources.local.LocalDataSource
import pe.fernanapps.shop.data.sources.local.user.UserPreference
import pe.fernanapps.shop.data.sources.remote.payment.api.BackendApi
import pe.fernanapps.shop.data.sources.remote.payment.model.CustomerApi
import pe.fernanapps.shop.domain.DataState
import pe.fernanapps.shop.domain.PaymentStatusCode
import pe.fernanapps.shop.domain.model.payment.Payment
import pe.fernanapps.shop.domain.model.user.Card
import pe.fernanapps.shop.model.OrderDetailsCollection
import pe.fernanapps.shop.model.OrderItemCollection
import pe.fernanapps.shop.model.PaymentCollection
import java.util.UUID
import javax.inject.Inject


class PaymentRepositoryImp @Inject constructor(
    private val stripe: Stripe,
    private val backendApi: BackendApi,
    private val userPreference: UserPreference,
    private val local: LocalDataSource,
    private val paymentService: PaymentService,
    ) :
    PaymentRepository {
    override suspend fun createPayment(card: Card): Flow<DataState<Payment>> =
        flow {
            try {
                emit(DataState.Loading)

                val user = userPreference.getUserFromPreferences()!!

                // Create CustomerApi For Stripe
                val responseCustomerApi = backendApi.createOrGetCustomer(CustomerApi(
                    id = "",
                    customerId = user.id,
                    name = user.name,
                    email = user.email
                )).body()!!
                println("CUSTOMER_RESPONSE :::: $responseCustomerApi")

                val customerIdOfStripe = responseCustomerApi.id

                val items = local.getAllProductsInCart()
                val priceTotal = items.sumOf { it.finalPrice().toDouble() }.toFloat()

                println("Price_Total :::::. $priceTotal")

                val currency = "usd"
                // [PRODUCT_ID-(M)(3x25.5)]
                val description = items.joinToString {
                    "[${it.id}-(${it.sizeSelected})(${it.amount}x${it.price})]"
                }

                val stripeCard = PaymentMethodCreateParams.Card.Builder()
                    .setNumber(card.number)
                    .setExpiryMonth(card.expiryMonth)
                    .setExpiryYear(card.expiryYear)
                    .setCvc(card.cvc)
                    .build()

                val stripeBillingDetails = PaymentMethod.BillingDetails.Builder()
                    .setAddress(
                        Address(
                            user.address.city,
                            user.address.country,
                            user.address.line1,
                            user.address.line2,
                            user.address.postalCode,
                            user.address.state
                        )
                    )
                    .setEmail(user.email)
                    .setPhone(user.phone)
                    .build()

                val paymentMethodCreateParams =
                    PaymentMethodCreateParams.create(
                        stripeCard,
                        stripeBillingDetails
                    )


                val shippingDetails = ConfirmPaymentIntentParams.Shipping(
                    address = Address.Builder()
                        .setCity(user.address.city)
                        .setCountry(user.address.country)
                        .setLine1(user.address.line1)
                        .setLine2(user.address.line2)
                        .setPostalCode(user.address.postalCode)
                        .setState(user.address.state)
                        .build(),
                    name = "Jenny Rosen",
                    carrier = "Fedex",
                    trackingNumber = "12345"
                )

                // Generate Description

                val stripeAccountId: String? = null
                val confirmPaymentIntentParams = createAndConfirmPaymentIntent(
                    country = user.address.country!!,
                    paymentMethodCreateParams = paymentMethodCreateParams,
                    shippingDetails = shippingDetails,
                    stripeAccountId = stripeAccountId,
                    currency = currency,
                    amount = priceTotal,
                    description = description,
                    customerId = customerIdOfStripe,
                    existingPaymentMethodId = card.id.ifEmpty { null }
                ).single()

                emit(DataState.Progress("One moment,\\nTransaction in progress…"))
                delay(1000)


                val confirmPaymentIntent = stripe.confirmPaymentIntent(confirmPaymentIntentParams)


                /**
                PaymentIntent(
                id=pi_3NGOPsK3fu4EtKg51O6EoL49,
                paymentMethodTypes=[card],
                amount=1099,
                canceledAt=0,
                cancellationReason=null,
                captureMethod=Automatic,
                clientSecret=pi_3NGOPsK3fu4EtKg51O6EoL49_secret_foDmrbbCEIes0fbMK09kjBs2l,
                confirmationMethod=Automatic,
                countryCode=null, created=1686152216,
                currency=usd,
                description=Example PaymentIntent,
                isLiveMode=false, paymentMethod=null,
                paymentMethodId=pm_1NGOPuK3fu4EtKg54dqSHQNz,
                receiptEmail=null, status=succeeded, setupFutureUsage=null,
                lastPaymentError=null,
                shipping=Shipping(
                address =   Address(
                city=San Francisco,
                country=US,
                line1=123 Market St,
                line2=#345,
                postalCode=94107,
                state=CA
                ),
                carrier=Fedex,
                name=Jenny Rosen,
                phone=null,
                trackingNumber=12345
                ),
                unactivatedPaymentMethods=[],
                linkFundingSources=[],
                nextActionData=null,
                paymentMethodOptionsJsonString=null
                )
                 */


                if (confirmPaymentIntent.status == StripeIntent.Status.Succeeded) {

                    val payment = Payment(
                        id = confirmPaymentIntent.id!!,
                        amount = stripeFormatToDollars(confirmPaymentIntent.amount!!),
                        paymentType = confirmPaymentIntent.paymentMethodTypes.joinToString(", "),
                        currency = currency,
                        status = PaymentStatusCode.Succeeded
                    )

                    emit(DataState.Success(payment))
                    delay(3500)
                    emit(DataState.Finished)


                } else {
                    emit(DataState.Error(Exception("Payment failed")))
                    delay(2000)
                }
            } catch (e: Exception) {
                emit(DataState.Error(Exception(e)))

            }

        }.catch {
            emit(DataState.Error(it))
        }

    private suspend fun createAndConfirmPaymentIntent(
        country: String,
        amount: Float,
        paymentMethodCreateParams: PaymentMethodCreateParams?,
        supportedPaymentMethods: String? = null,
        shippingDetails: ConfirmPaymentIntentParams.Shipping? = null,
        stripeAccountId: String? = null,
        existingPaymentMethodId: String? = null,
        mandateDataParams: MandateDataParams? = null,
        customerId: String? = null,
        setupFutureUsage: ConfirmPaymentIntentParams.SetupFutureUsage? = null,
        currency: String? = null,
        description: String? = null,
        paymentMethodOptions: PaymentMethodOptionsParams? = null,
        onPaymentIntentCreated: (String) -> Unit = {},
    ): Flow<ConfirmPaymentIntentParams> = flow {

        val checkingValue = (paymentMethodCreateParams ?: existingPaymentMethodId)
            ?: throw Exception(PaymentStatusCode.Failed.code)

        val json = createPaymentIntent(
            country = country,
            supportedPaymentMethods = supportedPaymentMethods,
            customerId = customerId,
            currency = currency,
            description = description,
            amount = amount
        )

        handleCreatePaymentIntentResponse(
            json,
            paymentMethodCreateParams,
            shippingDetails,
            stripeAccountId,
            existingPaymentMethodId,
            mandateDataParams,
            setupFutureUsage,
            paymentMethodOptions,
            onPaymentIntentCreated
        ).collect {
            emit(it)
        }

    }



    private fun centsToStripeFormat(value: Float): Long {
        return (value * 100).toLong()
    }

    private fun stripeFormatToDollars(value: Long): Float {
        return value / 100f
    }


    private suspend fun createPaymentIntent(
        country: String,
        customerId: String? = null,
        supportedPaymentMethods: String? = null,
        currency: String? = null,
        description: String? = null,
        amount: Float,
    ): JSONObject {

        val amountInCents = centsToStripeFormat(amount)
        val apiMethod = backendApi.createPaymentIntent(
            mapOf("country" to country)
                .plus(mapOf("amount" to amountInCents))
                .plus(
                    description?.let {
                        mapOf("description" to it)
                    }.orEmpty()
                )
                .plus(
                    customerId?.let {
                        mapOf("customer_id" to it)
                    }.orEmpty()
                ).plus(
                    supportedPaymentMethods?.let {
                        mapOf("supported_payment_methods" to it)
                    }.orEmpty()
                ).plus(
                    currency?.let {
                        mapOf("currency" to it)
                    }.orEmpty()
                )
                .toMutableMap()
        )
        return JSONObject(apiMethod.string())
    }


    fun handleCreatePaymentIntentResponse(
        responseData: JSONObject,
        params: PaymentMethodCreateParams?,
        shippingDetails: ConfirmPaymentIntentParams.Shipping?,
        stripeAccountId: String?,
        existingPaymentMethodId: String?,
        mandateDataParams: MandateDataParams?,
        setupFutureUsage: ConfirmPaymentIntentParams.SetupFutureUsage?,
        paymentMethodOptions: PaymentMethodOptionsParams?,
        onPaymentIntentCreated: (String) -> Unit = {},
    ) = flow<ConfirmPaymentIntentParams> {

        if (!responseData.has("secret")) {
            throw Exception(PaymentStatusCode.Failed.code)
        }

        val secret = responseData.getString("secret")

        onPaymentIntentCreated(secret)

        val confirmPaymentIntentParams = if (existingPaymentMethodId == null) {
            ConfirmPaymentIntentParams.createWithPaymentMethodCreateParams(
                paymentMethodCreateParams = requireNotNull(params),
                clientSecret = secret,
                shipping = shippingDetails,
                setupFutureUsage = setupFutureUsage,
                paymentMethodOptions = paymentMethodOptions,
            ).copy(
                mandateData = mandateDataParams,
                useStripeSdk = true
            )
        } else {
            ConfirmPaymentIntentParams.createWithPaymentMethodId(
                paymentMethodId = existingPaymentMethodId,
                clientSecret = secret,
                mandateData = mandateDataParams,
                setupFutureUsage = setupFutureUsage,
                paymentMethodOptions = paymentMethodOptions,
            ).copy(
                useStripeSdk = true
            )
        }
        emit(confirmPaymentIntentParams)
    }

    override suspend fun generateOrder(payment: Payment): Flow<DataState<Boolean>> = flow {
        emit(DataState.Loading)

        val items = local.getAllProductsInCart()
        val currentUser = userPreference.getUserFromPreferences()!!

        // First Upload Order Details for next get Id generate,
        val orderDetails = paymentService.uploadAndGetOrder(
            OrderDetailsCollection(
                // Temp
                id = UUID.randomUUID().toString(),
                userId = currentUser.id,
                paymentId = payment.id,
                total = payment.amount
            )
        )

        items.forEach { productCart ->
            // Upload Item Id
            paymentService.uploadOrderItem(
                OrderItemCollection(
                    // Temp
                    id = UUID.randomUUID().toString(),
                    productId = productCart.id,
                    orderId = orderDetails.id,
                    size =  productCart.sizeSelected ?: "",
                    quantity = productCart.amount,
                    subTotal = productCart.finalPrice().toLong()
                )
            )
        }



        paymentService.uploadPayment(
            PaymentCollection(
                id = payment.id!!,
                clientId = currentUser.id,
                orderId = orderDetails.id,
                amount = payment.amount!!,
                paymentType = payment.paymentType,
                currency = payment.currency,
            )
        )

        local.deleteAllProductsInCart()
        delay(2500)
        emit(DataState.Success(true))

    }.catch {
        emit(DataState.Error(it))
    }

    override suspend fun completePayment(paymentId: String): Flow<Result<Payment>> {
        return flow {
            try {
                // Realiza una llamada a tu servidor de pagos para obtener el estado del pago utilizando el ID del pago
                // Puedes utilizar bibliotecas como Retrofit para realizar la llamada al servidor
                val paymentStatusResponse = backendApi.completePayment(paymentId)

                if (paymentStatusResponse.isSuccessful) {
                    // La respuesta del servidor es exitosa, obtén el estado del pago desde la respuesta
                    val paymentStatus = paymentStatusResponse.body()!!
                    Result.success(paymentStatus)
                } else {
                    // La respuesta del servidor no es exitosa, devuelve un error con el mensaje de error del servidor
                    val errorMessage = paymentStatusResponse.message()
                    Result.failure(Exception(errorMessage))
                }
            } catch (e: Exception) {
                // Ocurrió un error al comunicarse con el servidor
                Result.failure(e)
            }
        }
    }

}






