package pe.fernanapps.shop.domain.usecases.signup

import kotlinx.coroutines.flow.Flow
import pe.fernanapps.shop.domain.DataState
import pe.fernanapps.shop.domain.model.user.User
import pe.fernanapps.shop.domain.repository.LoginRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {

    operator fun invoke(user: User, password: String): Flow<DataState<User>> =
        loginRepository.signUp(user, password)

}