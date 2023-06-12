package pe.fernanapps.shop.model

import com.google.gson.annotations.SerializedName
import pe.fernanapps.shop.domain.model.notifications.Notification
import pe.fernanapps.shop.domain.model.notifications.NotificationStatus

data class NotificationCollection(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("created_at") val createdAt: Long,
    @SerializedName("status") val status: NotificationStatus?,
    @SerializedName("recipients") val recipients: String,
    @SerializedName("image_url") val imageUrl: String,
) {
    fun toDomain() = Notification(
        id = this.id,
        title = this.title,
        content = this.content,
        createdAt = this.createdAt,
        status = this.status ?: NotificationStatus.UNREAD,
        recipients = if (this.recipients.contains(",")) {
            this.recipients.split(",")
        } else {
            listOf(this.recipients)
        },
        imageUrl = this.imageUrl
    )
}

