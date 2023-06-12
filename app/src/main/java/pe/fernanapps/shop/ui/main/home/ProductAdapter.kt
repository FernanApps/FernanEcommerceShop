package pe.fernanapps.shop.ui.main.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import pe.fernanapps.shop.utils.UIUtils.setImageDrawable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pe.fernanapps.shop.R
import pe.fernanapps.shop.databinding.ItemProductBinding
import pe.fernanapps.shop.domain.model.product.Product
import pe.fernanapps.shop.utils.StringExt
import pe.fernanapps.shop.utils.UIUtils
import pe.fernanapps.shop.utils.UIUtils.themeAccentColor
import pe.fernanapps.shop.utils.UIUtils.themeColor
import pe.fernanapps.shop.utils.load



class ProductAdapter(
    private val onProductClick: (Product) -> Unit,
    private val onProductLikeClick: (Product) -> Unit,
) :
    ListAdapter<Product, ProductViewHolder>(ProductDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            ItemProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position), onProductClick, onProductLikeClick)
    }
}

class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id == newItem.id &&
                oldItem.category == newItem.category &&
                oldItem.price == newItem.price && oldItem.favorite == newItem.favorite
    }
}

class ProductViewHolder(private val bin: ItemProductBinding) : RecyclerView.ViewHolder(bin.root) {
    fun bind(
        product: Product,
        onProductClick: (Product) -> Unit,
        onProductLikeClick: (Product) -> Unit,
    ) {
        with(bin) {
            root.setOnClickListener { onProductClick(product) }
            productLike.setOnClickListener { onProductLikeClick(product) }

            productTitle.text = product.title
            productSubtitle.text = product.subtitle
            productPrice.text = StringExt.formatCurrency(product.price)

            productImage.load(UIUtils.getImageUrl(product))

            // Favorite
            if(product.favorite){
                productLike.setImageDrawable(R.drawable.ic_favourites, productLike.context.themeAccentColor())
            } else {
                productLike.setImageDrawable(R.drawable.ic_heart)

            }

        }

    }
}

