package pe.fernanapps.shop.widget

import com.google.android.material.shape.*

class TicketEdgeTreatment(
    private val size: Float
): EdgeTreatment() {
    override fun getEdgePath(
        length: Float,
        center: Float,
        interpolation: Float,
        shapePath: ShapePath
    ) {
        val circleRadius = size * interpolation
        shapePath.lineTo(center - circleRadius, 0f)
        shapePath.addArc(
            center - circleRadius, -circleRadius,
            center + circleRadius, circleRadius,
            180f,
            -180f
        )
        shapePath.lineTo(length, 0f)
    }
}

val ticketShapePathModel = ShapeAppearanceModel
    .Builder()
    .setAllCorners(CornerFamily.ROUNDED, 36f)
    .setLeftEdge(TicketEdgeTreatment(36f))
    .setRightEdge(TicketEdgeTreatment(36f))
    .build()

class TicketDrawable : MaterialShapeDrawable(ticketShapePathModel)