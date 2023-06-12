package pe.fernanapps.shop.data.sources.remote.user

import io.appwrite.services.Databases
import pe.fernanapps.shop.ConstantsData
import pe.fernanapps.shop.data.sources.remote.AppWriteService
import pe.fernanapps.shop.model.UserCollection
import pe.fernanapps.shop.utils.toModel
import javax.inject.Inject

class UserService @Inject constructor(databases: Databases) : AppWriteService(databases) {
    override val myCollectionId: String get() = ConstantsData.getAppWriteCustomersId()

    suspend fun getUser(userId: String): UserCollection? {
        return try {
            getDocument(documentId = userId).data.toModel<UserCollection>()
        } catch (e: Exception) {
            println("getUser :::: ${e.message}")
            e.printStackTrace()
            null
        }
    }


    suspend fun saveUser(user: UserCollection) = uploadDocumentAndGet(user, documentId = user.id)

    suspend fun updateUser(user: UserCollection) = updateDocumentAndGet(user, documentId = user.id)

    suspend fun clearUser(userId: String) = deleteDocument(documentId = userId)
}
