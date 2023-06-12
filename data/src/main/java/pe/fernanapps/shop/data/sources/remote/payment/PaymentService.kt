package pe.fernanapps.shop.data.sources.remote.payment

import io.appwrite.models.Document
import io.appwrite.services.Databases
import pe.fernanapps.shop.ConstantsData
import pe.fernanapps.shop.data.sources.remote.AppWriteService
import pe.fernanapps.shop.model.OrderDetailsCollection
import pe.fernanapps.shop.model.OrderItemCollection
import pe.fernanapps.shop.model.PaymentCollection
import pe.fernanapps.shop.utils.toJson
import javax.inject.Inject

/**
 * For AppWrite, upload payment, order, orderItem
 */


class PaymentService @Inject constructor(override val databases: Databases): AppWriteService(databases) {


    private val collectionOrderId = ConstantsData.getAppWriteOrderDetailsId()
    private val collectionOrderItemId = ConstantsData.getAppWriteOrderItemId()
    private val collectionPaymentsId = ConstantsData.getAppWritePaymentsId()

    suspend fun uploadAndGetOrder(order: OrderDetailsCollection): Document {
        return uploadDocumentAndGet(order, collectionOrderId)
    }

    suspend fun uploadOrderItem(orderItem: OrderItemCollection): Document {
        return uploadDocumentAndGet(orderItem, collectionOrderItemId)
    }

    suspend fun uploadPayment(payment: PaymentCollection): Document {
        return uploadDocumentAndGet(payment, collectionPaymentsId)
    }


}

