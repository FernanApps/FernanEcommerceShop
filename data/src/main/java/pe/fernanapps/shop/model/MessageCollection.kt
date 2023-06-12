package pe.fernanapps.shop.model

import com.google.gson.annotations.SerializedName
import pe.fernanapps.shop.domain.model.chat.Message
import java.util.Date

data class MessageCollection(
    @SerializedName("sender_id") var senderId: String,
    @SerializedName("text") var text: String = "",
    @SerializedName("epoch_time_ms") var epochTimeMs: Long = Date().time,
    @SerializedName("chat_id") var chatId: String,
    @SerializedName("seen") var seen: Boolean = false
){

    fun toDomain() = Message(senderId, text, epochTimeMs, chatId, seen)

    override fun toString(): String {
        return "MessageCollection(senderId='$senderId', text='$text', epochTimeMs=$epochTimeMs, chatId='$chatId', seen=$seen)"
    }
}

fun Message.toCollection() = MessageCollection(senderId, text, epochTimeMs, chatId, seen)
