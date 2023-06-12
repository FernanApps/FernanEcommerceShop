package pe.fernanapps.shop.ui.main.notifications

import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pe.fernanapps.shop.databinding.ItemNotificationBinding
import pe.fernanapps.shop.domain.model.notifications.Notification
import pe.fernanapps.shop.domain.model.product.Offer
import pe.fernanapps.shop.utils.TimeUtils
import pe.fernanapps.shop.utils.load

class NotificationAdapter :
    ListAdapter<Notification, NotificationAdapter.NotificationViewHolder>(NotificationDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return NotificationViewHolder(
            ItemNotificationBinding.inflate(inflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class NotificationViewHolder(val bin: ItemNotificationBinding) :
        RecyclerView.ViewHolder(bin.root) {
        fun bind(item: Notification) {
            with(bin) {
                content.text = setFormattedText(item.title, item.content)
                date.text = TimeUtils.formatTimeAgo(item.createdAt)
                image.load(item.imageUrl)
            }

        }
    }

    fun setFormattedText(title: String, content: String): SpannableString {
        val formattedText = SpannableString("$title $content").apply {
            setSpan(StyleSpan(Typeface.BOLD), 0, title.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            setSpan(
                ForegroundColorSpan(Color.BLACK),
                0,
                title.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            setSpan(
                ForegroundColorSpan(Color.GRAY),
                title.length + 1,
                length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        return formattedText
    }
}

class NotificationDiffCallback : DiffUtil.ItemCallback<Notification>() {
    override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
        return oldItem.createdAt == newItem.createdAt
                && oldItem.content == newItem.content
                && oldItem.title == newItem.title
                && oldItem.imageUrl == newItem.imageUrl
                && oldItem.status == newItem.status
    }
}
