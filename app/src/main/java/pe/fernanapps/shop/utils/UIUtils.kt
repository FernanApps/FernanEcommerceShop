package pe.fernanapps.shop.utils

import android.content.Context
import android.graphics.PorterDuff
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import pe.fernanapps.shop.R
import pe.fernanapps.shop.domain.model.order.OrderItem
import pe.fernanapps.shop.domain.model.product.Category
import pe.fernanapps.shop.domain.model.product.Offer
import pe.fernanapps.shop.domain.model.product.Product
import pe.fernanapps.shop.domain.model.product.ProductCart

object UIUtils {

    /**
        the images are loaded in App-write Storage, but for some strange reason it doesn't load,
        this repo is backup image test

     */
    fun getImageUrl(any: Any?): String {
        val bkFormatProduct = "https://raw.githubusercontent.com/FernanApps/FXEcommerceShopImages/main/products/%s.jpg"
        val bkFormatCategory = "https://raw.githubusercontent.com/FernanApps/FXEcommerceShopImages/main/categories/%s.jpg"
        return when(any){
            is Product -> bkFormatProduct.format(any.id)
            is ProductCart -> bkFormatProduct.format(any.id)

            is OrderItem -> bkFormatProduct.format(any.product?.id)
            is Offer -> bkFormatProduct.format(any.product.id)

            is Category -> bkFormatCategory.format(any.id)
            else -> "https://raw.githubusercontent.com"
        }


    }

    fun converterDp(context: Context, initialHeight: Int): Float {
        val heightDp = initialHeight - 50
        val scale = context.resources.displayMetrics.density
        return (heightDp * scale + 0.5f)
    }

    fun MenuItem.setIconColor(color: Int) {
        //val color = ContextCompat.getColor(context, colorRes)
        val icon = icon
        icon?.setColorFilter(color, PorterDuff.Mode.SRC_IN)
    }



    fun setTint(view: View, tintColor: Int){
        val backgroundDrawable = view.background
        val tintedDrawable = DrawableCompat.wrap(backgroundDrawable)
        DrawableCompat.setTint(tintedDrawable, tintColor)
        view.background = tintedDrawable
    }
    fun ImageView.setImageDrawable (@DrawableRes drawableRes: Int ){
        this.setImageDrawable(ContextCompat.getDrawable(this.context, drawableRes))
    }

    fun ImageView.setImageDrawable(@DrawableRes drawableRes: Int, tintColorRes: Int) {
        val drawable = ContextCompat.getDrawable(this.context, drawableRes)
        drawable ?: return
        tintColorRes.let {
            DrawableCompat.setTint(drawable, tintColorRes)
        }
        this.setImageDrawable(drawable)
    }


    fun ImageView.setImageDrawable(@DrawableRes drawableRes: Int, @ColorRes tintColorRes: Int? = null) {
        val drawable = ContextCompat.getDrawable(this.context, drawableRes)
        drawable ?: return
        tintColorRes?.let {
            val tintColor = ContextCompat.getColor(this.context, it)
            DrawableCompat.setTint(drawable, tintColor)
        }
        this.setImageDrawable(drawable)
    }


    fun getColorResourceId(context: Context, colorValue: Int): Int {
        val colorResId = context.resources.getIdentifier("color_$colorValue", "color", context.packageName)
        if (colorResId != 0) {
            return colorResId
        }
        throw IllegalArgumentException("Invalid color value: $colorValue")
    }

    @ColorInt
    fun Context.themeAccentColor() = themeColor(android.R.attr.colorAccent)


    @ColorInt
    fun Context.themeColor(@AttrRes attrRes: Int): Int = TypedValue()
        .apply { theme.resolveAttribute (attrRes, this, true) }
        .data



}