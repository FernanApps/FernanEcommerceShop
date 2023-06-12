package pe.fernanapps.shop.data.sources.remote.chat

import io.appwrite.ID
import io.appwrite.extensions.toJson
import io.appwrite.models.RealtimeResponseEvent
import io.appwrite.models.RealtimeSubscription
import io.appwrite.services.Databases
import io.appwrite.services.Realtime
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import pe.fernanapps.shop.ConstantsData
import pe.fernanapps.shop.domain.model.chat.Message
import pe.fernanapps.shop.domain.repository.MessageRepository
import pe.fernanapps.shop.model.MessageCollection
import pe.fernanapps.shop.model.toCollection
import javax.inject.Inject

class MessageRepositoryImp @Inject constructor(
    private val databases: Databases,
    private val realtime: Realtime,
) : MessageRepository {

    private val databaseId = ConstantsData.getAppWriteDatabaseId()
    private val collectionId = ConstantsData.getAppWriteChatsId()

    private var subscription: RealtimeSubscription? = null
    private val channelDocuments = "documents"


    override suspend fun subscribeToMessages(userId: String): Flow<Message> = callbackFlow {
        subscription = realtime.subscribe(
            channelDocuments,
            payloadType = MessageCollection::class.java
        ) {
            if(it.payload is MessageCollection){
                if (it.payload.chatId == userId) {
                    val message = handleMessageEvent(it)
                    if (message != null) {
                        trySend(message)
                    }
                }
            }

        }
        awaitClose {
            subscription?.close()
        }
    }.catch {

    }

    private fun handleMessageEvent(
        it: RealtimeResponseEvent<MessageCollection>,
    ): Message? {
        println("events ::: ${it.events}")

        val eventString = "databases.$databaseId.collections.$collectionId.documents.*"

        it.events.filter { event -> event == eventString }.forEach { final ->
            println("payload ::: " + it.payload.toString());
            it?.payload?.let { payload ->
                when (final) {
                    in
                    "${eventString}.create",
                    "${eventString}.update",
                    -> {
                        return (payload.toDomain())
                    }
                }
            }
        }

        return null

    }


    override suspend fun sendMessage(message: Message) {
        val messageJson = message.toCollection().toJson()
        println("<createMessage> :: sending $messageJson")
        val response = databases.createDocument(
            databaseId = databaseId,
            collectionId = collectionId,
            documentId = ID.unique(),
            data = messageJson
        )
        println("<createMessage> :: response $response")
    }

    override fun unsubscribeFromMessages() {
        subscription?.close()
    }


}


//class MessageService @Inject constructor(
//    private val databases: Databases,
//    private val realtime: Realtime,
//) {
//
//    private val databaseId = ConstantsData.getAppWriteDatabaseId()
//    private val collectionId = ConstantsData.getAppWriteChatsId()
//
//    private val _messageReceived = MutableLiveData<Message>()
//    val messageReceived: LiveData<Message> = _messageReceived
//
//    private var subscription: RealtimeSubscription? = null
//
//    private val channelDocuments = "documents"
//
//    fun subscribeToMessages() {
//        subscription = realtime.subscribe(
//            channelDocuments, payloadType = MessageCollection::class.java,
//            callback = ::handleMessageEvent
//        )
//    }
//
//
//    private fun handleMessageEvent(it: RealtimeResponseEvent<MessageCollection>) {
//        println("events ::: ${it.events}")
//
//
//        val eventString = "databases.$databaseId.collections.$collectionId.documents.*"
//
//        it.events.filter { event -> event == eventString }.forEach { final ->
//            println("payload ::: " + it.payload.toString());
//
//            it?.payload?.let { payload ->
//                when (final) {
//                    in
//                    "${eventString}.create",
//                    "${eventString}.update",
//                    -> _messageReceived.postValue(payload.toDomain())
//                }
//            }
//        }
//    }
//
//    suspend fun sendMessage(message: Message) {
//        val messageJson = message.toCollection().toJson()
//        println("<createMessage> :: sending $messageJson")
//        val response = databases.createDocument(
//            databaseId = ConstantsData.getAppWriteDatabaseId(),
//            collectionId = ConstantsData.getAppWriteChatsId(),
//            documentId = ID.unique(),
//            data = messageJson
//        )
//        println("<createMessage> :: response $response")
//    }
//
//    fun unsubscribeFromMessages() {
//        subscription?.close()
//    }
//}

