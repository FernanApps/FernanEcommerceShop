package pe.fernanapps.shop.di


import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pe.fernanapps.shop.data.sources.remote.login.LoginRepositoryImp
import pe.fernanapps.shop.data.sources.remote.user.UserService
import pe.fernanapps.shop.domain.repository.LoginRepository
import pe.fernanapps.shop.utils.PrefManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoginModule {

    @Provides
    @Singleton
    fun provideLoginRepository(
        client: io.appwrite.Client,
        pref: PrefManager,
        userService: UserService
    ): LoginRepository {
        return LoginRepositoryImp(client, pref, userService)
    }

}