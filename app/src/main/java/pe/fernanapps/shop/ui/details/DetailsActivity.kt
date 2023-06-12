package pe.fernanapps.shop.ui.details

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import pe.fernan.list.selector.*
import pe.fernanapps.shop.R
import pe.fernanapps.shop.databinding.ActivityDetailsBinding
import pe.fernanapps.shop.domain.model.product.Product
import pe.fernanapps.shop.ui.base.BaseActivity
import pe.fernanapps.shop.ui.main.MainActivity
import pe.fernanapps.shop.utils.UIUtils
import pe.fernanapps.shop.utils.UIUtils.setIconColor
import pe.fernanapps.shop.utils.UIUtils.themeAccentColor
import pe.fernanapps.shop.utils.load

@AndroidEntryPoint
class DetailsActivity : BaseActivity<ActivityDetailsBinding>(ActivityDetailsBinding::inflate) {

    private val viewModel by viewModels<DetailsViewModel>()

    /*
    private val _product: Product? by lazy {
         this@DetailsActivity.intent.getSerializableExtra(INTENT_PRODUCT_TO_DETAILS) as? Product
    }
    */

    // private val product get() = _product!!

    private val sizeAdapter by lazy {
        SelectorAdapter(spaceBetweenItemRight = 10) {
            viewModel.setProductSize(it)
        }
    }
    private val colorAdapter by lazy {
        SelectorAdapter(
            sizeTextViewInDp = 27f,
            spaceBetweenItemBottom = 10,
            iconWhenSelected = R.drawable.ic_check_24
        ) {
            viewModel.setProductColor(it)
        }
    }

    override fun initViews() {


        setSupportActionBar(bin.toolbar)

        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val customArrowBackDrawable = customIconDrawableWithBackground(
            iconDrawable = pe.fernan.list.selector.customIconDrawable(
                this,
                iconDrawable = R.drawable.ic_arrow_back,
                iconSizeInDp = 24,
                iconColor = Color.WHITE
            ),
            marginInsideIcon = 0,
            backgroundDrawable = circularDrawable(Color.BLACK)

        )
        supportActionBar?.setHomeAsUpIndicator(customArrowBackDrawable)

        bin.productSizeList.adapter = sizeAdapter
        //bin.productColorList.adapter = colorAdapter

    }
    /*
    open val price: Float,
    open val size: String,
    open val category: String,
    open val image: String
     */

    private fun setProduct(product: Product) {
        with(bin) {
            with(product) {
                productTitle.text = title
                productSubtitle.text = subtitle
                productDescription.text = description
                productImage.load(UIUtils.getImageUrl(product))
            }
        }
    }


    override fun initObserves() {
        with(bin) {
            with(viewModel) {
                product.observe(this@DetailsActivity) { product ->
                    setProduct(product)
                }

                sizeAdapterPositionSelected.observe(this@DetailsActivity) {
                    sizeAdapter.selected(it)

                }
                colorAdapterPositionSelected.observe(this@DetailsActivity) {
                    colorAdapter.selected(it)
                }

                productSizes.observe(this@DetailsActivity) {
                    if (it.isNotEmpty()) {
                        sizeAdapter.submitList(it)
                    } else {
                        productSizeLabel.visibility = View.INVISIBLE
                        productSizeList.visibility = View.INVISIBLE
                    }

                }

                productColors.observe(this@DetailsActivity) {
                    colorAdapter.submitList(it)
                }

                productAmount.observe(this@DetailsActivity) {
                    productAmountLayout.productAmount.text = it.toString()
                }

                productPrice.observe(this@DetailsActivity) {
                    bin.productPrice.text = String.format("$%.2f", it)
                }

                productInCart.observe(this@DetailsActivity) {
                    @DrawableRes val drawableCheckId: Int
                    if (it) {
                        val colorSelected = this@DetailsActivity.themeAccentColor()
                            //ContextCompat.getColor(this@DetailsActivity, R.color.orange_700)

                        addCart.text = getString(R.string.added_product_to_cart)
                        addCart.backgroundTintList = ColorStateList.valueOf(colorSelected)
                        drawableCheckId = R.drawable.ic_check_24
                        changeColorIconCart(colorSelected)

                    } else {
                        val colorUnSelected = Color.BLACK
                        addCart.text = getString(R.string.add_to_cart)
                        addCart.backgroundTintList = ColorStateList.valueOf(colorUnSelected)
                        drawableCheckId = R.drawable.ic_cart
                        changeColorIconCart(colorUnSelected)

                    }
                    val drawableCheck =
                        ContextCompat.getDrawable(this@DetailsActivity, drawableCheckId)
                    drawableCheck?.setTint(
                        ContextCompat.getColor(
                            this@DetailsActivity, R.color.white
                        )
                    )
                    addCart.setCompoundDrawablesWithIntrinsicBounds(drawableCheck, null, null, null)
                    addCart.compoundDrawablePadding = dpToPx(5f, this@DetailsActivity).toInt()

                }

            }


        }


    }

    override fun initActions() {
        with(bin) {
            productAmountLayout.productAmountAdd.setOnClickListener {
                viewModel.addProductAmount()
            }
            productAmountLayout.productAmountDiss.setOnClickListener {
                viewModel.descProductAmount()
            }
            addCart.setOnClickListener {
                viewModel.productAddOrDeleteInCart()
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        if (id == android.R.id.home) {
            super.onBackPressed()
            return true
        } else if (id == R.id.menu_cart) {
            this@DetailsActivity.finish()
            MainActivity.navigateCartFragment.set(true)

        }
        return super.onOptionsItemSelected(item)
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        super.onCreateOptionsMenu(menu)
//        menuInflater.inflate(R.menu.menu_cart, menu)
//        return true
//    }

    private var currentMenu: Menu? = null
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_cart, menu)
        currentMenu = menu


        return true
    }

    private fun changeColorIconCart(color: Int) {
        val cartMenuItem = currentMenu?.findItem(R.id.menu_cart)
        cartMenuItem?.setIconColor(color)
    }

}