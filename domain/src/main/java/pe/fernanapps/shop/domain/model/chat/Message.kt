package pe.fernanapps.shop.domain.model.chat

import pe.fernanapps.shop.domain.model.To
import java.util.Date

data class Message(
    var senderId: String,
    var text: String = "",
    var epochTimeMs: Long = Date().time,
    var chatId: String,
    var seen: Boolean = false
)

