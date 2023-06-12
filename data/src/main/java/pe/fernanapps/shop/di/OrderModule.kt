package pe.fernanapps.shop.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pe.fernanapps.shop.data.sources.local.user.UserPreference
import pe.fernanapps.shop.data.sources.remote.orders.OrderRepositoryImp
import pe.fernanapps.shop.data.sources.remote.orders.OrderService
import pe.fernanapps.shop.domain.repository.OrderRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OrderModule {
    
    @Singleton
    @Provides
    fun provideOrderRepository(
        userPreference: UserPreference,
        orderService: OrderService
    ): OrderRepository {
        return OrderRepositoryImp(userPreference, orderService)
    }
    
}