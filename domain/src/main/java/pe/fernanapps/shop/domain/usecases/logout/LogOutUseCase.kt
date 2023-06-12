package pe.fernanapps.shop.domain.usecases.logout

import kotlinx.coroutines.flow.Flow
import pe.fernanapps.shop.domain.DataState
import pe.fernanapps.shop.domain.repository.LoginRepository
import javax.inject.Inject

class LogOutUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {

    suspend operator fun invoke(): Flow<DataState<Boolean>> =
        loginRepository.logOut()
}