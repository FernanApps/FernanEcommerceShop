package pe.fernan.list.selector

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.util.DisplayMetrics
import android.util.TypedValue
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import kotlin.math.roundToInt


/*

            val baseColor = Color.parseColor("#028069")
            val brightColor: Int = ColorUtils.blendARGB(baseColor, Color.WHITE, 0.5f)
            val darkColor = ColorUtils.blendARGB(baseColor, Color.BLACK, 0.2f)
            val mutedColor: Int = ColorUtils.setAlphaComponent(baseColor, 128)
            val complementaryColor = ColorUtils.compositeColors(0xFFFF0000.toInt(), baseColor)
            val textColor = if (ColorUtils.calculateContrast(
                    baseColor,
                    Color.WHITE
                ) >= 4.5
            ) Color.WHITE else Color.BLACK


 */


fun getColorVisible(baseColor: Int) = if (ColorUtils.calculateContrast(
        baseColor,
        Color.WHITE
    ) >= 4.5
) Color.WHITE else Color.BLACK

fun customIconDrawable(
    context: Context,
    @DrawableRes iconDrawable: Int,
    iconSizeInDp: Int,
    iconColor: Int? = null
) = customIconDrawable(context,
    ContextCompat.getDrawable(context, iconDrawable)!!,
    iconSizeInDp, iconColor
)


fun customIconDrawable(
    context: Context,
    iconDrawable: Drawable,
    iconSizeInDp: Int,
    iconColor: Int? = null
): BitmapDrawable {
    val iconSize = dpToPx(iconSizeInDp.toFloat(), context).toInt()

    val bitmap = Bitmap.createBitmap(iconSize, iconSize, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    iconDrawable.apply {
        iconColor?.let { setTint(it) }
        //setTint(iconColor)
        setBounds(0, 0, iconSize, iconSize)
        draw(canvas)
    }


    return BitmapDrawable(context.resources, bitmap)
}

fun customIconDrawableWithBackground(
    iconDrawable: Drawable,
    marginInsideIcon: Int,
    backgroundDrawable: Drawable
): LayerDrawable {

    val layers = arrayOf<Drawable?>(backgroundDrawable, iconDrawable)
    val layerDrawable = LayerDrawable(layers)
    layerDrawable.setLayerInset(1, marginInsideIcon, marginInsideIcon, marginInsideIcon, marginInsideIcon)
    return layerDrawable
}


fun circularDrawableWithStroke(backgroundColor: Int, strokeColor: Int, strokeWidth: Float) =
    object : Drawable() {
        private val ovalRect = RectF()
        private val strokePaint = Paint().apply {
            style = Paint.Style.STROKE
            this.strokeWidth = strokeWidth
            color = strokeColor
            strokeJoin = Paint.Join.ROUND
            isAntiAlias = false
        }
        private val solidPaint = Paint().apply {
            color = backgroundColor
            style = Paint.Style.FILL
        }

        override fun draw(canvas: Canvas) {
            ovalRect.set(bounds)
            canvas.drawOval(ovalRect, strokePaint)
            ovalRect.inset(strokeWidth / 2, strokeWidth / 2)
            canvas.drawOval(ovalRect, solidPaint)
        }

        override fun setAlpha(alpha: Int) {
            strokePaint.alpha = alpha
            solidPaint.alpha = alpha
        }

        override fun setColorFilter(colorFilter: ColorFilter?) {
            strokePaint.colorFilter = colorFilter
            solidPaint.colorFilter = colorFilter
        }

        override fun getOpacity(): Int = PixelFormat.TRANSLUCENT
    }


/*fun circularDrawableWithStroke(backgroundColor: Int, strokeColor: Int, strokeWidth: Float) =
    ShapeDrawable(OvalShape()).apply {
        //setPadding(16, 16, 16, 16)
        val strokePaint = Paint()
        strokePaint.style = Paint.Style.STROKE
        strokePaint.strokeWidth = strokeWidth
        strokePaint.color = strokeColor


        val solidPaint = Paint()
        solidPaint.color = backgroundColor

        paint.strokeWidth = strokePaint.strokeWidth
        paint.color = strokePaint.color
        paint.style = strokePaint.style
        paint.isAntiAlias = true
        paint.flags = Paint.ANTI_ALIAS_FLAG

        paint.setShadowLayer(strokePaint.strokeWidth, 0f, 0f, strokePaint.color)
        paint.alpha = strokePaint.alpha
        paint.xfermode = strokePaint.xfermode

        paint.isAntiAlias = false

        //paint.setFillType(Path.FillType.FILL_AND_STROKE)

        paint.shader = null
        paint.maskFilter = null
        paint.colorFilter = null
        paint.pathEffect = null

        shape = this.shape // reasignar shape para actualizar cambios de padding

        // establecer paint para la parte sólida
        paint.color = solidPaint.color
        paint.style = Paint.Style.FILL

    }*/

fun circularDrawable(backgroundColor: Int) = ShapeDrawable(OvalShape()).apply {
    setPadding(16, 16, 16, 16)
    paint.color = backgroundColor
}

fun pxToDp(px: Float, context: Context): Int {
    val displayMetrics = context.resources.displayMetrics
    return (px / (displayMetrics.density / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
}

fun dpToPx(dip: Float, context: Context) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    dip,
    context.resources.displayMetrics
)