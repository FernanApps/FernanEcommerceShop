package pe.fernanapps.shop.utils

import java.util.Locale

object StringExt {
    fun formatCurrency(value: Float, symbol: String = "$"): String = try {
        String.format("${symbol}%.2f", value)
    } catch (e: Exception) {
        value.toString()
    }
    fun capitalizeFirstLetter(texto: String): String {
        return texto.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
    }

}
