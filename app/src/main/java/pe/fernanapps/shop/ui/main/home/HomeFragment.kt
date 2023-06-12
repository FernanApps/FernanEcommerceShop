package pe.fernanapps.shop.ui.main.home

import android.content.Intent
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import pe.fernanapps.shop.Constants.ACTION_SHOW_CART_FRAGMENT
import pe.fernanapps.shop.databinding.FragmentHomeBinding
import pe.fernanapps.shop.domain.DataState
import pe.fernanapps.shop.domain.model.product.Product
import pe.fernanapps.shop.ui.base.BaseFragment
import pe.fernanapps.shop.ui.details.DetailsActivity
import pe.fernanapps.shop.ui.favorite.FavoriteViewModel
import pe.fernanapps.shop.utils.UIUtils.converterDp


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {


    private val viewModel by viewModels<HomeViewModel>()
    private val favoriteViewModel by viewModels<FavoriteViewModel>()

    private val offerAdapter: OfferAdapter by lazy {
        OfferAdapter {
            toDetails(it.product)
        }
    }


    private val productAdapter by lazy {
        ProductAdapter(
            onProductClick = {
                toDetails(it)
            },
            onProductLikeClick = {
                if (it.favorite) {
                    favoriteViewModel.delete(it)
                } else {
                    favoriteViewModel.insert(it)
                }
            }
        )
    }

    private fun toDetails(product: Product) {
        val intent = Intent(activity, DetailsActivity::class.java).apply {
            putExtra(ACTION_SHOW_CART_FRAGMENT, product)
        }
        activity?.startActivity(intent)
    }

    override fun initObserves() {

        viewModel.offers.observe(this) {
            when (it) {
                is DataState.Error -> Log.d("HomeFragment", "Error " + it.exception.message)
                is DataState.Finished -> Log.d("HomeFragment", "Finish ")
                is DataState.Loading -> Log.d("HomeFragment", "Loading ")
                is DataState.Success -> {
                    //Log.d("HomeFragment", it.data[0].title)
                    offerAdapter.submitList(it.data)
                }
                else -> Unit
            }
        }

        favoriteViewModel.productsWithFavorites.observe(this) {
            productAdapter.submitList(it)
        }


        viewModel.products.observe(this) {
            when (it) {
                is DataState.Error -> Log.d("HomeFragmentProducts", "Error " + it.exception.message)
                is DataState.Finished -> Log.d("HomeFragmentProducts", "Finish ")
                is DataState.Loading -> Log.d("HomeFragmentProducts", "Loading ")
                is DataState.Success -> {
                    //Log.d("HomeFragmentProducts", it.data[0].title)
                    productAdapter.submitList(it.data)
                    favoriteViewModel.setCurrentList(it.data)
                    favoriteViewModel.getAllFavoritesAndUpdateListWithFavorites()
                }
                else -> Unit
            }
        }
        viewModel.isTitleVisible.observe(this) { isVisible ->
            supportActionBar?.title = if (isVisible) {
                ""
            } else {
                getString(pe.fernanapps.shop.R.string.app_name)
            }
        }

    }

    override fun initViews() {
//        offerAdapter = OfferAdapter {
//            requireActivity().toast(it.title)
//        }
        with(bin) {
            recyclerViewOffers.adapter = offerAdapter
            recyclerViewProducts.adapter = productAdapter
            // Listener
//            bin.homeNestRoot.viewTreeObserver.addOnScrollChangedListener {
//                onScrollChange(bin.homeNestRoot)
//            }
        }
    }

    private fun onScrollChange(v: NestedScrollView) {

        val location = IntArray(2)
        bin.title.getLocationOnScreen(location)

        val y = location[1] - converterDp(requireActivity(), 100)
        val isVisible = (y >= 0 && y <= v.height)
        viewModel.setTitleVisible(isVisible)

    }


    override fun initActions() {
        with(bin) {
            filters.setOnClickListener {
                navController.navigate(pe.fernanapps.shop.R.id.action_navigation_page_home_to_navigation_page_categories)
            }
            categoryAll.setOnClickListener {
                navController.navigate(pe.fernanapps.shop.R.id.action_navigation_page_home_to_navigation_page_products)
            }


        }
    }

    private val onScrollChangedListener = object : ViewTreeObserver.OnScrollChangedListener {
        override fun onScrollChanged() {
            onScrollChange(bin.homeNestRoot)
        }
    }

    override fun onPause() {
        supportActionBar?.title = ""
        bin.homeNestRoot.viewTreeObserver.removeOnScrollChangedListener(onScrollChangedListener)
        supportActionBar?.hide()
        super.onPause()
    }


    override fun onResume() {
        bin.homeNestRoot.viewTreeObserver.addOnScrollChangedListener(onScrollChangedListener)
        supportActionBar?.show()
        super.onResume()

    }

}