import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/** ----------------------- VARIABLES -----------------------------------------------*/

                            // "https://example.gitpod.io/v1"
const val APPWRITE_ENDPOINT = "YOUR_END_POINT"
const val APPWRITE_PROJECT_ID = "YOUR_PROJECT_ID"
const val APPWRITE_API_KEY = "YOUR_API_KEY"

const val DATABASE_ID = "fernan_shop_db"
const val DATABASE_NAME = DATABASE_ID

/**
 *
 * ----------------------- COLLECTIONS -----------------------------------------------
 *
 *          AttrType URL, STRING, INT, FLOAT, BOOLEAN, ENUM
 *
 * */

val collections = listOf(
    "categories" to listOf(
        CollectionAttr("id", 36),
        CollectionAttr("title", 150),
        CollectionAttr("image", 300)
    ),
    "offers" to listOf(
        CollectionAttr("product_id", 36),
        CollectionAttr("title", 200),
        CollectionAttr("description", 350),
        CollectionAttr("image", 300),
        CollectionAttr("code", 36)
    ),
    "products2" to listOf(
        CollectionAttr("id", 36),
        CollectionAttr("title", 200),
        CollectionAttr("subtitle", 250),
        CollectionAttr("description", 350),
        CollectionAttr("price", 350, AttrType.FLOAT),
        CollectionAttr("size", 350),
        CollectionAttr("category", 36),
        CollectionAttr("image", 300),
    ),
    "chats" to listOf(
        CollectionAttr("sender_id", 36),
        CollectionAttr("text", 500),
        CollectionAttr("epoch_time_ms", 0, AttrType.FLOAT),
        CollectionAttr("chat_id", 36),
        CollectionAttr("seen", 0, AttrType.BOOLEAN),
    ),
    "payments" to listOf(
        CollectionAttr("id", 36),
        CollectionAttr("client_id", 36),
        CollectionAttr("order_id", 36),
        CollectionAttr("amount", 0, AttrType.FLOAT),
        CollectionAttr("payment_type", 150),
        CollectionAttr("created_at", 0, AttrType.FLOAT),
        CollectionAttr("currency", 100),
    ),
    "order_details" to listOf(
        CollectionAttr("id", 36),
        CollectionAttr("user_id", 36),
        CollectionAttr("payment_id", 36),
        CollectionAttr("total", 0, AttrType.FLOAT),
        CollectionAttr("created_at", 0, AttrType.FLOAT),
        CollectionAttr("status", 0, AttrType.ENUM).setElements(
            "PENDING", "PROCESSING", "CONFIRMED", "SHIPPED", "DELIVERED", "CANCELLED"
        )
    ),
    "order_item" to listOf(
        CollectionAttr("id", 36),
        CollectionAttr("product_id", 36),
        CollectionAttr("order_id", 36),
        CollectionAttr("quantity", 36, AttrType.INT),
        CollectionAttr("size", 100),
        CollectionAttr("created_at", 0, AttrType.FLOAT),
        CollectionAttr("sub_total", 0, AttrType.FLOAT),
    ),
    "customers" to listOf(
        CollectionAttr("id", 36),
        CollectionAttr("name", 250),
        CollectionAttr("phone", 50),
        CollectionAttr("email", 75),
        CollectionAttr("city", 75),
        CollectionAttr("country", 75),
        CollectionAttr("line_1", 75),
        CollectionAttr("line_2", 75),
        CollectionAttr("postal_code", 36),
        CollectionAttr("state", 75),
    ),
    "notifications" to listOf(
        CollectionAttr("id", 36),
        CollectionAttr("title", 200),
        CollectionAttr("content", 500),
        CollectionAttr("created_at", 0, AttrType.FLOAT),
        CollectionAttr("status", 0, AttrType.ENUM).setElements(
            "UNREAD", "READ", "ARCHIVED", "HIGHLIGHTED"
        ),
        CollectionAttr("recipients", 500),
        CollectionAttr("image_url", 300),
    )
)



fun main() {

    /** ------------------------- CREATE DATABASE -------------------------------- */
    val data = """
        {
          "databaseId": "$DATABASE_ID",
          "name": "$DATABASE_NAME"
        }
    """.trimIndent()

    httpPost("/databases", data)


    /** ------------------------- CREATE COLLECTIONS -------------------------------**/

    for (pair in collections) {
        val collectionId = pair.first
        val attrList = pair.second

        val collectionData = """
            {
              "collectionId": "$collectionId",
              "name": "$collectionId",
              "permissions": ["read(\"any\")"],
              "documentSecurity": false
            }
        """.trimIndent()

        httpPost("/databases/$DATABASE_ID/collections", collectionData)

        /** ---------------------- CREATE ATTRIBUTES OF COLLECTIONS   -------------------------**/
        attrList.forEach {
            val finalAttr = it.copy(collectionId = collectionId)
            httpPost(finalAttr.attrPath(), finalAttr.toData())
        }
    }

}



fun httpPost(pathUrl: String, data: String): HttpURLConnection? {
    try {
        val endpoint = APPWRITE_ENDPOINT
        val url = URL("$endpoint$pathUrl")
        val connection = url.openConnection() as HttpURLConnection
        connection.apply {
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("X-Appwrite-Response-Format", "1.0.0")
            connection.setRequestProperty("X-Appwrite-Project", APPWRITE_PROJECT_ID)
            connection.setRequestProperty("X-Appwrite-Key", APPWRITE_API_KEY)
            connection.doOutput = true
        }
        connection.outputStream.use { outputStream ->
            val input = data.toByteArray()
            outputStream.write(input, 0, input.size)
        }

        val responseCode = connection.responseCode
        val bufferedReader = BufferedReader(InputStreamReader(connection.inputStream))
        var response = bufferedReader.readLine()
        while (response != null) {
            println(response)
            response = bufferedReader.readLine()
        }

        bufferedReader.close()
        println("Response Code: $responseCode")
        println()

        return connection
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null

}

data class CollectionAttr(
    private val key: String,
    private val size: Int = 36,
    private val type: AttrType = AttrType.STRING,
    var collectionId: String = ""
) {


    private val databaseId = DATABASE_ID
    private val basePathAttr = "/databases/${databaseId}/collections/$collectionId/attributes/"

    private fun buildPath(attrType: String): String {
        return basePathAttr + attrType
    }

    fun attrPath() = buildAttrPatWithData().first
    fun toData() = buildAttrPatWithData().second


    private fun buildAttrPatWithData(): Pair<String, String> {
        return when (this.type) {
            AttrType.INT -> buildPath("integer") to toDataIntegerType()
            AttrType.URL -> buildPath("url") to toDataUrlType()
            AttrType.FLOAT -> buildPath("float") to toDataFloatType()
            AttrType.BOOLEAN -> buildPath("boolean") to toDataBooleanType()
            AttrType.ENUM -> buildPath("enum ") to toDataEnumType()

            else -> buildPath("string") to toDataStringType()
        }
    }


    private fun toDataUrlType() = """
        {
          "key": "${this.key}",
          "required": false,
          "array": false
        }
    """.trimIndent()

    private fun toDataStringType() = """
        {
          "key": "${this.key}",
          "size": ${this.size},
          "required": false,
          "array": false
        }
    """.trimIndent()

    private fun toDataFloatType() = toDataUrlType()
    private fun toDataBooleanType() = toDataUrlType()
    private fun toDataIntegerType() = toDataUrlType()

    private var elements: Array<out String> = emptyArray()
    private val elementsString get() = elements.joinToString(separator = ",", transform = { "\"$it\"" })

    private fun toDataEnumType() = """
        {
          "key": "${this.key}",
          "elements": [${this.elementsString}],
          "required": false,
          "array": false
        }
    """.trimIndent()


    fun setElements(vararg enums: String): CollectionAttr {
        elements = enums
        return this
    }


}

enum class AttrType {
    URL,
    STRING,
    INT,
    FLOAT,
    BOOLEAN,
    ENUM
}