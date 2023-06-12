package pe.fernanapps.shop.utils

import android.annotation.SuppressLint
import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

object TimeUtils {

    fun formatTimeAgo(timeInMillis: Long): String {
        val currentTime = System.currentTimeMillis()
        val timeDifference = currentTime - timeInMillis

        return when {
            timeDifference < DateUtils.HOUR_IN_MILLIS -> {
                val hoursAgo = (timeDifference / DateUtils.MINUTE_IN_MILLIS).toInt()
                "$hoursAgo hours ago"
            }
            timeDifference < DateUtils.DAY_IN_MILLIS -> {
                val daysAgo = (timeDifference / DateUtils.HOUR_IN_MILLIS).toInt()
                "$daysAgo days ago"
            }
            timeDifference < DateUtils.DAY_IN_MILLIS * 3 -> {
                val dateFormat = SimpleDateFormat("dd MMMM", Locale.getDefault())
                dateFormat.format(Date(timeInMillis))
            }
            else -> {
                val dateFormat = SimpleDateFormat("dd MMMM", Locale.getDefault())
                dateFormat.format(Date(timeInMillis))
            }
        }
    }

    fun convertTimestampToFormattedDate(timestamp: Long): String {
        return formattedDate(timestamp, "dd MMM, yyyy")
    }

    fun formattedDate(timestamp: Long, format: String): String {
        val dateFormat = SimpleDateFormat(format, Locale.getDefault())
        val date = Date(timestamp)
        return dateFormat.format(date)
    }


    @SuppressLint("SimpleDateFormat")
    fun time(epochTimeMs: Long): String {
        if (epochTimeMs > 0) {
            val currentTimeMs = Date().time
            val numOfDays = TimeUnit.MILLISECONDS.toDays(currentTimeMs - epochTimeMs)

            val replacePattern = when {
                numOfDays >= 1.toLong() -> "Yy"
                else -> "YyMd"
            }
            val pat = SimpleDateFormat().toLocalizedPattern().replace("\\W?[$replacePattern]+\\W?".toRegex(), " ")
            val formatter = SimpleDateFormat(pat, Locale.getDefault())
            return formatter.format(Date(epochTimeMs))
        }
        return ""
    }
}