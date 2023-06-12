package pe.fernan.list.selector

import android.graphics.Typeface
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView


class SelectorAdapter(
    private val sizeTextViewInDp: Float = 50f,
    private val spaceBetweenItemLeft: Int = 0,
    private val spaceBetweenItemRight: Int = 0,
    private val spaceBetweenItemTop: Int = 0,
    private val spaceBetweenItemBottom: Int = 0,
    @DrawableRes private val iconWhenSelected: Int? = null,
    private val onSelectorClick: (SelectorModel) -> Unit = {}
) :
    ListAdapter<SelectorModel, SelectorAdapter.SelectorViewHolder>(SelectorDiffCallback()) {

    private var oldIndex = -1
    private var newIndex = -1

    private var selectedUnique:Boolean? = null

    fun selected(position: Int){
        Log.d("Adapter","<ooo $position .... ${(position in currentList.indices)}")
        if(position in currentList.indices){
            selectedUnique = true
            newIndex = position
            notifyItemChanged(position)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectorViewHolder {
        val textView = TextView(parent.context).apply {
            val marginLayoutParams = ViewGroup.MarginLayoutParams(
                dpToPx(sizeTextViewInDp, parent.context).toInt(),
                dpToPx(sizeTextViewInDp, parent.context).toInt(),
            )
            setPadding(2, 2, 2, 2)
            marginLayoutParams.setMargins(
                spaceBetweenItemLeft,
                spaceBetweenItemTop,
                spaceBetweenItemRight,
                spaceBetweenItemBottom
            )
            layoutParams = marginLayoutParams

            gravity = Gravity.CENTER
            typeface = Typeface.DEFAULT_BOLD


        }
        return SelectorViewHolder(textView)

    }

    override fun onBindViewHolder(holder: SelectorViewHolder, position: Int) {
        holder.bind(getItem(position), onSelectorClick)

    }

    override fun submitList(list: MutableList<SelectorModel>?, commitCallback: Runnable?) {
        super.submitList(list, commitCallback)
    }


    inner class SelectorViewHolder(private val root: TextView) :
        RecyclerView.ViewHolder(root) {
        fun bind(model: SelectorModel, onOfferClick: (SelectorModel) -> Unit) {

            with(root) {
                with(model) {
                    fun selected() {
                        background = if (iconWhenSelected == null) {
                            circularDrawable(backgroundSelected)
                        } else {
                            customIconDrawableWithBackground(
                                iconDrawable = customIconDrawable(
                                    context,
                                    iconDrawable = ContextCompat.getDrawable(
                                        context,
                                        iconWhenSelected
                                    )!!,
                                    iconSizeInDp = (sizeTextViewInDp - 10).toInt(),
                                    iconColor = getColorVisible(backgroundSelected)
                                ),
                                marginInsideIcon = 0,
                                backgroundDrawable = circularDrawable(backgroundSelected)

                            )
                        }
                        setTextColor(textColorSelected)
                    }

                    fun unSelected() {
                        setTextColor(textColorUnSelected)
                        background = circularDrawableWithStroke(
                            backgroundUnSelected,
                            strokeColorUnSelected,
                            dpToPx(strokeWidthUnSelected, context)
                        )
                    }

                    setOnClickListener {
                        selected()
                        oldIndex = newIndex
                        newIndex = adapterPosition
                        if (oldIndex != -1 && oldIndex != adapterPosition) {
                            notifyItemChanged(oldIndex)
                        }
                        onOfferClick(getItem(adapterPosition))
                    }
                    text = textString

                    if(selectedUnique != null){
                        if(selectedUnique!!){

                            selected()
                            selectedUnique = null
                        }
                    } else {
                        unSelected()
                    }


                }


            }
        }
    }


}








