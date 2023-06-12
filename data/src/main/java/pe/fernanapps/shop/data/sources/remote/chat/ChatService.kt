package pe.fernanapps.shop.data.sources.remote.chat

import io.appwrite.services.Databases
import pe.fernanapps.shop.ConstantsData
import pe.fernanapps.shop.data.sources.remote.AppWriteService
import pe.fernanapps.shop.model.MessageCollection
import javax.inject.Inject

class ChatService @Inject constructor(
    override val databases: Databases,
) : AppWriteService(databases) {

    override val myCollectionId: String get() = ConstantsData.getAppWriteChatsId()

    suspend fun getChats(userId: String): List<MessageCollection> {
        return getAllDocumentsRecursiveWithQueryEqual<MessageCollection>(
            queryAttribute = "chat_id", queryValue = userId
        )

    }
}