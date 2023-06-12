package pe.fernanapps.shop.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import pe.fernanapps.shop.data.sources.local.db.EcommerceDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseLocalModule {

    private const val PRODUCT_DATABASE_NAME = "fernan_shop"

    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context): EcommerceDatabase {
        return Room.databaseBuilder(context, EcommerceDatabase::class.java, PRODUCT_DATABASE_NAME).build()
    }

    @Singleton
    @Provides
    fun provideProductCartDao(db: EcommerceDatabase) = db.getProductDao()

    @Singleton
    @Provides
    fun provideFavoriteDao(db: EcommerceDatabase) = db.getFavoriteDao()

    @Singleton
    @Provides
    fun provideNotificationDao(db: EcommerceDatabase) = db.getNotificationDao()

}