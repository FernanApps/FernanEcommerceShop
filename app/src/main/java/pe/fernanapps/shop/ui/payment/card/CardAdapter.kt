package pe.fernanapps.shop.ui.payment.card

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pe.fernanapps.shop.R
import pe.fernanapps.shop.databinding.ItemCardBinding
import pe.fernanapps.shop.domain.model.user.Card
import pe.fernanapps.shop.utils.StringExt

class CardAdapter : ListAdapter<Card, CardAdapter.CardViewHolder>(CardDiffCallback()) {

    private var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding).apply {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    setSelectedPosition(position)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = getItem(position)
        holder.bind(card, position)
    }

    inner class CardViewHolder(private val binding: ItemCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(card: Card, position: Int) {
            binding.brandIcon.setImageResource(getBrandIconResId(card.brand))
            binding.brand.text = StringExt.capitalizeFirstLetter(card.brand)
            binding.number.text = getFormattedCardNumber(card.number)
            binding.iconSelected.visibility = if (position == selectedPosition) View.VISIBLE else View.INVISIBLE
        }
    }

    private class CardDiffCallback : DiffUtil.ItemCallback<Card>() {
        override fun areItemsTheSame(oldItem: Card, newItem: Card): Boolean {
            return oldItem.number == newItem.number
        }

        override fun areContentsTheSame(oldItem: Card, newItem: Card): Boolean {
            return oldItem == newItem
        }
    }

    // Helper method to map card brand to the corresponding icon resource
    private fun getBrandIconResId(brand: String): Int {
        return if (brand.lowercase().contains("master")) {
            R.drawable.ic_card_master
        } else {
            R.drawable.ic_card_visa
        }
    }

    fun getCurrentSelected(): Card = getItem(selectedPosition) ?: getItem(0)
    // Helper method to format the card number
    private fun getFormattedCardNumber(number: String?): String {
        return "**** **** **** $number"
    }

    fun setSelectedPosition(position: Int) {
        val previousSelectedPosition = selectedPosition
        selectedPosition = position
        submitList(currentList.toMutableList()) // Notifica cambios en la lista
        notifyItemChanged(previousSelectedPosition)
        notifyItemChanged(selectedPosition)
    }
}
