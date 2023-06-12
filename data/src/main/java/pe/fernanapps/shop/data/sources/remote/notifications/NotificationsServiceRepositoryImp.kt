package pe.fernanapps.shop.data.sources.remote.notifications

import io.appwrite.models.RealtimeResponseEvent
import io.appwrite.models.RealtimeSubscription
import io.appwrite.services.Realtime
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import pe.fernanapps.shop.ConstantsData
import pe.fernanapps.shop.data.sources.local.user.UserPreference
import pe.fernanapps.shop.domain.model.notifications.Notification
import pe.fernanapps.shop.domain.repository.NotificationsServiceRepository
import pe.fernanapps.shop.model.NotificationCollection
import javax.inject.Inject

class NotificationsServiceRepositoryImp @Inject constructor(
    private val userPreference: UserPreference,
    private val realtime: Realtime,
) : NotificationsServiceRepository {

    private val databaseId = ConstantsData.getAppWriteDatabaseId()
    private val collectionId = ConstantsData.getAppWriteNotificationsId()

    private var subscription: RealtimeSubscription? = null
    private val channelDocuments = "documents"


    override fun subscribe(): Flow<Notification> = callbackFlow {
        val userId = userPreference.getUserFromPreferences()?.id ?: return@callbackFlow

        subscription = realtime.subscribe(
            channelDocuments,
            payloadType = NotificationCollection::class.java
        ) {
            try {
                if(it.payload is NotificationCollection){
                    val recipients = it.payload.recipients
                    var isForEveryone = false
                    var isUserEveryone = false

                    if(recipients.contains(",")){
                        isForEveryone = "all" in recipients.split(",")
                        isUserEveryone = userId in recipients.split(",")

                    } else {
                        isForEveryone = "all" in recipients
                        isUserEveryone = userId in recipients
                    }

                    if (isForEveryone || isUserEveryone) {
                        val message = handleMessageEvent(it)
                        if (message != null) {
                            trySend(message)
                        }
                    }
                }

            } catch (e: Exception){

            }

        }
        awaitClose {
            subscription?.close()
        }
    }.catch {

    }

    private fun handleMessageEvent(
        it: RealtimeResponseEvent<NotificationCollection>,
    ): Notification? {
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

    override fun unsubscribe() {
        subscription?.close()
    }



}

