package pe.fernanapps.shop.domain.repository

import kotlinx.coroutines.flow.Flow
import pe.fernanapps.shop.domain.DataState
import pe.fernanapps.shop.domain.model.user.User

interface LoginRepository {

    fun login(email: String, password: String): Flow<DataState<Boolean>>

    fun signUp(user: User, password: String): Flow<DataState<User>>

    fun logOut(): Flow<DataState<Boolean>>

    fun getUserData(): Flow<DataState<Boolean>>

    fun saveUser(user: User): Flow<DataState<Boolean>>
}