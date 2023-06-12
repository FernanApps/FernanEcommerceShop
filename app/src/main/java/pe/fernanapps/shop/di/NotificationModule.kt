package pe.fernanapps.shop.di

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.resources.Compatibility.Api18Impl.setAutoCancel
import androidx.core.app.NotificationCompat
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import pe.fernanapps.shop.R
import pe.fernanapps.shop.services.MyRealtimeService
import pe.fernanapps.shop.services.NOTIFICATION_CHANNEL_ID
import pe.fernanapps.shop.services.PENDING_INTENT_REQUEST_CODE
import pe.fernanapps.shop.ui.main.MainActivity

@Module
@InstallIn(ServiceComponent::class)
object NotificationModule {


    @ServiceScoped
    @Provides
    fun providePendingIntent(
        @ApplicationContext context: Context,
    ): PendingIntent {
        return PendingIntent.getActivity(
            context,
            PENDING_INTENT_REQUEST_CODE,
            Intent(context, MainActivity::class.java)/*.apply {
                this.action = Constants.ACTION_NAVIGATE_TO_MAPS_FRAGMENT //the navigation has already implemented in the Activity
            },*/,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    @ServiceScoped
    @Provides
    fun provideNotificationBuilder(
        @ApplicationContext context: Context,
        pendingIntent: PendingIntent,
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentIntent(pendingIntent)
    }

    @ServiceScoped
    @Provides
    fun provideNotificationManager(
        @ApplicationContext context: Context,
    ): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }


}