package pe.fernanapps.shop.data.sources.local.user

import pe.fernanapps.shop.domain.model.user.User
import pe.fernanapps.shop.domain.repository.UserLocalRepository
import javax.inject.Inject





class UserLocalLocalRepositoryImp @Inject constructor(private val userPreference: UserPreference) : UserLocalRepository {

    override fun getUserFromPreferences(): User? = userPreference.getUserFromPreferences()

    override fun saveUserToPreferences(user: User) = userPreference.saveUserToPreferences(user)

    override fun clearUserData() = userPreference.clearUserData()

}