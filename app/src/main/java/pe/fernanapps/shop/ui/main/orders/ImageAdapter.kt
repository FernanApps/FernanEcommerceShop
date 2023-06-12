package pe.fernanapps.shop.ui.main.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pe.fernanapps.shop.databinding.ItemImageBinding
import pe.fernanapps.shop.utils.load

class ImageAdapter : ListAdapter<String, ImageAdapter.ImageViewHolder>(ImageDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemImageBinding.inflate(inflater, parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ImageViewHolder(private val bin: ItemImageBinding) : RecyclerView.ViewHolder(bin.root) {
        fun bind(imageUrl: String) {
            bin.productImage.load(imageUrl)
        }
    }
}

class ImageDiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}
