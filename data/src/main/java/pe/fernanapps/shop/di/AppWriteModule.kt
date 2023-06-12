package pe.fernanapps.shop.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.appwrite.Client
import io.appwrite.services.Account
import io.appwrite.services.Databases
import io.appwrite.services.Realtime
import pe.fernanapps.shop.ConstantsData
import pe.fernanapps.shop.data.BuildConfig
import pe.fernanapps.shop.data.sources.local.user.UserPreference
import pe.fernanapps.shop.data.sources.remote.chat.ChatRepositoryImp
import pe.fernanapps.shop.data.sources.remote.chat.ChatService
import pe.fernanapps.shop.data.sources.remote.chat.MessageRepositoryImp
import pe.fernanapps.shop.data.sources.remote.notifications.NotificationsRemoteRepositoryImp
import pe.fernanapps.shop.data.sources.remote.notifications.NotificationsService
import pe.fernanapps.shop.data.sources.remote.notifications.NotificationsServiceRepositoryImp
import pe.fernanapps.shop.data.sources.remote.user.UserRemoteRepositoryImp
import pe.fernanapps.shop.data.sources.remote.user.UserService
import pe.fernanapps.shop.domain.repository.ChatRepository
import pe.fernanapps.shop.domain.repository.MessageRepository
import pe.fernanapps.shop.domain.repository.NotificationsRemoteRepository
import pe.fernanapps.shop.domain.repository.NotificationsServiceRepository
import pe.fernanapps.shop.domain.repository.UserRemoteRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppWriteModule {

    @Provides
    @Singleton
    fun provideAppWriteClient(app: Application): Client {
        return Client(app.applicationContext)
            .setEndpoint(ConstantsData.getAppWriteEndpoint())
            .setProject(ConstantsData.getAppWriteProyectId())
            .setSelfSigned(status =  BuildConfig.DEBUG)
    }


    @Provides
    @Singleton
    fun provideAppWriteAccount(client: io.appwrite.Client) = Account(client)

    @Provides
    @Singleton
    fun provideAppWriteDatabases(client: io.appwrite.Client) = Databases(client)

    @Provides
    @Singleton
    fun provideAppWriteRealtime(client: io.appwrite.Client) = Realtime(client)

    @Provides
    @Singleton
    fun provideMessageRepository(
        databases: Databases,
        realtime: Realtime
    ): MessageRepository {
        return MessageRepositoryImp(databases, realtime)
    }

    @Provides
    @Singleton
    fun provideChatRepository(
        chatService: ChatService
    ): ChatRepository {
        return ChatRepositoryImp(chatService)
    }


    @Provides
    @Singleton
    fun provideUserRemoteRepository(
        userService: UserService
    ): UserRemoteRepository {
        return UserRemoteRepositoryImp(userService)
    }

    @Provides
    @Singleton
    fun provideNotificationsServiceRepository(
        userPreference: UserPreference,
        realtime: Realtime
    ): NotificationsServiceRepository {
        return NotificationsServiceRepositoryImp(userPreference, realtime)
    }

    @Provides
    @Singleton
    fun provideNotificationsRemoteRepository(
        userPreference: UserPreference,
        notificationsService: NotificationsService
    ): NotificationsRemoteRepository {
        return NotificationsRemoteRepositoryImp(userPreference, notificationsService)
    }


}
