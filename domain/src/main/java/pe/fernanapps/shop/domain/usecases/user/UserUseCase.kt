package pe.fernanapps.shop.domain.usecases.user

import pe.fernanapps.shop.domain.model.user.User
import pe.fernanapps.shop.domain.repository.UserLocalRepository
import pe.fernanapps.shop.domain.repository.UserRemoteRepository
import javax.inject.Inject

/** Local */
class SaveUserLocalUseCase @Inject constructor(private val userLocalRepository: UserLocalRepository) {
    operator fun invoke(user: User) {
        userLocalRepository.saveUserToPreferences(user)
    }
}

class ClearUserLocalUseCase @Inject constructor(private val userLocalRepository: UserLocalRepository) {
    operator fun invoke() {
        userLocalRepository.clearUserData()
    }
}

class GetUserLocalUseCase @Inject constructor(private val userLocalRepository: UserLocalRepository) {
    operator fun invoke(): User? {
        return userLocalRepository.getUserFromPreferences()
    }
}


/** Remote */
class GetUserRemoteUseCase @Inject constructor (private val userRemoteRepository: UserRemoteRepository) {
    suspend operator fun invoke(userId: String) = userRemoteRepository.getUser(userId)

}

class SaveUserRemoteUseCase @Inject constructor (private val userRemoteRepository: UserRemoteRepository) {
    suspend operator fun invoke(user: User) = userRemoteRepository.saveUser(user)

}

class ClearUserRemoteUseCase @Inject constructor (private val userRemoteRepository: UserRemoteRepository) {
    suspend operator fun invoke(userId: String) {
        userRemoteRepository.clearUser(userId)
    }
}
