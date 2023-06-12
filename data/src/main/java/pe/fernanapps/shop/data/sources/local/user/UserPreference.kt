package pe.fernanapps.shop.data.sources.local.user

import pe.fernanapps.shop.ConstantsData
import pe.fernanapps.shop.domain.model.user.User
import pe.fernanapps.shop.utils.PrefManager
import pe.fernanapps.shop.utils.toJson
import pe.fernanapps.shop.utils.toModel
import javax.inject.Inject

class UserPreference @Inject constructor(
    private val pref: PrefManager,
) {

    fun getUserFromPreferences(): User? {
        val json = pref.getValue<String>(ConstantsData.USER, "")
        return if (json.isNotEmpty()) {
            json.toModel<User>()

        } else {
            null
        }
    }

    fun saveUserToPreferences(user: User) {
        pref.putValue(ConstantsData.USER, user.toJson())
    }

    fun clearUserData() {
        pref.remove(ConstantsData.USER)
    }
}