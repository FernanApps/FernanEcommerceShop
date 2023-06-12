package pe.fernanapps.shop.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pe.fernanapps.shop.databinding.ItemFavoriteBinding
import pe.fernanapps.shop.domain.model.product.Product
import pe.fernanapps.shop.ui.main.home.ProductDiffCallback
import pe.fernanapps.shop.utils.StringExt
import pe.fernanapps.shop.utils.UIUtils
import pe.fernanapps.shop.utils.load

class FavoriteAdapter(
    private val onProductClick: (Product) -> Unit,
) :
    ListAdapter<Product, FavoriteViewHolder>(ProductDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder(
            ItemFavoriteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(getItem(position), onProductClick)
    }
}


class FavoriteViewHolder(private val bin: ItemFavoriteBinding) : RecyclerView.ViewHolder(bin.root) {
    fun bind(
        product: Product,
        onProductClick: (Product) -> Unit,
    ) {
        with(bin) {
            root.setOnClickListener { onProductClick(product) }
            productTitle.text = product.title
            productSubtitle.text = product.subtitle
            productPrice.text = StringExt.formatCurrency(product.price)
            productImage.load(UIUtils.getImageUrl(product))

        }

    }
}

