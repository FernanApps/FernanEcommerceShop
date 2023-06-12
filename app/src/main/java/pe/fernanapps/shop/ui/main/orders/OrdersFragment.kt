package pe.fernanapps.shop.ui.main.orders

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import pe.fernanapps.shop.R
import pe.fernanapps.shop.databinding.FragmentOrdersBinding
import pe.fernanapps.shop.domain.DataState
import pe.fernanapps.shop.mvvm.observe
import pe.fernanapps.shop.ui.base.BaseFragmentHideTopAndBottom
import pe.fernanapps.shop.ui.main.categories.CategoriesFragmentDirections

@AndroidEntryPoint
class OrdersFragment : BaseFragmentHideTopAndBottom<FragmentOrdersBinding>(FragmentOrdersBinding::inflate) {
    private val viewModel by viewModels<OrdersViewModel>()

    private val adapter by lazy {
        OrderDetailsAdapter{
            val action = OrdersFragmentDirections.actionNavigationPageOrdersToNavigationPageOrdersItems(it)
            navController.navigate(action)
        }
    }

    override fun initObserves() {
        with(viewModel) {
            observe(ordersDetailsWithItemsFiltered){
                if(it.isEmpty()){
                    showView(true)
                } else {
                    adapter.submitList(it)
                    showView(false)
                }
            }

            observe(ordersDetailsWithItems) {
                println(":::::: ordersDetails $it ")
                when (it) {
                    is DataState.Error -> Unit
                    DataState.Finished -> Unit
                    DataState.Loading -> Unit
                    is DataState.Progress -> Unit
                    is DataState.Success -> {
                        setCurrentOrderDetailsList(it.data)
                        filteredListOrdersNotCompleted()
                    }
                }
            }
            getAllOrderDetailsWithItems()

        }
    }

    private fun showView(isEmpty: Boolean) {

        with(bin) {
            base.empty.root.isVisible = isEmpty
            base.empty.title.text = getString(R.string.you_dont_have_any_order)
            base.recycler.isVisible = !isEmpty
            base.loading.root.isVisible = false

        }
    }


    override fun initActions() {
        with(bin) {
            base.back.setOnClickListener {
                if (!navController.navigateUp()) {
                    navController.navigate(R.id.navigation_page_cart)
                }
            }

            completed.setOnClickListener {
                changeStyle(true)
                viewModel.filteredListOrdersCompleted()
            }

            ongoing.setOnClickListener {
                changeStyle(false)
                viewModel.filteredListOrdersNotCompleted()
            }


        }
    }

    override fun initViews() {
        with(bin) {
            base.recycler.adapter = adapter
        }
    }


    private fun changeSelected(textView: TextView) {
        textView.apply {
            setTextColor(ContextCompat.getColor(_this, R.color.blue_grey_50))
            setBackgroundResource(R.drawable.bg_round_gray3)
            backgroundTintList = ContextCompat.getColorStateList(context, R.color.black)
        }
    }

    private fun changeUnSelected(textView: TextView) {
        textView.apply {
            setTextColor(ContextCompat.getColor(_this, R.color.grey_500))
            backgroundTintList = null
            setBackgroundResource(R.drawable.corners_round_gray3)

        }
    }

    fun changeStyle(selected: Boolean) {
        if (selected) {
            changeSelected(bin.completed)
            changeUnSelected(bin.ongoing)

        } else {
            changeUnSelected(bin.completed)
            changeSelected(bin.ongoing)
        }
    }


}