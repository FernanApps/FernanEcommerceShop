package pe.fernanapps.shop.data.sources.remote

import io.appwrite.ID
import io.appwrite.Query
import io.appwrite.models.Document
import io.appwrite.models.DocumentList
import io.appwrite.services.Databases
import pe.fernanapps.shop.ConstantsData
import pe.fernanapps.shop.utils.toJson
import pe.fernanapps.shop.utils.toModel

abstract class AppWriteService constructor(open val databases: Databases) {
    protected val databaseId = ConstantsData.getAppWriteDatabaseId()
    open val myCollectionId: String get() = TODO()

    protected suspend fun getDocument(
        collectionId: String = "", documentId: String,
    ): Document {
        return databases.getDocument(
            databaseId,
            collectionId.ifEmpty { myCollectionId },
            documentId = documentId
        )
    }


    protected suspend fun deleteDocument(
        collectionId: String = "",
        documentId: String,
    ) {
        databases.deleteDocument(
            databaseId,
            collectionId.ifEmpty { myCollectionId },
            documentId
        )
    }

    protected suspend inline fun <reified T : Any> updateDocumentAndGet(
        model: T,
        collectionId: String = "",
        documentId: String = ID.unique(),
    ): Document {
        val json = model.toJson()
        return databases.updateDocument(
            databaseId,
            collectionId.ifEmpty { myCollectionId },
            documentId,
            data = json
        )
    }

    protected suspend inline fun <reified T : Any> uploadDocumentAndGet(
        model: T,
        collectionId: String = "",
        documentId: String = ID.unique(),
    ): Document {
        val json = model.toJson()
        return databases.createDocument(
            databaseId,
            collectionId.ifEmpty { myCollectionId },
            documentId,
            data = json
        )
    }

    protected suspend fun getAllDocuments(
        collectionId: String = "",
        queries: List<String> = emptyList(),
    ): DocumentList {
        return databases.listDocuments(
            databaseId = databaseId,
            collectionId.ifEmpty { myCollectionId },
            queries = queries.ifEmpty { null }
        )
    }

    protected suspend inline fun <reified T : Any> getAllDocumentsRecursiveWithQuerySearch(
        collectionId: String = "",
        queryAttribute: String,
        queryValue: String,
    ): List<T> {
        val query = Query.search(queryAttribute, queryValue)
        return getAllDocumentsRecursiveWithQuery(collectionId, query)
    }

    protected suspend inline fun <reified T : Any> getAllDocumentsRecursiveWithQueryEqual(
        collectionId: String = "",
        queryAttribute: String,
        queryValue: String,
    ): List<T> {
        val query = Query.equal(queryAttribute, queryValue)
        return getAllDocumentsRecursiveWithQuery<T>(collectionId, query)
    }


    protected suspend inline fun <reified T : Any> getAllDocumentsRecursiveWithQuery(
        collectionId: String = "",
        query: String,
    ) = getAllDocumentsPureRecursiveWithQuery(collectionId, query).map {
        it.data.toModel<T>()
    }


    protected suspend inline fun getAllDocumentsPureRecursiveWithQuery(
        collectionId: String = "",
        query: String,
    ): MutableList<Document> {

        val queries = mutableListOf(Query.limit(1), Query.offset(0))
        queries.add(query)

        val responseFirst = getAllDocuments(
            collectionId.ifEmpty { myCollectionId },
            queries = queries
        )

        val limit = 25
        val totalDocuments = responseFirst.total

        val documents = mutableListOf<Document>()

        for (offset in 0 until totalDocuments step limit.toLong()) {
            val response = databases.listDocuments(
                databaseId = databaseId,
                collectionId.ifEmpty { myCollectionId },
                queries = mutableListOf(Query.limit(limit), Query.offset(offset.toInt()), query)
            )
            documents.addAll(response.documents)
        }

        return documents
    }


}
