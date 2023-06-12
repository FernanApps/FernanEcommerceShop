package pe.fernan.stepper

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pe.simple.list.selector.R
import pe.simple.list.selector.databinding.ItemStepperBinding

class StepperAdapter(private val currentSelected: String? = null, private val items: List<String>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var finalItems = mutableListOf<Stepper>()

    private var currentSelectedPosition: Int = 0

    private val VIEW_TYPE_STEP = 0
    private val VIEW_TYPE_LINE = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemStepperBinding.inflate(inflater, parent, false)

        return when (viewType) {
            VIEW_TYPE_STEP -> StepperViewHolder(binding)
            VIEW_TYPE_LINE -> StepperViewLineHolder(binding)
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = finalItems[position]
        when (holder) {
            is StepperViewHolder -> holder.bind(item)
            is StepperViewLineHolder -> holder.bind(item)

        }
    }

    override fun getItemCount(): Int {
        return finalItems.size
    }


    override fun getItemViewType(position: Int): Int {
        return if (isEven(position)) {
            VIEW_TYPE_STEP
        } else {
            VIEW_TYPE_LINE
        }
    }

    val disableColor = Color.parseColor("#a5a5a5")
    val selectedColor = Color.parseColor("#29a968")
    inner class StepperViewHolder(private val bin: ItemStepperBinding) :
        RecyclerView.ViewHolder(bin.root) {

        fun bind(item: Stepper) {
            with(bin) {
                title.text = item.title
                if (item.selected == null) {
                    icon.setBackgroundResource(R.drawable.ic_circle_disable)
                    title.setTextColor(disableColor)

                } else if (item.selected) {
                    icon.setBackgroundResource(R.drawable.ic_circle_selected)
                    title.setTextColor(selectedColor)


                } else {
                    icon.setBackgroundResource(R.drawable.ic_circle_unselected)
                    title.setTextColor(selectedColor)
                    title.setTypeface(null, Typeface.BOLD)


                }
            }

        }
    }

    inner class StepperViewLineHolder(private val bin: ItemStepperBinding) :
        RecyclerView.ViewHolder(bin.root) {

        fun bind(item: Stepper) {
            with(bin) {
                val layoutParams = bin.root.layoutParams
                layoutParams.height = 10
                bin.root.layoutParams = layoutParams
                title.text = ""
                if (item.selected == null) {
                    icon.backgroundTintList = null
                    icon.setBackgroundResource(R.drawable.ic_line)
                } else {
                    icon.setBackgroundResource(R.drawable.ic_line)
                    icon.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#29a968"))
                }
            }

        }
    }


    private fun isEven(number: Int): Boolean {
        return number % 2 == 0
    }

//    fun getNumberFromList(title: String?): Int? {
//        title ?: return null
//        val item = finalItems.find { it.second == title }
//        return item?.first
//    }

    init {
        var position = 0
        var positionSelected = 0
        for ((index, item) in items.withIndex()) {

            if (currentSelected.equals(item)) {
                positionSelected = position
            }
            finalItems.add(Stepper(position, item))

            if (index < items.size - 1) {
                position += 1
                finalItems.add(Stepper(position))
            }
            position += 1
        }
        // Fixing Selected :)
        println("data #positionSelected $positionSelected")
        val fixingSelectedList = finalItems
        finalItems = mutableListOf<Stepper>()
        fixingSelectedList.forEach {
            if (it.position == (positionSelected + 1) || it.position == (positionSelected + 2)) {
                finalItems.add(it.copy(selected = false))
            } else if (it.position > positionSelected) {
                finalItems.add(it.copy(selected = null))
            } else {
                finalItems.add(it)
            }
        }



        println("data :::: $finalItems")
        //currentSelectedPosition = getNumberFromList(currentSelected) ?: 0
    }
}