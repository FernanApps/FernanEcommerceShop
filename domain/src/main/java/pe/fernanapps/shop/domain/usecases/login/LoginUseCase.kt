package pe.fernanapps.shop.domain.usecases.login

import kotlinx.coroutines.flow.Flow
import pe.fernanapps.shop.domain.DataState
import pe.fernanapps.shop.domain.repository.LoginRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {
    operator fun invoke(email: String, password: String): Flow<DataState<Boolean>> =
        loginRepository.login(email, password)
}

