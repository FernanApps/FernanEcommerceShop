package pe.fernanapps.shop.data.sources.remote.user

import io.appwrite.extensions.toJson
import io.appwrite.services.Databases
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import pe.fernanapps.shop.ConstantsData
import pe.fernanapps.shop.data.sources.remote.AppWriteService
import pe.fernanapps.shop.domain.DataState
import pe.fernanapps.shop.domain.model.user.User
import pe.fernanapps.shop.domain.repository.UserRemoteRepository
import pe.fernanapps.shop.model.UserCollection
import pe.fernanapps.shop.model.toCollection
import pe.fernanapps.shop.model.toDomain
import pe.fernanapps.shop.utils.toModel
import javax.inject.Inject



class UserRemoteRepositoryImp @Inject constructor(
    private val userService: UserService,
) : UserRemoteRepository {

    override suspend fun getUser(userId: String) = flow<DataState<User?>> {
        emit(DataState.Loading)
        val user = userService.getUser(userId)!!.toDomain()
        delay(1000)
        emit(DataState.Success(user))
        delay(1500)
        emit(DataState.Finished)

    }.catch {
        emit(DataState.Error(it))
    }

    override suspend fun saveUser(user: User) = flow {
        try {
            emit(DataState.Loading)
            var checkUser: UserCollection? = null
            try {
                checkUser = userService.getUser(user.id)
            } catch (e: Exception) {

            }
            if (checkUser != null) {
                userService.updateUser(user.toCollection())
            } else {
                userService.saveUser(user.toCollection())
            }

            delay(1500)
            emit(DataState.Success(true))
            delay(1500)
            emit(DataState.Finished)

        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }.catch {
        emit(DataState.Error(it))
    }

    override suspend fun clearUser(userId: String) {
        userService.clearUser(userId)
    }

}