package pe.fernan.stepper

data class Stepper(
    val position: Int = -1,
    val title: String = "",
    val selected: Boolean? = true,
) {
    override fun toString(): String {
        return "Stepper(position=$position, title='$title', selected=$selected)"
    }
}