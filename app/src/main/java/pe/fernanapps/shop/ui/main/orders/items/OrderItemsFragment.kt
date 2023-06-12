package pe.fernanapps.shop.ui.main.orders.items

import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import pe.fernanapps.shop.R
import pe.fernanapps.shop.databinding.FragmentOrdersBinding
import pe.fernanapps.shop.mvvm.observe
import pe.fernanapps.shop.ui.base.BaseFragmentHideTopAndBottom
import pe.fernanapps.shop.ui.main.orders.OrderAdapter
import pe.fernanapps.shop.ui.main.orders.OrdersViewModel
import pe.fernanapps.shop.utils.TimeUtils

@AndroidEntryPoint
class OrderItemsFragment : BaseFragmentHideTopAndBottom<FragmentOrdersBinding>(FragmentOrdersBinding::inflate) {

    private val viewModel by viewModels<OrdersViewModel>()
    private val args: OrderItemsFragmentArgs by navArgs()

    private val adapter by lazy {
        OrderAdapter()
    }

    override fun initViews() {

        with(bin){

            ongoing.isVisible = false
            completed.isVisible = false

            base.title.text = getText(R.string.profile_personal_my_order)

            base.empty.root.isVisible = false
            base.loading.root.isVisible = false

            base.recycler.isVisible = true
            base.recycler.adapter = adapter

        }
    }

    override fun initObserves() {
        observe(viewModel.currentOrderDetails){
            //bin.title.text = "Order: \n${it.id}"
            setTitle(it.id)
            val createdAt = TimeUtils.convertTimestampToFormattedDate(it.createdAt)
            val status = "${it.status.status} : $createdAt"
            bin.ongoing.text = status
            bin.ongoing.isClickable = false
            bin.ongoing.isVisible = true
            adapter.submitList(it.ordersItems)
        }
        viewModel.setCurrentOrderDetailsAndObserve(args.argOrder)

    }

    private fun setTitle(orderId: String){
        val orderText = "Order:"

        val initialText = "$orderText \n$orderId"
        val numberText = initialText.substringAfter(":").trim()

        val orderStartIndex = 0
        val orderEndIndex = orderText.length
        val orderSpan = StyleSpan(Typeface.BOLD)
        val orderColorSpan = ForegroundColorSpan(Color.BLACK)

        val spannableStringBuilder = SpannableStringBuilder().apply {
            append(orderText)
            setSpan(orderSpan, orderStartIndex, orderEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            setSpan(orderColorSpan, orderStartIndex, orderEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            append("\n")

            val numberStartIndex = length
            val numberEndIndex = numberStartIndex + numberText.length
            val numberSpan = RelativeSizeSpan(0.7f)
            val colorSpan = ForegroundColorSpan(ContextCompat.getColor(_this, R.color.grey_400))
            append(numberText)
            setSpan(numberSpan, numberStartIndex, numberEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            setSpan(colorSpan, numberStartIndex, numberEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        }

         bin.base.title.text = spannableStringBuilder

    }

    override fun initActions() {
        bin.base.back.setOnClickListener {
            if (!navController.navigateUp()) {
                navController.navigate(R.id.navigation_page_orders)
            }
        }

    }



  }