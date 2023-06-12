package pe.fernanapps.shop.utils

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.annotations.Expose
import com.google.gson.reflect.TypeToken
import io.appwrite.extensions.gson
import java.lang.reflect.Type

// Gsony
// Thank you https://github.com/dhhAndroid/gson-ktx
object GsonKtx {

    private val strategy: ExclusionStrategy = object : ExclusionStrategy {
        override fun shouldSkipClass(clazz: Class<*>?): Boolean {
            return false
        }

        override fun shouldSkipField(field: FieldAttributes): Boolean {
            val exposeAnnotation = field.getAnnotation(Expose::class.java)
            return if(exposeAnnotation == null){
                false
            } else {
                !exposeAnnotation.serialize
            }
        }
    }

    val _X: Gson = GsonBuilder().addSerializationExclusionStrategy(strategy).setLenient().disableHtmlEscaping().create()

    inline fun <reified T> fromJson(json: String): T {
        val type = object : TypeToken<T>() {}.type
        return _X.fromJson(json, type)
    }


    inline fun <reified T> fromJson(json: Map<String, Any>): T {
        return gson.fromJson(_X.toJson(json), T::class.java)
    }

    inline fun <reified T> fromJsonNormal(json: String): T {
        val _X: Gson = GsonBuilder().setLenient().create()
        val type = object : TypeToken<T>() {}.type
        return _X.fromJson(json, type)
    }

    inline fun <reified T> fromJson(jsonElement: JsonElement): T {
        val type = object : TypeToken<T>() {}.type
        return _X.fromJson(jsonElement, type)
    }

    @JvmStatic
    fun toJson(any: Any): String = _X.toJson(any)

    @JvmStatic
    fun toJson(jsonElement: JsonElement): String = _X.toJson(jsonElement)

    @JvmStatic
    fun <T> fromJson(json: String?, typeOfT: Type?): T? {
        if (json == null) {
            return null
        }
        return _X.fromJson(json, typeOfT)
    }
}


inline fun <reified T> String.toBean() = GsonKtx.fromJsonNormal<T>(this)
inline fun <reified T> JsonElement.toBean() = GsonKtx.fromJson<T>(this)
inline fun <reified T> String.toModel() = GsonKtx.fromJson<T>(this)
inline fun <reified T> Map<String, Any>.toModel() = GsonKtx.fromJson<T>(this)

inline fun <reified T> JsonElement.toModel() = GsonKtx.fromJson<T>(this)
fun Any.toJson(): String = GsonKtx.toJson(this)
fun Any.toJsonPretty(): String = GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create().toJson(this)

fun JsonElement.toJson(): String = GsonKtx.toJson(this)




