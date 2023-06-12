package pe.fernanapps.shop.ui.main.cart

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pe.fernanapps.shop.databinding.ItemCategoryBinding
import pe.fernanapps.shop.domain.model.product.Category
import pe.fernanapps.shop.utils.UIUtils
import pe.fernanapps.shop.utils.load


class CategoryAdapter(private val onCategoryClick: (Category) -> Unit) :
    ListAdapter<Category, CategoryViewHolder>(CategoryDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            ItemCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val isEven = (position % 2 == 0)
        holder.bind(getItem(position), isEven, onCategoryClick)
    }
}

class CategoryDiffCallback : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.id == newItem.id
    }
}

class CategoryViewHolder(private val bin: ItemCategoryBinding) : RecyclerView.ViewHolder(bin.root) {
    fun bind(category: Category, isEven: Boolean, onCategoryClick: (Category) -> Unit) {
        with(bin) {
            root.setOnClickListener { onCategoryClick(category) }

            // IsEven Align parent of Title to : Right
            (categoryTitle.parent as LinearLayout).gravity =
                Gravity.CENTER_VERTICAL or if (isEven) {
                    Gravity.START
                } else {
                    Gravity.END
                }

            categoryTitle.text = category.title
            categoryDescription.text = category.description

            categoryImage.load(
                UIUtils.getImageUrl(category),
                null,
                0f,
                -1
            )
        }

    }

}

