package pe.fernanapps.shop.ui.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pe.fernanapps.shop.databinding.ItemOfferBinding
import pe.fernanapps.shop.domain.model.product.Offer
import pe.fernanapps.shop.utils.UIUtils
import pe.fernanapps.shop.utils.load


class OfferAdapter(private val onOfferClick: (Offer) -> Unit) : ListAdapter<Offer, OfferViewHolder>(OfferDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferViewHolder {
        return OfferViewHolder(
            ItemOfferBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: OfferViewHolder, position: Int) {
        holder.bind(getItem(position), onOfferClick)
    }
}

class OfferDiffCallback : DiffUtil.ItemCallback<Offer>() {
    override fun areItemsTheSame(oldItem: Offer, newItem: Offer): Boolean {
        return oldItem.product.id == newItem.product.id
    }

    override fun areContentsTheSame(oldItem: Offer, newItem: Offer): Boolean {
        return oldItem == newItem
    }
}

class OfferViewHolder(private val bin: ItemOfferBinding) : RecyclerView.ViewHolder(bin.root) {
    fun bind(offer: Offer, onOfferClick: (Offer) -> Unit) {
        with(bin) {
            root.setOnClickListener { onOfferClick(offer) }
            offerGetNow.setOnClickListener { root.performClick() }
            offerTitle.text = offer.title
            offerDescription.text = offer.description
            offerCode.text = offer.code
            offerImage.load(UIUtils.getImageUrl(offer))
        }

    }

}

