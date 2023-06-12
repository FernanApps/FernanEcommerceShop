package pe.fernanapps.shop.utils


import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import pe.fernanapps.shop.data.BuildConfig
import javax.inject.Inject


/*

apply() = asynchronous.
commit() = synchronous

 */

class PrefManager @Inject constructor(private val context: Context, name: String) {
    private val pref: SharedPreferences
    private val editor: SharedPreferences.Editor
    private val _context: Context get() = context


    init {
        pref = _context.getSharedPreferences(name, Context.MODE_PRIVATE)
        editor = pref.edit()
    }

    fun setKey(key: String, value: Boolean) {
        editor.putBoolean(key, value)
        editor.commit()
    }

    fun setKey(key: String, value: String) {
        editor.putString(key, value)
        editor.commit()
    }

    fun setKey(key: String, value: Int) {
        editor.putInt(key, value)
        editor.commit()
    }

    fun setKey(key: String, value: Float) {
        editor.putFloat(key, value)
        editor.commit()
    }

    fun setKey(key: String, value: Long) {
        editor.putLong(key, value)
        editor.commit()
    }




    fun remove(key: String) {
        if (pref.contains(key) == true) {
            editor.remove(key)
            editor.commit()
            //
        }
    }

    operator fun contains(key: String): Boolean {
        return pref.contains(key) == true
    }

    /**
     * WARNING
     */
    fun deleteAll() {
        editor.clear()?.commit()
    }

    fun getBoolean(key: String): Boolean  {
        return getKey(key, false)
    }



    fun getKey(key: String, defVal:String = ""): String {
        return pref.getString(key, defVal) ?: defVal
    }

    fun getKey(key: String, defVal:Int = -1): Int {
        return pref.getInt(key, defVal)
    }

    fun getKey(key: String, defVal: Boolean = false): Boolean {
        return pref.getBoolean(key, defVal)
    }

    fun setKey(key: String ,value: Any) {
        when (value) {
            is Boolean -> setKey(key, value)
            is Int -> setKey(key, value)
            is String -> setKey(key, value)
            is Float -> setKey(key, value)
            is Long -> setKey(key, value)
            (value as? Set<String> != null) -> {
                editor.putStringSet(key, value as Set<String>)
                editor.commit()
            }
        }
    }

    fun <T> putValue(key: String, value: T) {
        val editor = pref.edit()
        when (value) {
            is String -> editor.putString(key, value)
            is Int -> editor.putInt(key, value)
            is Boolean -> editor.putBoolean(key, value)
            is Float -> editor.putFloat(key, value)
            is Long -> editor.putLong(key, value)
            else -> throw UnsupportedOperationException("Type not supported")
        }
        editor.apply()
    }

    fun <T> getValue(key: String, defaultValue: T): T {
        return when (defaultValue) {
            is String -> pref.getString(key, defaultValue) as T
            is Int -> pref.getInt(key, defaultValue) as T
            is Boolean -> pref.getBoolean(key, defaultValue) as T
            is Float -> pref.getFloat(key, defaultValue) as T
            is Long -> pref.getLong(key, defaultValue) as T
            else -> throw UnsupportedOperationException("Type not supported")
        }
    }
    val allData: Map<String, *>?
        get() = pref.all



}

