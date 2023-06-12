package pe.fernanapps.shop.data.sources.remote.notifications

import io.appwrite.services.Databases
import pe.fernanapps.shop.ConstantsData
import pe.fernanapps.shop.data.sources.remote.AppWriteService
import pe.fernanapps.shop.model.NotificationCollection
import javax.inject.Inject

class NotificationsService @Inject constructor(
    override val databases: Databases,
) : AppWriteService(databases) {

    override val myCollectionId: String get() = ConstantsData.getAppWriteNotificationsId()

    suspend fun getNotifications(userId: String): List<NotificationCollection> {

        val list = mutableListOf<NotificationCollection>()

        val allNot = getAllDocumentsRecursiveWithQueryEqual<NotificationCollection>(
            queryAttribute = "recipients", queryValue =  "all"
        )
        val userNot = getAllDocumentsRecursiveWithQuerySearch<NotificationCollection>(
            queryAttribute = "recipients", queryValue = userId
        )
        println("NOTIF_SERVICE ::: $allNot")
        println("NOTIF_SERVICE ::: $userNot")

        list.addAll(allNot)
        list.addAll(userNot)
        
        return list.sortedByDescending { it.createdAt }

    }
}