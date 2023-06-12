package pe.fernanapps.shop.data.sources.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import pe.fernanapps.shop.data.sources.local.ConstantsDatabase
import pe.fernanapps.shop.domain.model.notifications.Notification
import pe.fernanapps.shop.domain.model.notifications.NotificationStatus

@Entity(tableName = ConstantsDatabase.TABLE_NOTIFICATIONS)
class NotificationEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "status") val status: NotificationStatus,
    @ColumnInfo(name = "recipients") val recipients: ArrayList<String>,
    @ColumnInfo(name = "image_url") val imageUrl: String,
) {
    fun toDomain() = Notification(
        id = this.id,
        title = this.title,
        content = this.content,
        createdAt = this.createdAt,
        status = this.status,
        recipients = this.recipients,
        imageUrl = this.imageUrl
    )
}

fun Notification.toEntity() = NotificationEntity(
    id = this.id,
    title = this.title,
    content = this.content,
    createdAt = this.createdAt,
    status = this.status,
    recipients = ArrayList(this.recipients),
    imageUrl = this.imageUrl
)