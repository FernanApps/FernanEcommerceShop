package pe.fernanapps.shop.utils

import android.util.Base64


fun String.encodeBase64(): String {
    val data = this.toByteArray(Charsets.UTF_8)
    return Base64.encodeToString(data, Base64.NO_WRAP)
}

fun String.decodeBase64(): String {
    val base64 = Base64.decode(this, Base64.NO_WRAP)
    return base64.toString(Charsets.UTF_8)
}