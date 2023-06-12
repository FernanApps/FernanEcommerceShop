package pe.fernanapps.shop.ui.main.cart

import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import dagger.hilt.android.AndroidEntryPoint
import pe.fernanapps.shop.Constants
import pe.fernanapps.shop.R
import pe.fernanapps.shop.databinding.FragmentCartBinding
import pe.fernanapps.shop.model.toProductBase
import pe.fernanapps.shop.mvvm.observe
import pe.fernanapps.shop.ui.base.BaseFragmentHideTopAndBottom
import pe.fernanapps.shop.ui.details.DetailsActivity
import pe.fernanapps.shop.ui.main.orders.OrdersViewModel
import pe.fernanapps.shop.ui.payment.PaymentFragment
import pe.fernanapps.shop.utils.StringExt
import pe.fernanapps.shop.utils.UIUtils
import pe.fernanapps.shop.utils.UIUtils.themeAccentColor
import java.util.concurrent.atomic.AtomicBoolean

@AndroidEntryPoint
class CartFragment : BaseFragmentHideTopAndBottom<FragmentCartBinding>(FragmentCartBinding::inflate) {

    override val hideBottomBar: Boolean get() = false

    private val viewModel by viewModels<CartViewModel>()
    private val ordersViewModel by viewModels<OrdersViewModel>()
    private val intervalColorViewModel by viewModels<IntervalColorViewModel>()

    private val cartAdapter by lazy {
        CartAdapter(context = requireActivity(), onCartClick = {
            val intent = Intent(activity, DetailsActivity::class.java).apply {
                putExtra(Constants.ACTION_SHOW_CART_FRAGMENT, it.toProductBase())
            }
            activity?.startActivity(intent)
        }, onDelete = {})
    }

    override fun initViews() {
        with(bin){
            recyclerProductsInCart.adapter = cartAdapter


            val swipeToDeleteCallback = SwipeToDeleteCallback(requireContext()) { position ->
                val item = cartAdapter.currentList[position]
                viewModel.deleteProduct(item)
            }

            val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
            itemTouchHelper.attachToRecyclerView(bin.recyclerProductsInCart)


        }
    }

    override fun initObserves() {
        with(bin){
            observe(intervalColorViewModel.currentColor){
                UIUtils.setTint(bin.badge.number, it)
            }


            observe(ordersViewModel.sizeOrdersDetails){
                if(it > 0){
                    badge.root.isVisible = true
                    badge.number.text = it.toString()
                    intervalColorViewModel.setFirstColor(_this.themeAccentColor())
                    intervalColorViewModel.setSecondColor(ContextCompat.getColor(_this,R.color.pink_600))
                    intervalColorViewModel.startColorChange()
                } else {
                    badge.root.isVisible = false
                    intervalColorViewModel.stopColorChange()
                }
            }


            with(viewModel){
                observe(products){
                    if(it.isNotEmpty()){
                        productNone.root.visibility = View.INVISIBLE
                        recyclerProductsInCart.visibility = View.VISIBLE
                        layoutCheckout.visibility = View.VISIBLE

                        cartAdapter.submitList(it)
                        // size
                        totalItems.text = getText(R.string.total_item).toString().format(it.size)

                    } else {
                        productNone.root.visibility = View.VISIBLE
                        recyclerProductsInCart.visibility = View.INVISIBLE
                        layoutCheckout.visibility = View.GONE

                    }
                }

                observe(priceTotal){
                    totalPrice.text = StringExt.formatCurrency(it)
                }
            }
            ordersViewModel.getSizeOrderDetails()
        }

    }

    /**
     * Test Service
     */


    override fun initActions() {
        with(bin){
            checkout.setOnClickListener {
                val action = CartFragmentDirections
                    .actionNavigationPageCartToNavigationPagePayment(viewModel.priceTotal.value ?: 0f)
                navController.navigate(action)
            }
            myOrders.setOnClickListener{
                navController.navigate(R.id.action_navigation_page_cart_to_navigation_page_orders)
            }

            


        }
    }

    override fun onPause() {
        super.onPause()
        intervalColorViewModel.stopColorChange()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getUltimateProductsInCart()
        if(IS_RECENT_PAYMENT.get()){
            IS_RECENT_PAYMENT.set(false)
            ordersViewModel.getSizeOrderDetails()
        }
        if(bin.badge.root.isVisible){
            intervalColorViewModel.startColorChange()
        }
    }

    companion object {
        val IS_RECENT_PAYMENT = AtomicBoolean(false)
    }

}