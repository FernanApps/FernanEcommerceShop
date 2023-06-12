package pe.fernanapps.shop.domain.repository

import kotlinx.coroutines.flow.Flow
import pe.fernanapps.shop.domain.DataState
import pe.fernanapps.shop.domain.model.user.User
import javax.xml.crypto.Data

interface UserLocalRepository {
    fun getUserFromPreferences(): User?
    fun saveUserToPreferences(user: User)
    fun clearUserData()
}

interface UserRemoteRepository {
    suspend fun getUser(userId: String): Flow<DataState<User?>>
    suspend fun saveUser(user: User): Flow<DataState<Boolean>>
    suspend fun clearUser(userId: String)
}



