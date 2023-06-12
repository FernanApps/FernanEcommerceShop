package pe.fernanapps.shop.data.sources.remote.orders

import io.appwrite.Query
import io.appwrite.services.Databases
import pe.fernanapps.shop.ConstantsData
import pe.fernanapps.shop.data.sources.remote.AppWriteService
import pe.fernanapps.shop.domain.model.order.OrderDetails
import pe.fernanapps.shop.domain.model.order.OrderItem
import pe.fernanapps.shop.model.OrderDetailsCollection
import pe.fernanapps.shop.model.OrderItemCollection
import pe.fernanapps.shop.model.ProductCollection
import pe.fernanapps.shop.model.toDomain
import pe.fernanapps.shop.utils.toModel
import javax.inject.Inject

class OrderService @Inject constructor(override val databases: Databases) :
    AppWriteService(databases) {

    suspend fun getAllOrdersDetails(userId: String): List<OrderDetails> {
        val collectionId = ConstantsData.getAppWriteOrderDetailsId()

        println("OrderService:::::: getAllOrdersDetails")
        val querySearch = Query.equal("user_id", userId)
        val orderDetailsList = mutableListOf<OrderDetailsCollection>()
        getAllDocuments(
            collectionId,
            queries = listOf(querySearch)
        ).documents.forEach {
            val fixItemWithDocumentId = it.data.toModel<OrderDetailsCollection>()
            orderDetailsList.add(fixItemWithDocumentId.copy(id = it.id))
        }
        println("OrderService:::::: orderDetailsList $orderDetailsList")


        return orderDetailsList.map { it.toDomain() }
    }

    suspend fun getAllOrdersItem(orderId: String): MutableList<OrderItem> {
        val collectionId = ConstantsData.getAppWriteOrderItemId()

        println("OrderService:::::: getAllOrdersItem orderId $orderId")

        val orderItemCollectionList = getAllDocumentsRecursiveWithQueryEqual<OrderItemCollection>(
            collectionId,
            "order_id",
            orderId
        )

        println("OrderService:::::: getAllOrdersItem orderItemCollectionList $orderItemCollectionList")


        /** Now Get All Products :( "*/
        val collectionProductsId = ConstantsData.getAppWriteProductsId()
        val productsId = orderItemCollectionList.map {it.productId }


        val finalQuery = Query.equal("id", productsId)


        println("OrderService:::::: getAllOrdersItem queries $finalQuery")

        val productCollectionList = getAllDocuments(collectionProductsId, listOf(finalQuery)).documents.map {
            it.data.toModel<ProductCollection>()
        }

        println("OrderService:::::: getAllOrdersItem productCollectionList $productCollectionList")

        val productsMap = productCollectionList.associateBy { it.id }
        
        val finalList = mutableListOf<OrderItem>()
         orderItemCollectionList.forEach {  
             val product = productsMap[it.productId]
             if(product != null){
                 val orderItem = it.toDomain().copy(product = product.toDomain())
                 finalList.add(orderItem)
             }
         }
        println("OrderService:::::: getAllOrdersItem finalList $finalList")


        return finalList

    }   

}
