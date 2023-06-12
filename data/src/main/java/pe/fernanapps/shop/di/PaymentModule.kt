package pe.fernanapps.shop.di

import android.app.Application
import com.stripe.android.Stripe
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.appwrite.services.Databases
import pe.fernanapps.shop.ConstantsData
import pe.fernanapps.shop.data.sources.local.LocalDataSource
import pe.fernanapps.shop.data.sources.local.user.UserPreference
import pe.fernanapps.shop.data.sources.remote.payment.CustomerRepositoryImp
import pe.fernanapps.shop.data.sources.remote.payment.PaymentRepositoryImp
import pe.fernanapps.shop.data.sources.remote.payment.PaymentService
import pe.fernanapps.shop.data.sources.remote.payment.api.BackendApi
import pe.fernanapps.shop.domain.repository.CustomerRepository
import pe.fernanapps.shop.domain.repository.PaymentRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PaymentModule {

    @Singleton
    @Provides
    fun provideStripe(application: Application): Stripe {
        return Stripe(application.applicationContext, ConstantsData.getStripePublishableKey())
    }

    @Singleton
    @Provides
    fun providePaymentRepository(
        stripe: Stripe,
        api: BackendApi,
        userPreference: UserPreference,
        local: LocalDataSource,
        paymentService: PaymentService
    ): PaymentRepository {
        return PaymentRepositoryImp(stripe, api, userPreference, local, paymentService)
    }

    /**
     * For AppWrite, upload payment, order, orderItem
     */
    @Singleton
    @Provides
    fun providePaymentService(databases: Databases): PaymentService {
        return PaymentService(databases)
    }


    // Customer
    @Singleton
    @Provides
    fun provideCustomerRepository(
        userPreference: UserPreference,
        backendApi: BackendApi
    ): CustomerRepository {
        return CustomerRepositoryImp(userPreference, backendApi)
    }

}

