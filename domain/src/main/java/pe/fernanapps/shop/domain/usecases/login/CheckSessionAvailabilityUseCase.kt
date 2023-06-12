package pe.fernanapps.shop.domain.usecases.login

import kotlinx.coroutines.flow.Flow
import pe.fernanapps.shop.domain.DataState
import pe.fernanapps.shop.domain.repository.LoginRepository
import javax.inject.Inject

class CheckSessionAvailabilityUseCase  @Inject constructor(
    private val loginRepository: LoginRepository
) {

    operator fun invoke(): Flow<DataState<Boolean>> =
        loginRepository.getUserData()
}