package pe.fernanapps.shop.di

import androidx.lifecycle.SavedStateHandle
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import pe.fernanapps.shop.Constants
import pe.fernanapps.shop.domain.model.product.Product
import javax.inject.Qualifier

@Module
@InstallIn(ViewModelComponent::class)
object DetailViewModelModule {

  //  private val _product: Product? by lazy {
    //        this@DetailsActivity.intent.getSerializableExtra(INTENT_PRODUCT_TO_DETAILS) as? Product
    //    }

    @ProductData
    @Provides
    fun provideProduct(savedStateHandle: SavedStateHandle) =
        requireNotNull(savedStateHandle.get<Product>(Constants.ACTION_SHOW_CART_FRAGMENT))

    @Retention(AnnotationRetention.BINARY)
    @Qualifier
    annotation class ProductData

}