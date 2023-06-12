package pe.fernanapps.shop.ui.main.cart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.util.Util
import pe.fernanapps.shop.databinding.ItemCartBinding
import pe.fernanapps.shop.domain.model.product.ProductCart
import pe.fernanapps.shop.utils.UIUtils
import pe.fernanapps.shop.utils.load


class CartAdapter(
    private val context: Context,
    private val onCartClick: (ProductCart) -> Unit,
    private val onDelete: (Int) -> Unit,
) :
    ListAdapter<ProductCart, CartAdapter.CartViewHolder>(CartDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        return CartViewHolder(
            ItemCartBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(getItem(position), onCartClick)
    }


    inner class CartViewHolder(private val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductCart, onCategoryClick: (ProductCart) -> Unit) {
            with(binding) {
                root.setOnClickListener { onCategoryClick(product) }
                println("CART_ADAPTER ${product.toString()}")

                productTitle.text = product.title
                productSubtitle.text = product.subtitle

                productAmountLayout.productAmountAdd.isInvisible = true
                productAmountLayout.productAmountDiss.isInvisible = true
                productAmountLayout.productAmount.text = product.amount.toString()
                productPrice.text = String.format("$%.2f", product.finalPrice())
                productImage.load(UIUtils.getImageUrl(product), null, 0f)
            }

        }
    }
}

class CartDiffCallback : DiffUtil.ItemCallback<ProductCart>() {
    override fun areItemsTheSame(oldItem: ProductCart, newItem: ProductCart): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ProductCart, newItem: ProductCart): Boolean {
        return (oldItem.id == newItem.id) &&
                (oldItem.amount == newItem.amount) &&
                (oldItem.sizeSelected == newItem.sizeSelected) &&
                (oldItem.color == newItem.color)
    }
}


class SwipeToDeleteCallback(
    private val context: Context,
    private val onDelete: (Int) -> Unit,
) : ItemTouchHelper.Callback() {

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
    ): Int {
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags(0, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder,
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        onDelete(position)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean,
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    override fun isLongPressDragEnabled(): Boolean {
        return false
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }
}


class AdapterListSwipe(context: Context, private val _items: MutableList<String>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), SwipeItemTouchHelper.SwipeHelperAdapter {

    private var items = mutableListOf<String>()
    private val items_swiped = mutableListOf<String>()
    private val ctx: Context


    init {
        this.items = _items as ArrayList<String>
        ctx = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }


    // Replace the contents of a view (invoked by the layout manager)


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                for (s in items_swiped) {
                    val index_removed = items.indexOf(s)
                    if (index_removed != -1) {
                        items.removeAt(index_removed)
                        notifyItemRemoved(index_removed)
                    }
                }
                items_swiped.clear()
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun onItemDismiss(position: Int) {

        // handle when double swipe
//        if (items[position].swiped) {
//            items_swiped.remove(items[position])
//            items.removeAt(position)
//            notifyItemRemoved(position)
//            return
//        }
//        items[position].swiped = true
//        items_swiped.add(items[position])
//        notifyItemChanged(position)
    }
}


// Swipe :)
class SwipeItemTouchHelper(adapter: SwipeHelperAdapter) :
    ItemTouchHelper.Callback() {
    private var bgColorCode = Color.LTGRAY
    private val mAdapter: SwipeHelperAdapter

    init {
        mAdapter = adapter
    }

    override fun isLongPressDragEnabled(): Boolean {
        return false
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
    ): Int {
        // Set movement flags based on the layout manager
        return if (recyclerView.layoutManager is GridLayoutManager) {
            val dragFlags =
                ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            val swipeFlags = 0
            makeMovementFlags(dragFlags, swipeFlags)
        } else {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
            makeMovementFlags(dragFlags, swipeFlags)
        }
    }

    override fun onMove(
        recyclerView: RecyclerView,
        source: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder,
    ): Boolean {
        return if (source.itemViewType != target.itemViewType) false else true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
        // Notify the adapter of the dismissal
        mAdapter.onItemDismiss(viewHolder.adapterPosition)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean,
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val itemView = viewHolder.itemView
            val background: Drawable = ColorDrawable()
            (background as ColorDrawable).color = getBgColorCode()
            if (dX > 0) { // swipe right
                background.setBounds(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
            } else { // swipe left
                background.setBounds(
                    itemView.right + dX.toInt(),
                    itemView.top,
                    itemView.right,
                    itemView.bottom
                )
            }
            background.draw(c!!)
        }
        super.onChildDraw(
            c!!,
            recyclerView!!, viewHolder, dX, dY, actionState, isCurrentlyActive
        )
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        // We only want the active item to change
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder is TouchViewHolder) {
                // Let the view holder know that this item is being moved or dragged
                val itemViewHolder = viewHolder as TouchViewHolder
                itemViewHolder.onItemSelected()
            }
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView!!, viewHolder)
        viewHolder.itemView.alpha = ALPHA_FULL
        if (viewHolder is TouchViewHolder) {
            // Tell the view holder it's time to restore the idle state
            val itemViewHolder = viewHolder as TouchViewHolder
            itemViewHolder.onItemClear()
        }
    }

    fun getBgColorCode(): Int {
        return bgColorCode
    }

    fun setBgColorCode(bgColorCode: Int) {
        this.bgColorCode = bgColorCode
    }

    interface SwipeHelperAdapter {
        fun onItemDismiss(position: Int)
    }

    interface TouchViewHolder {
        fun onItemSelected()
        fun onItemClear()
    }

    companion object {
        const val ALPHA_FULL = 1.0f
    }
}
