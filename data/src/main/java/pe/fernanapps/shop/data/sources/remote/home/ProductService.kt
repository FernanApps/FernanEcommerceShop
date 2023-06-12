package pe.fernanapps.shop.data.sources.remote.home

import io.appwrite.Query
import io.appwrite.services.Databases
import pe.fernanapps.shop.ConstantsData
import pe.fernanapps.shop.domain.NetworkResult
import pe.fernanapps.shop.domain.model.product.Category
import pe.fernanapps.shop.domain.model.product.Offer
import pe.fernanapps.shop.domain.model.product.Product
import pe.fernanapps.shop.model.CategoryCollection
import pe.fernanapps.shop.model.OfferCollection
import pe.fernanapps.shop.model.ProductCollection
import pe.fernanapps.shop.model.toDomain
import pe.fernanapps.shop.utils.toModel
import retrofit2.HttpException
import javax.inject.Inject

class ProductService @Inject constructor(
    private val databases: Databases,
) {
    suspend fun getOffers(): NetworkResult<List<Offer>> {
        return try {

            val response = databases.listDocuments(
                databaseId = ConstantsData.getAppWriteDatabaseId(),
                collectionId = ConstantsData.getAppWriteOffersId()
            )
            println("getOffers :::: ${response.total}")
            println("getOffers :::: ${response.documents.toString()}")

            val offers = mutableListOf<Offer>()

            response.documents.forEach { document ->
                val offerCollection = document.data.toModel<OfferCollection>()
                println("getOffers :::: offerCollection ::: ${offerCollection.toString()}")
                // Get Product :( Temp
                val responseProduct = databases.getDocument(
                    databaseId = ConstantsData.getAppWriteDatabaseId(),
                    collectionId = ConstantsData.getAppWriteProductsId(),
                    documentId = offerCollection.productId
                )
                println("responseProduct $responseProduct")
                val product = (responseProduct.data.toModel<ProductCollection>()).toDomain()
                println("responseProduct product $product")

                offers.add(offerCollection.toDomain(product))
            }
            NetworkResult.Success(offers)

        } catch (e: HttpException) {
            NetworkResult.Failure(code = e.code(), message = e.message())
        } catch (e: Exception) {
            NetworkResult.Exception(e)
        }
    }

    suspend fun getProducts(
        categoryId: String?,
        page: Int,
        size: Int,
    ): NetworkResult<List<Product>> {
        return try {

            val databaseId = ConstantsData.getAppWriteDatabaseId()
            val collectionId = ConstantsData.getAppWriteProductsId()


            // :categoryId

            val queries = mutableListOf(Query.limit(1), Query.offset(0))
            val querySearch =
                if (categoryId != null) Query.equal("category", categoryId.uppercase()) else {
                    null
                }

            querySearch?.let {
                queries.add(it)
            }

            var response = databases.listDocuments(
                databaseId = databaseId,
                collectionId = collectionId,
                queries = queries

            )
            println("first query $querySearch")

            println("response Uno ${response.total}")
            println("response Uno ${response.documents.toString()}")


            val limit = size
            val offset = size * page

            // Check If Offset
            if (offset < (response.total - size) || categoryId != null) {
                // Ignore
                val queriesNew = mutableListOf(Query.limit(limit), Query.offset(offset))
                querySearch?.let {
                    queriesNew.add(it)
                }

                response = databases.listDocuments(
                    databaseId = databaseId,
                    collectionId = collectionId,
                    queries = queriesNew

                )
                val products = mutableListOf<Product>()
                response.documents.forEachIndexed { index, document ->
                    println("__product $index")
                    val product = document.data.toModel<ProductCollection>()
                    products.add(product.toDomain())
                }
                NetworkResult.Success(products)
            } else {
                NetworkResult.Success(emptyList())
            }

        } catch (e: HttpException) {
            NetworkResult.Failure(code = e.code(), message = e.message())
        } catch (e: Exception) {
            NetworkResult.Exception(e)
        }
    }

    suspend fun getCategories(): NetworkResult<List<Category>> {

        return try {

            val response = databases.listDocuments(
                databaseId = ConstantsData.getAppWriteDatabaseId(),
                collectionId = ConstantsData.getAppWriteCategoriesId()
            )
            val categories = mutableListOf<Category>()

            /*

            data={id=cat8,
                name=Jeans,
                $id=categories_8,
                $createdAt=2023-05-29T14:02:05.075+00:00,
                $updatedAt=2023-05-29T14:02:05.075+00:00,
                $permissions=[read("any")],
                $collectionId=categories,
                $databaseId=64739b907d3c97bfbc76
            }

             */
            response.documents.forEach { document ->
                val category = document.data.toModel<CategoryCollection>()
                categories.add(category.toDomain())
            }
            NetworkResult.Success(categories)

        } catch (e: HttpException) {
            NetworkResult.Failure(code = e.code(), message = e.message())
        } catch (e: Exception) {
            NetworkResult.Exception(e)
        }
    }
}


//private inline fun <reified T : Any> genericResponseRetrofitToNetworkResult(response: Response<T?>): NetworkResult<T> {
//    return try {
//        if (response.isSuccessful && response.body() != null) {
//            NetworkResult.Success(response.body()!!)
//        } else {
//            NetworkResult.Failure(code = response.code(), message = response.message())
//        }
//    } catch (e: HttpException) {
//        NetworkResult.Failure(code = e.code(), message = e.message())
//    } catch (e: Exception) {
//        NetworkResult.Exception(e)
//    }
//
//}