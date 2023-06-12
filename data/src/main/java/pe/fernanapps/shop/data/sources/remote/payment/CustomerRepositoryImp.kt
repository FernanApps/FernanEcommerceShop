package pe.fernanapps.shop.data.sources.remote.payment


import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import pe.fernanapps.shop.data.sources.local.user.UserPreference
import pe.fernanapps.shop.data.sources.remote.payment.api.BackendApi
import pe.fernanapps.shop.data.sources.remote.payment.model.CustomerApi
import pe.fernanapps.shop.data.sources.remote.payment.model.CustomerWithCardTokenApi
import pe.fernanapps.shop.data.sources.remote.payment.model.toApi
import pe.fernanapps.shop.domain.DataState
import pe.fernanapps.shop.domain.model.user.Card
import pe.fernanapps.shop.domain.repository.CustomerRepository
import javax.inject.Inject

class CustomerRepositoryImp @Inject constructor(
    private val userPreference: UserPreference,
    private val backendApi: BackendApi,
) : CustomerRepository {
    override suspend fun getCustomerCards() = flow<DataState<List<Card>>> {
        try {
            emit(DataState.Loading)

            val user = userPreference.getUserFromPreferences()!!
            val responseCustomerApi = backendApi.checkCustomer(
                CustomerApi(
                    id = "",
                    customerId = user.id,
                    name = user.name,
                    email = user.email
                )
            ).body()!!

            val customerId = responseCustomerApi.customerId

            val response = backendApi.getCustomerCards(
                CustomerApi(customerId = customerId)
            )
            if (response.isSuccessful && response.code() == 200) {
                val cardApiList = response.body()
                if (cardApiList?.isNotEmpty() == true) {
                    emit(DataState.Success(cardApiList.map { it.toDomain() }))
                }
            }

            delay(1500)
            emit(DataState.Finished)


        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }.catch { e ->
        emit(DataState.Error(e))
    }

    override suspend fun saveCard(card: Card) =
        flow<DataState<Boolean>> {
            try {
                emit(DataState.Loading)

                val user = userPreference.getUserFromPreferences()!!
                val responseCustomerApi = backendApi.createOrGetCustomer(
                    CustomerApi(
                        id = "",
                        customerId = user.id,
                        name = user.name,
                        email = user.email
                    )
                ).body()!!

                val customerId = responseCustomerApi.id

                emit(DataState.Loading)
                // Create Card With Token :::
                val responseToken = backendApi.createCardToken(card.toApi())


                println("respondeToken ${responseToken.raw()}")
                println("respondeToken ${responseToken.code()}")

                if (responseToken.isSuccessful && responseToken.code() == 200) {

                    val cardToken = responseToken.body()!!.token!!
                    val response =
                        backendApi.saveCard(
                            CustomerWithCardTokenApi(
                                cardToken = cardToken,
                                customerId = customerId
                            )
                        )

                    emit(DataState.Success(response.isSuccessful && response.code() == 200))
                } else {
                    emit(DataState.Success(false))
                }


                delay(1500)
                emit(DataState.Finished)

            } catch (e: Exception) {
                emit(DataState.Error(e))
            }
        }.catch { e ->
            emit(DataState.Error(e))

        }

}