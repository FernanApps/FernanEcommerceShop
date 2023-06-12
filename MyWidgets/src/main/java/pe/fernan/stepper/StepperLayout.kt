package pe.fernan.stepper

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import pe.simple.list.selector.databinding.StepperLayoutBinding

class StepperLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var bin: StepperLayoutBinding
    private lateinit var adapter: StepperAdapter

    init {
        bin = StepperLayoutBinding.inflate(LayoutInflater.from(context), this, true)
        initViews()
    }

    fun setItems(currentSelected: String? = null, items: List<String>){
        adapter = StepperAdapter(currentSelected, items)
        with(bin) {
            recycler.layoutManager = LinearLayoutManager(context)
            recycler.adapter = adapter
        }
    }

    private fun initViews() {

    }
}







