package pe.fernanapps.shop.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pe.fernanapps.shop.databinding.ItemMessageBinding
import pe.fernanapps.shop.domain.model.chat.Message
import pe.fernanapps.shop.utils.TimeUtils
import kotlin.math.abs

class MessagesListAdapter internal constructor(
    private val userId: String,
) :
    ListAdapter<Message, RecyclerView.ViewHolder>(MessageDiffCallback()) {

    private val holderTypeMessageReceived = 1
    private val holderTypeMessageSent = 2

    abstract inner class MessageViewHolder(private val bin: ItemMessageBinding) :
        RecyclerView.ViewHolder(bin.root) {
        fun bind(message: Message, isSend: Boolean) {
            with(bin) {

                timeText.text = TimeUtils.time(message.epochTimeMs)
                timeText.isVisible = shouldMessageShowTimeText(currentList, message)

                if (isSend) {
                    messageTextRight
                } else {
                    messageTextLeft
                }.apply {
                    this.isVisible = true
                    this.text = message.text
                }

            }

        }
    }

    fun shouldMessageShowTimeText(messagesList:List<Message>, message: Message): Boolean {
        val halfHourInMilli = 1800000
        val index = messagesList.indexOf(message)

        return if (index == 0) {
            true
        } else {
            val messageBefore = messagesList[index - 1]
            abs(messageBefore.epochTimeMs - message.epochTimeMs) > halfHourInMilli
        }
    }


    inner class ReceivedViewHolder(bin: ItemMessageBinding) : MessageViewHolder(bin)

    inner class SentViewHolder(bin: ItemMessageBinding) : MessageViewHolder(bin)

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).senderId != userId) {
            holderTypeMessageReceived
        } else {
            holderTypeMessageSent
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            holderTypeMessageSent -> (holder as SentViewHolder).bind(getItem(position), isSend = true)
            holderTypeMessageReceived -> (holder as ReceivedViewHolder).bind(getItem(position), isSend = false)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val bin = ItemMessageBinding.inflate(layoutInflater, parent, false)

        return when (viewType) {
            holderTypeMessageSent -> SentViewHolder(bin)
            holderTypeMessageReceived -> ReceivedViewHolder(bin)
            else ->  throw Exception("Error reading holder type")

        }
    }
}


class MessageDiffCallback : DiffUtil.ItemCallback<Message>() {
    override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem.epochTimeMs == newItem.epochTimeMs
    }
}