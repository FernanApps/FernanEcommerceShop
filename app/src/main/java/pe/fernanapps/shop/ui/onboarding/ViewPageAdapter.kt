package pe.fernanapps.shop.ui.onboarding

import android.content.Context
import android.content.res.TypedArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import pe.fernanapps.shop.R
import pe.fernanapps.shop.databinding.SliderLayoutBinding

class ViewPageAdapter(var context: Context) : PagerAdapter() {


    private val images: TypedArray
        get() = context.resources.obtainTypedArray(R.array.onboarding_images)

    //private val images
    //    get() = context.resources?.getIntArray(R.array.onboarding_images) ?: intArrayOf()

    private val headings
        get() = context.resources?.getStringArray(R.array.onboarding_headings)!!

    private val descriptions
        get() = context.resources?.getStringArray(R.array.onboarding_descriptions)!!


    override fun getCount(): Int {
        return headings.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater = LayoutInflater.from(container.context)
        val binding = SliderLayoutBinding.inflate(layoutInflater, container, false).apply {
            //image.setImageResource(images[position])
            image.setImageResource(images.getResourceId(position, R.drawable.onboarding_image1))

            title.text = headings[position]
            description.text = descriptions[position]
            container.addView(root)
        }
        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}