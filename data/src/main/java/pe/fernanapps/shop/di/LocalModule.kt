package pe.fernanapps.shop.di

import android.app.Application
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import pe.fernanapps.shop.data.sources.EcommerceDataSource
import pe.fernanapps.shop.data.sources.local.LocalDataSource
import pe.fernanapps.shop.data.sources.local.NotificationsLocalRepositoryImp
import pe.fernanapps.shop.data.sources.local.PreferencesRepositoryImp
import pe.fernanapps.shop.data.sources.local.ProductCartRepositoryImp
import pe.fernanapps.shop.data.sources.local.ProductFavoriteRepositoryImp
import pe.fernanapps.shop.data.sources.local.dao.NotificationDao
import pe.fernanapps.shop.data.sources.local.user.UserLocalLocalRepositoryImp
import pe.fernanapps.shop.data.sources.local.dao.ProductCartDao
import pe.fernanapps.shop.data.sources.local.dao.ProductFavoriteDao
import pe.fernanapps.shop.data.sources.local.user.UserPreference
import pe.fernanapps.shop.data.sources.remote.notifications.NotificationsRemoteRepositoryImp
import pe.fernanapps.shop.domain.repository.NotificationsLocalRepository
import pe.fernanapps.shop.domain.repository.NotificationsRemoteRepository
import pe.fernanapps.shop.domain.repository.PreferencesRepository
import pe.fernanapps.shop.domain.repository.ProductCartRepository
import pe.fernanapps.shop.domain.repository.ProductFavoriteRepository
import pe.fernanapps.shop.domain.repository.UserLocalRepository
import pe.fernanapps.shop.utils.PrefManager
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object LocalSourcesModule {

    @Provides
    @Singleton
    fun provideLocalProductCartDataSource(
        cartDao: ProductCartDao,
        favoriteDao: ProductFavoriteDao,
        notificationDao: NotificationDao,
    ): EcommerceDataSource.Local {
        return LocalDataSource(cartDao, favoriteDao, notificationDao)
    }


}


const val PREF_NAME = pe.fernanapps.shop.data.BuildConfig.LIBRARY_PACKAGE_NAME + "_pref"

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun providePrefManager(app: Application): PrefManager {
        return PrefManager(app.applicationContext, PREF_NAME)
    }

    @Provides
    @Singleton
    fun provideUserPreference(pref: PrefManager): UserPreference {
        return UserPreference(pref)
    }

    @Provides
    @Singleton
    fun providePreferencesRepository(pref: PrefManager): PreferencesRepository {
        return PreferencesRepositoryImp(pref)
    }

    @Provides
    @Singleton
    fun provideLocalUserRepository(userPref: UserPreference): UserLocalRepository {
        return UserLocalLocalRepositoryImp(userPref)
    }

    @Provides
    @Singleton
    fun provideNotificationsLocalRepository(local: LocalDataSource): NotificationsLocalRepository {
        return NotificationsLocalRepositoryImp(local)
    }


//    @Provides
//    @Singleton
//    @Named("prefName")
//    fun providePrefName(): String {
//        return PREF_NAME
//    }

}


@Module
@InstallIn(ViewModelComponent::class)
interface LocalRepositoryModule {
    @Binds
    fun provideProductRepository(impl: ProductCartRepositoryImp): ProductCartRepository

    @Binds
    fun provideFavoriteRepository(impl: ProductFavoriteRepositoryImp): ProductFavoriteRepository


}


