package pe.fernanapps.shop.ui.onboarding

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import pe.fernanapps.shop.R
import pe.fernanapps.shop.databinding.ActivityOnBoardingBinding
import pe.fernanapps.shop.ui.base.BaseActivity
import pe.fernanapps.shop.ui.signUp.SignUpActivity

class OnBoardingActivity : BaseActivity<ActivityOnBoardingBinding>(ActivityOnBoardingBinding::inflate) {
    private lateinit var dots: Array<TextView?>
    private var viewPageAdapter: ViewPageAdapter? = null

    override fun initViews() {
        viewPageAdapter = ViewPageAdapter(this)
        bin.slideViewPage.adapter = viewPageAdapter
        setupIndicatorDots(0)
        bin.slideViewPage.addOnPageChangeListener(viewListener)

    }

    override fun initActions() {
        with(bin) {
            imageButtonNext.setOnClickListener {
                if (getItem(0) < 2) {
                    bin.slideViewPage.setCurrentItem(getItem(1), true)
                } else {
                    val intent = Intent(this@OnBoardingActivity, SignUpActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    fun setupIndicatorDots(position: Int) {
        dots = arrayOfNulls<TextView>(3)
        bin.layoutDots.removeAllViews()
        for (i in dots.indices) {
            dots[i] = TextView(this)
            dots[i]?.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml("&#8226", Html.FROM_HTML_MODE_LEGACY)
            } else {
                Html.fromHtml("&#8226")
            }

            dots[i]?.textSize = 35f
            dots[i]?.setTextColor(ContextCompat.getColor(this, R.color.inactive))
            bin.layoutDots.addView(dots[i])
        }
        //&#8211
        //&#8722 ---
        //&#x2013 --
        dots[position]?.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml("&#8722", Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml("&#8722")
        }
        dots[position]?.setTextColor(ContextCompat.getColor(this, R.color.active))
    }

    private var viewListener: OnPageChangeListener = object : OnPageChangeListener {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {}

        override fun onPageSelected(position: Int) {
            setupIndicatorDots(position)
        }

        override fun onPageScrollStateChanged(state: Int) {}
    }

    private fun getItem(i: Int): Int {
        return bin.slideViewPage.currentItem + i
    }

    fun setBtnBack(view: View?) {
        if (getItem(0) > 0) {
            bin.slideViewPage.setCurrentItem(getItem(-1), true)
        }
    }



}