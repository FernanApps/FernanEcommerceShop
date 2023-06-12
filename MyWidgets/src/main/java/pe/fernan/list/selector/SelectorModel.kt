package pe.fernan.list.selector
import android.graphics.Color

data class SelectorModel(
    val textString: String = "",
    val textColorSelected: Int = Color.WHITE,
    val textColorUnSelected: Int = Color.BLACK,
    val backgroundSelected: Int = Color.BLACK,
    val backgroundUnSelected: Int = Color.WHITE,
    var strokeColorUnSelected: Int = Color.GRAY,
    var strokeWidthUnSelected: Float = 1f,
)