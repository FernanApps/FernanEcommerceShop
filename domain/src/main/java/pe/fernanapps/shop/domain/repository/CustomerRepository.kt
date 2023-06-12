package pe.fernanapps.shop.domain.repository

import kotlinx.coroutines.flow.Flow
import pe.fernanapps.shop.domain.DataState
import pe.fernanapps.shop.domain.model.user.Card
import pe.fernanapps.shop.domain.model.user.User

interface CustomerRepository {
    //suspend fun createCustomer(customerId: String, user: User): Customer
    suspend fun getCustomerCards(): Flow<DataState<List<Card>>>
    suspend fun saveCard(card: Card): Flow<DataState<Boolean>>
    //suspend fun createEphemeralKey(apiVersionMap: HashMap<String, String>): ResponseBody
}