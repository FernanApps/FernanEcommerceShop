package pe.fernanapps.shop.ui.main.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pe.fernanapps.shop.databinding.ItemOrderDetailsBinding
import pe.fernanapps.shop.domain.model.order.OrderDetails
import pe.fernanapps.shop.domain.model.order.OrderStatus
import pe.fernanapps.shop.utils.StringExt
import pe.fernanapps.shop.utils.TimeUtils
import pe.fernanapps.shop.utils.UIUtils

class OrderDetailsAdapter constructor(private val onClick: (OrderDetails) -> Unit):
    ListAdapter<OrderDetails, OrderDetailsAdapter.OrderDetailsViewHolder>(OrderDetailsDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailsViewHolder {
        return OrderDetailsViewHolder(
            ItemOrderDetailsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: OrderDetailsViewHolder, position: Int) {
        holder.bind(getItem(position), onClick)
    }


    class OrderDetailsViewHolder(private val bin: ItemOrderDetailsBinding) :
        RecyclerView.ViewHolder(bin.root) {
        fun bind(item: OrderDetails, onClick: (OrderDetails) -> Unit) {
            with(bin) {

                bin.root.setOnClickListener{
                    onClick(item)
                }

                stepper.setItems(
                    item.status.status,
                    OrderStatus.values().filterNot { it == OrderStatus.CANCELLED }
                        .map { it.status }
                )
                val imageAdapter = ImageAdapter()
                productsImage.adapter = imageAdapter
                imageAdapter.submitList(item.ordersItems.map { UIUtils.getImageUrl(it.product) })

                orderAmount.text = StringExt.formatCurrency(item.total)
                orderNumber.text = item.id
                orderDate.text = TimeUtils.convertTimestampToFormattedDate(item.createdAt)
                orderDelivery.text = "------"

            }
        }
    }
}

class OrderDetailsDiffCallBack : DiffUtil.ItemCallback<OrderDetails>() {
    override fun areItemsTheSame(oldItem: OrderDetails, newItem: OrderDetails): Boolean {
        return oldItem.id == newItem.id && oldItem.createdAt == newItem.createdAt
    }

    override fun areContentsTheSame(oldItem: OrderDetails, newItem: OrderDetails): Boolean {
        return oldItem.id == newItem.id &&
                oldItem.createdAt == newItem.createdAt &&
                oldItem.ordersItems.size == newItem.ordersItems.size
    }
}