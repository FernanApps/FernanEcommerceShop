package pe.fernanapps.shop.data.sources.remote.login


import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import pe.fernanapps.shop.ConstantsData
import pe.fernanapps.shop.data.sources.remote.user.UserService
import pe.fernanapps.shop.domain.DataState
import pe.fernanapps.shop.domain.LoginError
import pe.fernanapps.shop.domain.model.user.User
import pe.fernanapps.shop.domain.repository.LoginRepository
import pe.fernanapps.shop.model.toDomain
import pe.fernanapps.shop.utils.PrefManager
import pe.fernanapps.shop.utils.toJson
import javax.inject.Inject


private fun getTypeErrorAppWrite(e: Exception): Exception {
    return when (e) {
        is io.appwrite.exceptions.AppwriteException ->
            when (e.code) {
                401 -> LoginError.AuthInvalidCredentialsException
                404 -> LoginError.AuthActionCodeException
                else -> e
            }

        else -> e
    }
}


class LoginRepositoryImp @Inject constructor(
    private val client: io.appwrite.Client,
    private val pref: PrefManager,
    private val userService: UserService,
) : LoginRepository {


    override fun login(email: String, password: String) = flow {
        emit(DataState.Loading)
        try {
            var isSuccessful: Boolean = false

            val account = io.appwrite.services.Account(client)

            val session = account.createEmailSession(
                email = email,
                password = password
            )

            // Save
            pref.putValue(ConstantsData.SESSION_ID, session.id)

            val finalAccount = account.get()
            saveUser(finalAccount)
            isSuccessful = finalAccount.status

            delay(3500)
            emit(DataState.Success(isSuccessful))

        } catch (e: Exception) {
            emit(DataState.Error(getTypeErrorAppWrite(e)))
        }
        emit(DataState.Finished)
    }


    override fun signUp(user: User, password: String) = flow {
        emit(DataState.Loading)
        try {
            // response User ::::::


            val account = io.appwrite.services.Account(client)

            val userId = io.appwrite.ID.unique()

            val response = account.create(
                userId = userId,
                name = user.name,
                email = user.email,
                password = password,
            )



            delay(3500)
            if (response.status) {
                saveUser(response)

                val responseUser = User(
                    id = response.id,
                    email = response.email,
                    name = response.name
                )
                emit(DataState.Success(responseUser))
            } else {
                emit(DataState.Error(Exception("Not Created User")))
            }

        } catch (e: Exception) {
            println("Error")
            e.printStackTrace()
            emit(DataState.Error(getTypeErrorAppWrite(e)))
        }
        emit(DataState.Finished)
    }

    override fun logOut(): Flow<DataState<Boolean>> = flow {
        emit(DataState.Loading)
        try {
            val account = io.appwrite.services.Account(client)
            val sessionId = pref.getValue<String>(ConstantsData.SESSION_ID, "")

            // ResetValue
            pref.remove(ConstantsData.SESSION_ID)

            if (sessionId.isEmpty()) {
                emit(DataState.Error(Exception("Session Id is Empty")))
            } else {
                account.deleteSession(sessionId)
                emit(DataState.Success(true))
            }


        } catch (e: Exception) {
            emit(DataState.Error(getTypeErrorAppWrite(e)))
        }
        emit(DataState.Finished)
    }

    override fun getUserData(): Flow<DataState<Boolean>> = flow {
        emit(DataState.Loading)
        try {
            // Check If Session Id Is Available
            val account = io.appwrite.services.Account(client)
            val sessionId = pref.getValue<String>(ConstantsData.SESSION_ID, "")

            if (sessionId.isEmpty()) {
                println("getUserData :::: session isEmpty")

                emit(DataState.Success(false))
            } else {
                val session = account.getSession(sessionId)
                println("getUserData :::: session isNotEmpty")

                saveUser(account.get())
                println("getUserData :::: session isNotEmpty 2 ")

                //
                delay(2500)
                emit(DataState.Success(true))
            }


        } catch (e: Exception) {
            println("getUserData :::: " + e.message)
            e.printStackTrace()
            emit(DataState.Error(getTypeErrorAppWrite(e)))
        }
        emit(DataState.Finished)
    }


    private suspend fun saveUser(account: io.appwrite.models.Account) {
        val user = (User(
            id = account.id,
            name = account.name,
            email = account.email
        ))

        val checkUser = userService.getUser(user.id)
        if (checkUser != null) {
            pref.putValue(ConstantsData.USER, checkUser.toDomain().toJson())
        } else {
            pref.putValue(ConstantsData.USER, user.toJson())
        }
    }

    override fun saveUser(user: User): Flow<DataState<Boolean>> = flow {
        emit(DataState.Loading)
        try {
            pref.putValue(ConstantsData.USER, user.toJson())
            delay(2500)
            emit(DataState.Success(true))


        } catch (e: Exception) {
            emit(DataState.Error(getTypeErrorAppWrite(e)))
        }
        emit(DataState.Finished)
    }

}

