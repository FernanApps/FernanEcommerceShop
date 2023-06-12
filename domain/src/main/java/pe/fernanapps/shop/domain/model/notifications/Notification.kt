package pe.fernanapps.shop.domain.model.notifications

data class Notification(
    val id: String,
    val title: String,
    val content: String,
    val createdAt: Long,
    val status: NotificationStatus,
    val recipients: List<String>,
    val imageUrl: String
)
enum class NotificationStatus {
    UNREAD,
    READ,
    ARCHIVED,
    HIGHLIGHTED
}