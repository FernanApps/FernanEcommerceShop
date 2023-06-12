package pe.fernanapps.shop

import io.appwrite.extensions.toJson
import io.appwrite.models.Document
import io.appwrite.services.Databases
import pe.fernanapps.shop.domain.model.To
import pe.fernanapps.shop.utils.toModel


object AppWriteUtils {
//    suspend inline fun <reified T : Any> getDocumentAsModel(
//        databases: Databases,
//        databaseId: String,
//        collectionId: String,
//        documentId: String,
//    ): T {
//        return getDocument(
//            databases = databases,
//            databaseId = databaseId,
//            collectionId = collectionId,
//            documentId = documentId
//        ).data.toModel<T>()
//    }
//
//    suspend inline fun getDocument(
//        databases: Databases,
//        databaseId: String,
//        collectionId: String,
//        documentId: String,
//    ): Document {
//        return databases.getDocument(
//            databaseId = databaseId,
//            collectionId = collectionId,
//            documentId = documentId
//        )
//    }
//
//
//    suspend inline fun <reified T : To> saveDocumentAndGet(
//        model: To,
//        databases: Databases,
//        databaseId: String,
//        collectionId: String,
//        documentId: String
//    ): T {
//        return saveDocument(model, databases, databaseId, collectionId, documentId).data.toModel<T>()
//    }
//
//    suspend inline fun <reified T : To> updateDocumentAndGet(
//        model: To,
//        databases: Databases,
//        databaseId: String,
//        collectionId: String,
//        documentId: String,
//    ): T {
//        return updateDocument(
//            model,
//            databases,
//            databaseId,
//            collectionId,
//            documentId
//        ).data.toModel<T>()
//    }
//
//
//    suspend fun updateDocument(
//        model: To,
//        databases: Databases,
//        databaseId: String,
//        collectionId: String,
//        documentId: String,
//    ): Document {
//        val modelToJson = model.toCollection().toJson()
//        return databases.updateDocument(
//            databaseId,
//            collectionId,
//            documentId,
//            data = modelToJson
//        )
//    }
//
//
//    suspend fun saveDocument(
//        model: To,
//        databases: Databases,
//        databaseId: String,
//        collectionId: String,
//        documentId: String
//    ): Document {
//        val modelToJson = model.toCollection().toJson()
//        println("<createMessage> :: sending $modelToJson")
//        return databases.createDocument(
//            databaseId = databaseId,
//            collectionId = collectionId,
//            documentId = documentId,
//            data = modelToJson
//        )
//    }


}