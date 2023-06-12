package pe.fernanapps.shop.ui.main.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import pe.fernanapps.shop.R
import pe.fernanapps.shop.databinding.ItemPersonalityBinding

class ItemAction(
    val title: String,
    @DrawableRes val icon: Int,
    val action: (() -> Unit)? = null,
)


class ItemActionListAdapter(
    private val items: List<ItemAction>,
) : RecyclerView.Adapter<ItemActionListAdapter.ItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPersonalityBinding.inflate(inflater, parent, false)
        return ItemHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ItemHolder(private val binding: ItemPersonalityBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ItemAction) {

            binding.profileItemTitle.text = item.title
            binding.profileItemIcon.setBackgroundResource(item.icon)

            if (item.action == null) {
                (binding.root as CardView).setCardBackgroundColor(
                    ContextCompat.getColor(binding.root.context, R.color.grey_10)
                )
            }

            binding.root.setOnClickListener {
                item.action?.invoke()
            }
        }
    }
}
