package pe.fernan.list.selector

import androidx.recyclerview.widget.DiffUtil

class SelectorDiffCallback : DiffUtil.ItemCallback<SelectorModel>() {
    override fun areItemsTheSame(oldItem: SelectorModel, newItem: SelectorModel): Boolean {
        return oldItem.textString == newItem.textString
    }

    override fun areContentsTheSame(oldItem: SelectorModel, newItem: SelectorModel): Boolean {
        return oldItem.textString == newItem.textString
    }
}