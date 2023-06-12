package pe.fernanapps.shop.ui.favorite

import android.content.Intent
import android.view.View
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import pe.fernanapps.shop.Constants
import pe.fernanapps.shop.R
import pe.fernanapps.shop.databinding.FragmentProductsBinding
import pe.fernanapps.shop.domain.model.product.Product
import pe.fernanapps.shop.mvvm.observe
import pe.fernanapps.shop.ui.base.BaseFragmentHideTopAndBottom
import pe.fernanapps.shop.ui.details.DetailsActivity
import pe.fernanapps.shop.ui.main.products.ProductsViewModel


@AndroidEntryPoint
class FavoriteFragment : BaseFragmentHideTopAndBottom<FragmentProductsBinding>(pe.fernanapps.shop.databinding.FragmentProductsBinding::inflate) {

    private val viewModel by viewModels<ProductsViewModel>()
    private val favoriteViewModel by viewModels<FavoriteViewModel>()

    private fun toDetails(it: Product){
        val intent = Intent(activity, DetailsActivity::class.java).apply {
            putExtra(Constants.ACTION_SHOW_CART_FRAGMENT, it)
        }
        activity?.startActivity(intent)
    }

    private val favoriteAdapter by lazy {
        FavoriteAdapter(
            onProductClick = {
                toDetails(it)
            }
        )
    }


    private val tag = "FavoriteFragmentTopAndBottom"
    override fun initObserves() {
        
        observe(favoriteViewModel.productsWithFavorites) {
            viewModel.addProducts(it)
        }

        with(viewModel) {

            observe(products) {
                favoriteAdapter.submitList(it)
                showListOrEmpty(it.isEmpty())
                showLoadMoreProducts(false)
            }

            observe(productsFiltered) {
                favoriteAdapter.submitList(it)
                showListOrEmpty(it.isEmpty())
            }

            showLoadMoreProducts(true)

            // init
            favoriteViewModel.getAllFavoritesAndUpdateListWithFavorites()
        }
    }

    private fun showListOrEmpty(isEmpty: Boolean) {
        bin.productNone.root.isVisible = isEmpty
        bin.recyclerViewProducts.isVisible = !isEmpty
    }

    private fun showLoadMoreProducts(isVisible: Boolean) {
        bin.loadMoreProducts.isVisible = isVisible
    }


    override fun initActions() {
        with(bin) {
            
            back.setOnClickListener {
                navController.navigateUp()
            }
            searchIcon.setOnClickListener {
                search.isInvisible = (search.visibility != View.INVISIBLE)
                search.requestFocus()
            }
        }
    }


    override fun initViews() {
        with(bin) {
            
            title.text = getString(R.string.profile_personal_my_favourites)

            recyclerViewProducts.layoutManager = LinearLayoutManager(_this, RecyclerView.VERTICAL, false)
            recyclerViewProducts.adapter = favoriteAdapter           

            search.addTextChangedListener {
                viewModel.filterProducts(it.toString())
            }

        }
    }




}

