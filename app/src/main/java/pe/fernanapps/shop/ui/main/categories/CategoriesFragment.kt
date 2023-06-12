package pe.fernanapps.shop.ui.main.categories

import android.util.Log
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import pe.fernanapps.shop.R
import pe.fernanapps.shop.databinding.FragmentCategoriesBinding
import pe.fernanapps.shop.domain.DataState
import pe.fernanapps.shop.mvvm.observe
import pe.fernanapps.shop.ui.base.BaseFragmentHideTopAndBottom
import pe.fernanapps.shop.ui.main.cart.CategoryAdapter

@AndroidEntryPoint
class CategoriesFragment : BaseFragmentHideTopAndBottom<FragmentCategoriesBinding>(FragmentCategoriesBinding::inflate) {


    private val viewModel by viewModels<CategoriesViewModel>()
    private val categoryAdapter by lazy {
        CategoryAdapter{
            val action = CategoriesFragmentDirections
                .actionNavigationPageCategoriesToNavigationPageProducts(it.title, it.id)
            navController.navigate(action)
        }
    }

    override fun initViews() {

        with(bin){
            includeLinearSearch.search.addTextChangedListener {
                Log.d("CATEGORIES", it.toString())
                viewModel.filterCategories(it.toString())
            }
        }

    }

    override fun initObserves() {
        with(viewModel){
            observe(categories){ state ->
                when (state) {
                    is DataState.Error -> Log.d("CategoriesFragment", "Error " + state.exception.message)
                    is DataState.Finished -> Log.d("CategoriesFragment", "Finish ")
                    is DataState.Loading -> Log.d("CategoriesFragment", "Loading ")
                    is DataState.Success -> {
                        Log.d("CategoriesFragment", state.data[0].title)
                        setOriginalCategories(state.data)
                        categoryAdapter.submitList(state.data)
                    }
                    else -> Unit
                }
            }
            observe(categoriesFiltered){ list ->
                categoryAdapter.submitList(list)
            }
            getCategories()
        }

    }

    override fun initActions() {
        with(bin){
            back.setOnClickListener {
                if(!navController.navigateUp()){
                    navController.navigate(R.id.action_navigation_page_categories_to_navigation_page_home)
                }
            }
            recyclerViewCategories.adapter = categoryAdapter
        }
    }



}