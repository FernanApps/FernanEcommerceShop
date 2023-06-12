package pe.fernanapps.shop.ui.main.products

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import pe.fernanapps.shop.Constants
import pe.fernanapps.shop.R
import pe.fernanapps.shop.databinding.FragmentProductsBinding
import pe.fernanapps.shop.domain.DataState
import pe.fernanapps.shop.domain.model.product.Product
import pe.fernanapps.shop.mvvm.observe
import pe.fernanapps.shop.ui.base.BaseFragmentHideTopAndBottom
import pe.fernanapps.shop.ui.details.DetailsActivity
import pe.fernanapps.shop.ui.favorite.FavoriteViewModel
import pe.fernanapps.shop.ui.main.home.ProductAdapter


@AndroidEntryPoint
class ProductsFragment : BaseFragmentHideTopAndBottom<FragmentProductsBinding>(FragmentProductsBinding::inflate) {

    private val viewModel by viewModels<ProductsViewModel>()
    private val favoriteViewModel by viewModels<FavoriteViewModel>()

    private val args: ProductsFragmentArgs by navArgs()



    private fun toDetails(it: Product){
        val intent = Intent(activity, DetailsActivity::class.java).apply {
            putExtra(Constants.ACTION_SHOW_CART_FRAGMENT, it)
        }
        activity?.startActivity(intent)
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


    private val tag = "ProductsFragment"
    override fun initObserves() {

        favoriteViewModel.productsWithFavorites.observe(this) {
            productAdapter.submitList(it)
        }

        with(viewModel) {


            observe(products) {
                productAdapter.submitList(it)
                showLoadMoreProducts(false)
                
                favoriteViewModel.setCurrentList(it)
                favoriteViewModel.getAllFavoritesAndUpdateListWithFavorites()

            }

            observe(productsFiltered) { list ->
                productAdapter.submitList(list)
            }

            observe(productsStatus) {
                when (it) {
                    is DataState.Error -> Log.d(tag, "Error " + it.exception.message)
                    is DataState.Finished -> Log.d(tag, "Finish ")
                    is DataState.Loading -> Log.d(tag, "Loading ")
                    is DataState.Success -> {
                        //Log.d(tag, it.data[0].title)
                        viewModel.addProducts(it.data)

                    }
                    else -> Unit
                }
            }

            observe(loadProducts) {
                if (it) {
                    showLoadMoreProducts(true)
                    getProducts(args.argCategoryId)

                }
            }


            // Init get products
            showLoadMoreProducts(true)
            getProducts(args.argCategoryId)
        }
    }

    private fun showLoadMoreProducts(isVisible: Boolean) {
        bin.loadMoreProducts.isVisible = isVisible
    }


    override fun initActions() {
        with(bin) {
            back.setOnClickListener {
                if(!navController.navigateUp()){
                    navController.navigate(R.id.navigation_page_home)
                }
            }
            searchIcon.setOnClickListener {
                search.isInvisible = (search.visibility != View.INVISIBLE)
                search.requestFocus()
            }
        }
    }

    val gridLayoutManager get() = bin.recyclerViewProducts.layoutManager as GridLayoutManager

    override fun initViews() {
        with(bin) {
            // Title
            title.text = args.argTitle

            recyclerViewProducts.adapter = productAdapter

            recyclerViewProducts.addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    viewModel.onScrolled(
                        dy,
                        gridLayoutManager.childCount,
                        gridLayoutManager.itemCount,
                        gridLayoutManager.findFirstVisibleItemPosition()
                    )
                }
            })

            search.addTextChangedListener {
                viewModel.filterProducts(it.toString())
            }

        }
    }




}