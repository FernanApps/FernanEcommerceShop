package pe.fernanapps.shop.ui.main.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pe.fernanapps.shop.R
import pe.fernanapps.shop.databinding.ItemOrderBinding
import pe.fernanapps.shop.domain.model.order.OrderItem
import pe.fernanapps.shop.utils.StringExt
import pe.fernanapps.shop.utils.UIUtils
import pe.fernanapps.shop.utils.load


class OrderAdapter : ListAdapter<OrderItem, OrderAdapter.OrderViewHolder>(OrderDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        return OrderViewHolder(
            ItemOrderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    class OrderViewHolder(private val bin: ItemOrderBinding) : RecyclerView.ViewHolder(bin.root) {
        fun bind(item: OrderItem) {
            val resources = bin.root.resources
            with(bin) {
                if (item.product != null) {
                    productImage.load(UIUtils.getImageUrl(item))
                    productTitle.text = item.product!!.title
                    productSubtitle.text = item.product!!.subtitle
                    productPrice.text =
                        StringExt.formatCurrency((item.product!!.price * item.quantity))

                }
                productSize.text = resources.getString(R.string.format_size).format(item.size)
                productQuantity.text =
                    resources.getString(R.string.format_quantity).format(item.quantity)


            }
        }
    }
}


class OrderDiffCallback : DiffUtil.ItemCallback<OrderItem>() {
    override fun areItemsTheSame(oldItem: OrderItem, newItem: OrderItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: OrderItem, newItem: OrderItem): Boolean {
        return oldItem.id == newItem.id &&
                oldItem.product?.id == newItem.product?.id
    }
}