package pe.fernanapps.shop.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import pe.fernanapps.shop.data.sources.EcommerceDataSource
import pe.fernanapps.shop.data.sources.remote.home.ProductRepositoryImp
import pe.fernanapps.shop.data.sources.remote.home.RemoteEcommerceDataSource
import pe.fernanapps.shop.data.sources.remote.home.ProductService
import pe.fernanapps.shop.domain.repository.ProductRepository
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RemoteSourcesModule {


    @Provides
    @Singleton
    fun provideRemoteEcommerceDataSource(service: ProductService): EcommerceDataSource.Remote {
        return RemoteEcommerceDataSource(service)
    }

//    @Provides
//    @Singleton
//    fun provideOfferRepository(remote: EcommerceDataSource.Remote): ProductRepository {
//        return ProductRepositoryImp(remote)
//    }

}

@Module
@InstallIn(ViewModelComponent::class)
interface RemoteRepositoryModule {
    @Binds
    fun provideApiRepository(impl: ProductRepositoryImp
    ): ProductRepository
}

