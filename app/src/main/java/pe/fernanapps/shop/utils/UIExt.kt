package pe.fernanapps.shop.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader.TileMode
import android.net.Uri
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapResource
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import pe.fernanapps.shop.R
import java.security.MessageDigest


fun Fragment.toast(@StringRes text: Int, length: Int = Toast.LENGTH_SHORT) {
    activity ?: return
    toast(activity!!, text, length)
}

fun Fragment.toast(text: String?, length: Int = Toast.LENGTH_SHORT) {
    activity ?: return
    toast(activity!!, text, length)
}

fun Activity.toast(@StringRes text: Int, length: Int = Toast.LENGTH_SHORT) {
    toast(this, text, length)
}

fun Activity.toast(text: String?, length: Int = Toast.LENGTH_SHORT) {
    toast(this, text, length)
}


fun toast(context: Context, text: String?, length: Int) {
    text ?: return
    Toast.makeText(context, text, length).show()
}

fun toast(context: Context, @StringRes text: Int, length: Int) {
    Toast.makeText(context, text, length).show()
}


fun ImageView.load(url: String, backupUrl: String?, bitmapTransformation: BitmapTransformation) {
    val requestOptions = RequestOptions()
        .transform(CenterCrop())
        .transform(bitmapTransformation)

    this.scaleType = ImageView.ScaleType.FIT_XY

    var glide = Glide.with(this.context)
        .load(url)
        .apply(requestOptions)

    if (backupUrl != null) {
        glide = glide.error(
            this.load(
                url = backupUrl,
                null,
                bitmapTransformation = bitmapTransformation
            )
        )
    }
    glide.into(this)
}

fun ImageView.load(
    url: String,
    backupUrl: String? = null,
    zoom: Float = 1.5f,
    placeholder: Int? = null,
) {
    if (url.isNotEmpty()) {
        var glide = Glide.with(this.context).load(url)

        glide = if (placeholder == null) {
            glide.placeholder(R.drawable.product_empty)
        } else if (placeholder > 0) {
            glide.placeholder(placeholder)
        } else {
            glide
        }

        glide = if (zoom > 0f) {
            glide
                .thumbnail(0.25f)
                .transform(ZoomTransformation(zoom))
                .fitCenter()
        } else {
            glide
                .thumbnail(0.25f)
        }

        if (backupUrl != null) {
            glide = glide.error(this.load(url = backupUrl, backupUrl = null, zoom = zoom, placeholder = placeholder))
        }
        glide.into(this)

    }
}

fun ImageView.loadURI(url: Uri) {
    Glide.with(this.context).load(url).into(this)
}


//class ZoomTransformation(private val zoomFactor: Float) : BitmapTransformation() {
//
//    private val id = "com.example.app.ZoomTransformation"
//    private val idBytes = id.toByteArray(Charsets.UTF_8)
//
//    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
//        val bitmap = pool.get(toTransform.width, toTransform.height, Bitmap.Config.ARGB_8888)
//        val canvas = Canvas(bitmap)
//        val paint = Paint().apply {
//            isAntiAlias = true
//            shader = BitmapShader(toTransform, TileMode.CLAMP, TileMode.CLAMP)
//        }
//
//        // Define the rectangle for the section of the bitmap that you want to zoom
//        val zoomRect = RectF(
//            toTransform.width / 3f, // left
//            toTransform.height / 3f, // top
//            toTransform.width / 3f * 2, // right
//            toTransform.height / 3f * 2 // bottom
//        )
//
//        // Apply the zoom factor to the matrix
//        val matrix = Matrix().apply {
//            postScale(zoomFactor, zoomFactor, zoomRect.centerX(), zoomRect.centerY())
//        }
//
//        // Draw the zoomed section to the canvas
//        canvas.drawBitmap(toTransform, matrix, paint)
//
//        // Recycle the original bitmap
//        pool.put(toTransform)
//
//        return bitmap
//    }
//
//    override fun equals(other: Any?): Boolean {
//        return other is ZoomTransformation
//    }
//
//    override fun hashCode(): Int {
//        return id.hashCode()
//    }
//
//    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
//        messageDigest.update(idBytes)
//    }
//}


class ZoomTransformation(private val zoom: Float) : BitmapTransformation() {
    private val id = "com.example.app.ZoomTransformation.$zoom"
    private val idBytes = id.toByteArray(com.bumptech.glide.load.Key.CHARSET)

    override fun transform(
        pool: BitmapPool,
        source: Bitmap,
        outWidth: Int,
        outHeight: Int,
    ): Bitmap {
        val matrix = Matrix()
        matrix.postScale(zoom, zoom)

        val result = pool.get(source.width, source.height, source.config)
        val canvas = Canvas(result)
        canvas.drawBitmap(source, matrix, null)

        return result
    }

    override fun equals(other: Any?): Boolean {
        return other is ZoomTransformation && other.zoom == zoom
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(idBytes)
    }
}

class RoundedCornersTransformation2 @JvmOverloads constructor(
    private val mBitmapPool: BitmapPool, private val mRadius: Int, margin: Int,
    cornerType: CornerType = CornerType.ALL,
) :
    Transformation<Bitmap?> {
    enum class CornerType {
        ALL, TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT, TOP, BOTTOM, LEFT, RIGHT, OTHER_TOP_LEFT, OTHER_TOP_RIGHT, OTHER_BOTTOM_LEFT, OTHER_BOTTOM_RIGHT, DIAGONAL_FROM_TOP_LEFT, DIAGONAL_FROM_TOP_RIGHT
    }

    private val mDiameter: Int
    private val mMargin: Int
    private val mCornerType: CornerType

    @JvmOverloads
    constructor(
        context: Context?, radius: Int, margin: Int,
        cornerType: CornerType = CornerType.ALL,
    ) : this(Glide.get(context!!).bitmapPool, radius, margin, cornerType) {
    }

    init {
        mDiameter = mRadius * 2
        mMargin = margin
        mCornerType = cornerType
    }


    override fun transform(
        context: Context,
        resource: Resource<Bitmap?>,
        outWidth: Int,
        outHeight: Int,
    ): Resource<Bitmap?> {
        val source = resource.get()
        val width = source.width
        val height = source.height
        var bitmap: Bitmap? = mBitmapPool[width, height, Bitmap.Config.ARGB_8888]
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        }
        val canvas = Canvas(bitmap!!)
        val paint = Paint()
        paint.isAntiAlias = true
        paint.shader = BitmapShader(source, TileMode.CLAMP, TileMode.CLAMP)
        drawRoundRect(canvas, paint, width.toFloat(), height.toFloat())
        return BitmapResource.obtain(bitmap, mBitmapPool)!!
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        TODO("Not yet implemented")
    }

    private fun drawRoundRect(canvas: Canvas, paint: Paint, width: Float, height: Float) {
        val right = width - mMargin
        val bottom = height - mMargin
        when (mCornerType) {
            CornerType.ALL -> canvas.drawRoundRect(
                RectF(
                    mMargin.toFloat(),
                    mMargin.toFloat(),
                    right,
                    bottom
                ), mRadius.toFloat(), mRadius.toFloat(), paint
            )

            CornerType.TOP_LEFT -> drawTopLeftRoundRect(canvas, paint, right, bottom)
            CornerType.TOP_RIGHT -> drawTopRightRoundRect(canvas, paint, right, bottom)
            CornerType.BOTTOM_LEFT -> drawBottomLeftRoundRect(canvas, paint, right, bottom)
            CornerType.BOTTOM_RIGHT -> drawBottomRightRoundRect(canvas, paint, right, bottom)
            CornerType.TOP -> drawTopRoundRect(canvas, paint, right, bottom)
            CornerType.BOTTOM -> drawBottomRoundRect(canvas, paint, right, bottom)
            CornerType.LEFT -> drawLeftRoundRect(canvas, paint, right, bottom)
            CornerType.RIGHT -> drawRightRoundRect(canvas, paint, right, bottom)
            CornerType.OTHER_TOP_LEFT -> drawOtherTopLeftRoundRect(canvas, paint, right, bottom)
            CornerType.OTHER_TOP_RIGHT -> drawOtherTopRightRoundRect(canvas, paint, right, bottom)
            CornerType.OTHER_BOTTOM_LEFT -> drawOtherBottomLeftRoundRect(
                canvas,
                paint,
                right,
                bottom
            )

            CornerType.OTHER_BOTTOM_RIGHT -> drawOtherBottomRightRoundRect(
                canvas,
                paint,
                right,
                bottom
            )

            CornerType.DIAGONAL_FROM_TOP_LEFT -> drawDiagonalFromTopLeftRoundRect(
                canvas,
                paint,
                right,
                bottom
            )

            CornerType.DIAGONAL_FROM_TOP_RIGHT -> drawDiagonalFromTopRightRoundRect(
                canvas,
                paint,
                right,
                bottom
            )

            else -> canvas.drawRoundRect(
                RectF(mMargin.toFloat(), mMargin.toFloat(), right, bottom),
                mRadius.toFloat(),
                mRadius.toFloat(),
                paint
            )
        }
    }

    private fun drawTopLeftRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(
            RectF(
                mMargin.toFloat(),
                mMargin.toFloat(),
                (mMargin + mDiameter).toFloat(),
                (mMargin + mDiameter).toFloat()
            ),
            mRadius.toFloat(), mRadius.toFloat(), paint
        )
        canvas.drawRect(
            RectF(
                mMargin.toFloat(),
                (mMargin + mRadius).toFloat(),
                (mMargin + mRadius).toFloat(),
                bottom
            ), paint
        )
        canvas.drawRect(
            RectF((mMargin + mRadius).toFloat(), mMargin.toFloat(), right, bottom),
            paint
        )
    }

    private fun drawTopRightRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(
            RectF(right - mDiameter, mMargin.toFloat(), right, (mMargin + mDiameter).toFloat()),
            mRadius.toFloat(),
            mRadius.toFloat(),
            paint
        )
        canvas.drawRect(RectF(mMargin.toFloat(), mMargin.toFloat(), right - mRadius, bottom), paint)
        canvas.drawRect(RectF(right - mRadius, (mMargin + mRadius).toFloat(), right, bottom), paint)
    }

    private fun drawBottomLeftRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(
            RectF(mMargin.toFloat(), bottom - mDiameter, (mMargin + mDiameter).toFloat(), bottom),
            mRadius.toFloat(), mRadius.toFloat(), paint
        )
        canvas.drawRect(
            RectF(
                mMargin.toFloat(),
                mMargin.toFloat(),
                (mMargin + mDiameter).toFloat(),
                bottom - mRadius
            ), paint
        )
        canvas.drawRect(
            RectF((mMargin + mRadius).toFloat(), mMargin.toFloat(), right, bottom),
            paint
        )
    }

    private fun drawBottomRightRoundRect(
        canvas: Canvas,
        paint: Paint,
        right: Float,
        bottom: Float,
    ) {
        canvas.drawRoundRect(
            RectF(right - mDiameter, bottom - mDiameter, right, bottom), mRadius.toFloat(),
            mRadius.toFloat(), paint
        )
        canvas.drawRect(RectF(mMargin.toFloat(), mMargin.toFloat(), right - mRadius, bottom), paint)
        canvas.drawRect(RectF(right - mRadius, mMargin.toFloat(), right, bottom - mRadius), paint)
    }

    private fun drawTopRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(
            RectF(mMargin.toFloat(), mMargin.toFloat(), right, (mMargin + mDiameter).toFloat()),
            mRadius.toFloat(),
            mRadius.toFloat(),
            paint
        )
        canvas.drawRect(
            RectF(mMargin.toFloat(), (mMargin + mRadius).toFloat(), right, bottom),
            paint
        )
    }

    private fun drawBottomRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(
            RectF(mMargin.toFloat(), bottom - mDiameter, right, bottom),
            mRadius.toFloat(),
            mRadius.toFloat(),
            paint
        )
        canvas.drawRect(RectF(mMargin.toFloat(), mMargin.toFloat(), right, bottom - mRadius), paint)
    }

    private fun drawLeftRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(
            RectF(mMargin.toFloat(), mMargin.toFloat(), (mMargin + mDiameter).toFloat(), bottom),
            mRadius.toFloat(),
            mRadius.toFloat(),
            paint
        )
        canvas.drawRect(
            RectF((mMargin + mRadius).toFloat(), mMargin.toFloat(), right, bottom),
            paint
        )
    }

    private fun drawRightRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(
            RectF(right - mDiameter, mMargin.toFloat(), right, bottom),
            mRadius.toFloat(),
            mRadius.toFloat(),
            paint
        )
        canvas.drawRect(RectF(mMargin.toFloat(), mMargin.toFloat(), right - mRadius, bottom), paint)
    }

    private fun drawOtherTopLeftRoundRect(
        canvas: Canvas,
        paint: Paint,
        right: Float,
        bottom: Float,
    ) {
        canvas.drawRoundRect(
            RectF(mMargin.toFloat(), bottom - mDiameter, right, bottom),
            mRadius.toFloat(),
            mRadius.toFloat(),
            paint
        )
        canvas.drawRoundRect(
            RectF(right - mDiameter, mMargin.toFloat(), right, bottom),
            mRadius.toFloat(),
            mRadius.toFloat(),
            paint
        )
        canvas.drawRect(
            RectF(
                mMargin.toFloat(),
                mMargin.toFloat(),
                right - mRadius,
                bottom - mRadius
            ), paint
        )
    }

    private fun drawOtherTopRightRoundRect(
        canvas: Canvas,
        paint: Paint,
        right: Float,
        bottom: Float,
    ) {
        canvas.drawRoundRect(
            RectF(mMargin.toFloat(), mMargin.toFloat(), (mMargin + mDiameter).toFloat(), bottom),
            mRadius.toFloat(),
            mRadius.toFloat(),
            paint
        )
        canvas.drawRoundRect(
            RectF(mMargin.toFloat(), bottom - mDiameter, right, bottom),
            mRadius.toFloat(),
            mRadius.toFloat(),
            paint
        )
        canvas.drawRect(
            RectF(
                (mMargin + mRadius).toFloat(),
                mMargin.toFloat(),
                right,
                bottom - mRadius
            ), paint
        )
    }

    private fun drawOtherBottomLeftRoundRect(
        canvas: Canvas,
        paint: Paint,
        right: Float,
        bottom: Float,
    ) {
        canvas.drawRoundRect(
            RectF(mMargin.toFloat(), mMargin.toFloat(), right, (mMargin + mDiameter).toFloat()),
            mRadius.toFloat(),
            mRadius.toFloat(),
            paint
        )
        canvas.drawRoundRect(
            RectF(right - mDiameter, mMargin.toFloat(), right, bottom),
            mRadius.toFloat(),
            mRadius.toFloat(),
            paint
        )
        canvas.drawRect(
            RectF(
                mMargin.toFloat(),
                (mMargin + mRadius).toFloat(),
                right - mRadius,
                bottom
            ), paint
        )
    }

    private fun drawOtherBottomRightRoundRect(
        canvas: Canvas, paint: Paint, right: Float,
        bottom: Float,
    ) {
        canvas.drawRoundRect(
            RectF(mMargin.toFloat(), mMargin.toFloat(), right, (mMargin + mDiameter).toFloat()),
            mRadius.toFloat(),
            mRadius.toFloat(),
            paint
        )
        canvas.drawRoundRect(
            RectF(mMargin.toFloat(), mMargin.toFloat(), (mMargin + mDiameter).toFloat(), bottom),
            mRadius.toFloat(),
            mRadius.toFloat(),
            paint
        )
        canvas.drawRect(
            RectF(
                (mMargin + mRadius).toFloat(),
                (mMargin + mRadius).toFloat(),
                right,
                bottom
            ), paint
        )
    }

    private fun drawDiagonalFromTopLeftRoundRect(
        canvas: Canvas, paint: Paint, right: Float,
        bottom: Float,
    ) {
        canvas.drawRoundRect(
            RectF(
                mMargin.toFloat(),
                mMargin.toFloat(),
                (mMargin + mDiameter).toFloat(),
                (mMargin + mDiameter).toFloat()
            ),
            mRadius.toFloat(), mRadius.toFloat(), paint
        )
        canvas.drawRoundRect(
            RectF(right - mDiameter, bottom - mDiameter, right, bottom), mRadius.toFloat(),
            mRadius.toFloat(), paint
        )
        canvas.drawRect(
            RectF(
                mMargin.toFloat(),
                (mMargin + mRadius).toFloat(),
                right - mDiameter,
                bottom
            ), paint
        )
        canvas.drawRect(
            RectF(
                (mMargin + mDiameter).toFloat(),
                mMargin.toFloat(),
                right,
                bottom - mRadius
            ), paint
        )
    }

    private fun drawDiagonalFromTopRightRoundRect(
        canvas: Canvas, paint: Paint, right: Float,
        bottom: Float,
    ) {
        canvas.drawRoundRect(
            RectF(right - mDiameter, mMargin.toFloat(), right, (mMargin + mDiameter).toFloat()),
            mRadius.toFloat(),
            mRadius.toFloat(),
            paint
        )
        canvas.drawRoundRect(
            RectF(mMargin.toFloat(), bottom - mDiameter, (mMargin + mDiameter).toFloat(), bottom),
            mRadius.toFloat(), mRadius.toFloat(), paint
        )
        canvas.drawRect(
            RectF(
                mMargin.toFloat(),
                mMargin.toFloat(),
                right - mRadius,
                bottom - mRadius
            ), paint
        )
        canvas.drawRect(
            RectF(
                (mMargin + mRadius).toFloat(),
                (mMargin + mRadius).toFloat(),
                right,
                bottom
            ), paint
        )
    }

    val id: String
        get() = ("RoundedTransformation(radius=" + mRadius + ", margin=" + mMargin + ", diameter="
                + mDiameter + ", cornerType=" + mCornerType.name + ")")
}


// https://github.com/dscoppelletti/glide-transformations/blob/main/example/src/main/java/jp/wasabeef/example/glide/MainAdapter.kt
class RoundedCornersTransformation @JvmOverloads constructor(
    private val radius: Int,
    margin: Int,
    cornerType: CornerType = CornerType.ALL,
) :
    BitmapTransformation() {
    enum class CornerType {
        ALL, TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT, TOP, BOTTOM, LEFT, RIGHT, OTHER_TOP_LEFT, OTHER_TOP_RIGHT, OTHER_BOTTOM_LEFT, OTHER_BOTTOM_RIGHT, DIAGONAL_FROM_TOP_LEFT, DIAGONAL_FROM_TOP_RIGHT
    }

    private val diameter: Int
    private val margin: Int
    private val cornerType: CornerType

    init {
        diameter = radius * 2
        this.margin = margin
        this.cornerType = cornerType
    }


    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap, outWidth: Int, outHeight: Int,
    ): Bitmap {
        val width = toTransform.width
        val height = toTransform.height
        val bitmap = pool[width, height, Bitmap.Config.ARGB_8888]
        bitmap.setHasAlpha(true)
        setCanvasBitmapDensity(toTransform, bitmap)
        val canvas = Canvas(bitmap)
        val paint = Paint()
        paint.isAntiAlias = true
        paint.shader = BitmapShader(toTransform, TileMode.CLAMP, TileMode.CLAMP)
        drawRoundRect(canvas, paint, width.toFloat(), height.toFloat())
        return bitmap
    }

    private fun setCanvasBitmapDensity(toTransform: Bitmap, canvasBitmap: Bitmap) {
        canvasBitmap.density = toTransform.density
    }

    private fun drawRoundRect(canvas: Canvas, paint: Paint, width: Float, height: Float) {
        val right = width - margin
        val bottom = height - margin
        when (cornerType) {
            CornerType.ALL -> canvas.drawRoundRect(
                RectF(
                    margin.toFloat(),
                    margin.toFloat(),
                    right,
                    bottom
                ), radius.toFloat(), radius.toFloat(), paint
            )

            CornerType.TOP_LEFT -> drawTopLeftRoundRect(canvas, paint, right, bottom)
            CornerType.TOP_RIGHT -> drawTopRightRoundRect(canvas, paint, right, bottom)
            CornerType.BOTTOM_LEFT -> drawBottomLeftRoundRect(canvas, paint, right, bottom)
            CornerType.BOTTOM_RIGHT -> drawBottomRightRoundRect(canvas, paint, right, bottom)
            CornerType.TOP -> drawTopRoundRect(canvas, paint, right, bottom)
            CornerType.BOTTOM -> drawBottomRoundRect(canvas, paint, right, bottom)
            CornerType.LEFT -> drawLeftRoundRect(canvas, paint, right, bottom)
            CornerType.RIGHT -> drawRightRoundRect(canvas, paint, right, bottom)
            CornerType.OTHER_TOP_LEFT -> drawOtherTopLeftRoundRect(canvas, paint, right, bottom)
            CornerType.OTHER_TOP_RIGHT -> drawOtherTopRightRoundRect(canvas, paint, right, bottom)
            CornerType.OTHER_BOTTOM_LEFT -> drawOtherBottomLeftRoundRect(
                canvas,
                paint,
                right,
                bottom
            )

            CornerType.OTHER_BOTTOM_RIGHT -> drawOtherBottomRightRoundRect(
                canvas,
                paint,
                right,
                bottom
            )

            CornerType.DIAGONAL_FROM_TOP_LEFT -> drawDiagonalFromTopLeftRoundRect(
                canvas,
                paint,
                right,
                bottom
            )

            CornerType.DIAGONAL_FROM_TOP_RIGHT -> drawDiagonalFromTopRightRoundRect(
                canvas,
                paint,
                right,
                bottom
            )

            else -> canvas.drawRoundRect(
                RectF(margin.toFloat(), margin.toFloat(), right, bottom),
                radius.toFloat(),
                radius.toFloat(),
                paint
            )
        }
    }

    private fun drawTopLeftRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(
            RectF(
                margin.toFloat(),
                margin.toFloat(),
                (margin + diameter).toFloat(),
                (margin + diameter).toFloat()
            ), radius.toFloat(),
            radius.toFloat(), paint
        )
        canvas.drawRect(
            RectF(
                margin.toFloat(),
                (margin + radius).toFloat(),
                (margin + radius).toFloat(),
                bottom
            ), paint
        )
        canvas.drawRect(RectF((margin + radius).toFloat(), margin.toFloat(), right, bottom), paint)
    }

    private fun drawTopRightRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(
            RectF(right - diameter, margin.toFloat(), right, (margin + diameter).toFloat()),
            radius.toFloat(),
            radius.toFloat(),
            paint
        )
        canvas.drawRect(RectF(margin.toFloat(), margin.toFloat(), right - radius, bottom), paint)
        canvas.drawRect(RectF(right - radius, (margin + radius).toFloat(), right, bottom), paint)
    }

    private fun drawBottomLeftRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(
            RectF(margin.toFloat(), bottom - diameter, (margin + diameter).toFloat(), bottom),
            radius.toFloat(),
            radius.toFloat(),
            paint
        )
        canvas.drawRect(
            RectF(
                margin.toFloat(),
                margin.toFloat(),
                (margin + diameter).toFloat(),
                bottom - radius
            ), paint
        )
        canvas.drawRect(RectF((margin + radius).toFloat(), margin.toFloat(), right, bottom), paint)
    }

    private fun drawBottomRightRoundRect(
        canvas: Canvas,
        paint: Paint,
        right: Float,
        bottom: Float,
    ) {
        canvas.drawRoundRect(
            RectF(right - diameter, bottom - diameter, right, bottom), radius.toFloat(),
            radius.toFloat(), paint
        )
        canvas.drawRect(RectF(margin.toFloat(), margin.toFloat(), right - radius, bottom), paint)
        canvas.drawRect(RectF(right - radius, margin.toFloat(), right, bottom - radius), paint)
    }

    private fun drawTopRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(
            RectF(margin.toFloat(), margin.toFloat(), right, (margin + diameter).toFloat()),
            radius.toFloat(),
            radius.toFloat(),
            paint
        )
        canvas.drawRect(RectF(margin.toFloat(), (margin + radius).toFloat(), right, bottom), paint)
    }

    private fun drawBottomRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(
            RectF(margin.toFloat(), bottom - diameter, right, bottom),
            radius.toFloat(),
            radius.toFloat(),
            paint
        )
        canvas.drawRect(RectF(margin.toFloat(), margin.toFloat(), right, bottom - radius), paint)
    }

    private fun drawLeftRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(
            RectF(margin.toFloat(), margin.toFloat(), (margin + diameter).toFloat(), bottom),
            radius.toFloat(),
            radius.toFloat(),
            paint
        )
        canvas.drawRect(RectF((margin + radius).toFloat(), margin.toFloat(), right, bottom), paint)
    }

    private fun drawRightRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(
            RectF(right - diameter, margin.toFloat(), right, bottom),
            radius.toFloat(),
            radius.toFloat(),
            paint
        )
        canvas.drawRect(RectF(margin.toFloat(), margin.toFloat(), right - radius, bottom), paint)
    }

    private fun drawOtherTopLeftRoundRect(
        canvas: Canvas,
        paint: Paint,
        right: Float,
        bottom: Float,
    ) {
        canvas.drawRoundRect(
            RectF(margin.toFloat(), bottom - diameter, right, bottom),
            radius.toFloat(),
            radius.toFloat(),
            paint
        )
        canvas.drawRoundRect(
            RectF(right - diameter, margin.toFloat(), right, bottom),
            radius.toFloat(),
            radius.toFloat(),
            paint
        )
        canvas.drawRect(
            RectF(margin.toFloat(), margin.toFloat(), right - radius, bottom - radius),
            paint
        )
    }

    private fun drawOtherTopRightRoundRect(
        canvas: Canvas,
        paint: Paint,
        right: Float,
        bottom: Float,
    ) {
        canvas.drawRoundRect(
            RectF(margin.toFloat(), margin.toFloat(), (margin + diameter).toFloat(), bottom),
            radius.toFloat(),
            radius.toFloat(),
            paint
        )
        canvas.drawRoundRect(
            RectF(margin.toFloat(), bottom - diameter, right, bottom),
            radius.toFloat(),
            radius.toFloat(),
            paint
        )
        canvas.drawRect(
            RectF(
                (margin + radius).toFloat(),
                margin.toFloat(),
                right,
                bottom - radius
            ), paint
        )
    }

    private fun drawOtherBottomLeftRoundRect(
        canvas: Canvas,
        paint: Paint,
        right: Float,
        bottom: Float,
    ) {
        canvas.drawRoundRect(
            RectF(margin.toFloat(), margin.toFloat(), right, (margin + diameter).toFloat()),
            radius.toFloat(),
            radius.toFloat(),
            paint
        )
        canvas.drawRoundRect(
            RectF(right - diameter, margin.toFloat(), right, bottom),
            radius.toFloat(),
            radius.toFloat(),
            paint
        )
        canvas.drawRect(
            RectF(
                margin.toFloat(),
                (margin + radius).toFloat(),
                right - radius,
                bottom
            ), paint
        )
    }

    private fun drawOtherBottomRightRoundRect(
        canvas: Canvas, paint: Paint, right: Float,
        bottom: Float,
    ) {
        canvas.drawRoundRect(
            RectF(margin.toFloat(), margin.toFloat(), right, (margin + diameter).toFloat()),
            radius.toFloat(),
            radius.toFloat(),
            paint
        )
        canvas.drawRoundRect(
            RectF(margin.toFloat(), margin.toFloat(), (margin + diameter).toFloat(), bottom),
            radius.toFloat(),
            radius.toFloat(),
            paint
        )
        canvas.drawRect(
            RectF(
                (margin + radius).toFloat(),
                (margin + radius).toFloat(),
                right,
                bottom
            ), paint
        )
    }

    private fun drawDiagonalFromTopLeftRoundRect(
        canvas: Canvas, paint: Paint, right: Float,
        bottom: Float,
    ) {
        canvas.drawRoundRect(
            RectF(
                margin.toFloat(),
                margin.toFloat(),
                (margin + diameter).toFloat(),
                (margin + diameter).toFloat()
            ), radius.toFloat(),
            radius.toFloat(), paint
        )
        canvas.drawRoundRect(
            RectF(right - diameter, bottom - diameter, right, bottom), radius.toFloat(),
            radius.toFloat(), paint
        )
        canvas.drawRect(
            RectF(
                margin.toFloat(),
                (margin + radius).toFloat(),
                right - radius,
                bottom
            ), paint
        )
        canvas.drawRect(
            RectF(
                (margin + radius).toFloat(),
                margin.toFloat(),
                right,
                bottom - radius
            ), paint
        )
    }

    private fun drawDiagonalFromTopRightRoundRect(
        canvas: Canvas, paint: Paint, right: Float,
        bottom: Float,
    ) {
        canvas.drawRoundRect(
            RectF(right - diameter, margin.toFloat(), right, (margin + diameter).toFloat()),
            radius.toFloat(),
            radius.toFloat(),
            paint
        )
        canvas.drawRoundRect(
            RectF(margin.toFloat(), bottom - diameter, (margin + diameter).toFloat(), bottom),
            radius.toFloat(),
            radius.toFloat(),
            paint
        )
        canvas.drawRect(
            RectF(margin.toFloat(), margin.toFloat(), right - radius, bottom - radius),
            paint
        )
        canvas.drawRect(
            RectF(
                (margin + radius).toFloat(),
                (margin + radius).toFloat(),
                right,
                bottom
            ), paint
        )
    }

    override fun toString(): String {
        return ("RoundedTransformation(radius=" + radius + ", margin=" + margin + ", diameter="
                + diameter + ", cornerType=" + cornerType.name + ")")
    }

    @Override
    override fun equals(o: Any?): Boolean {
        return if (o == null) {
            false
        } else {
            o is RoundedCornersTransformation &&
                    (o.radius == radius &&
                            (o).diameter == diameter &&
                            (o).margin == margin &&
                            (o).cornerType == cornerType);
        }

    }


    override fun hashCode(): Int {
        return ID.hashCode() + radius * 10000 + diameter * 1000 + margin * 100 + cornerType.ordinal * 10
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update((ID + radius + diameter + margin + cornerType).toByteArray(CHARSET))
    }


    companion object {
        private const val VERSION = 1
        private const val ID =
            "jp.wasabeef.glide.transformations.RoundedCornersTransformation." + VERSION
    }
}