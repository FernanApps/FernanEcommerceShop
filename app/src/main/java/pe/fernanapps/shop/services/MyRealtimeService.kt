package pe.fernanapps.shop.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.media.RingtoneManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pe.fernanapps.shop.R
import pe.fernanapps.shop.domain.model.notifications.Notification
import pe.fernanapps.shop.domain.repository.NotificationsLocalRepository
import pe.fernanapps.shop.domain.repository.NotificationsServiceRepository
import pe.fernanapps.shop.ui.main.MainActivity
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

const val NOTIFICATION_ID = 3
const val ACTION_SERVICE_START = "ACTION_SERVICE_START"
const val ACTION_SERVICE_STOP = "ACTION_SERVICE_STOP"
const val ACTION_NAVIGATE_TO_MAPS_FRAGMENT = "ACTION_NAVIGATE_TO_MAPS_FRAGMENT"
const val NOTIFICATION_CHANNEL_ID = "tracker_notification_id"
const val NOTIFICATION_CHANNEL_NAME = "tracker_notification"
const val NOTIFICATION_CHANNEL_DESC = "tracker_notification_description"

const val PENDING_INTENT_REQUEST_CODE = 99

@AndroidEntryPoint
class MyRealtimeService : LifecycleService() {

    @Inject
    lateinit var notificationBuilder: NotificationCompat.Builder

    @Inject
    lateinit var notificationManager: NotificationManager

    @Inject
    lateinit var notificationsServiceRepository: NotificationsServiceRepository

    @Inject
    lateinit var notificationsLocalRepository: NotificationsLocalRepository

    companion object {
        val started = MutableLiveData<Boolean>()

    }

    private fun setInitialValues() {
        started.postValue(
            false
        )
    }

    override fun onCreate() {
        setInitialValues()
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                ACTION_SERVICE_START -> {
                    started.postValue(true)
                    startForegroundService()
                }

                ACTION_SERVICE_STOP -> {
                    started.postValue(false)
                }

                else -> {

                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }


    private fun startForegroundService() {
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, notificationBuilder.build())
        lifecycleScope.launch {
            notificationsServiceRepository.subscribe().collect {
                Log.i("MY_REALTIME_APP", it.toString())
                notificationsLocalRepository.save(it)
                sendNotification(it)
                //createNotification(notification, it)

            }
        }

    }

    private fun createNotification2(
        notificationBuilder: NotificationCompat.Builder,
        notification: pe.fernanapps.shop.domain.model.notifications.Notification,
    ) {
        val context = this

        notificationBuilder.setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle(notification.title)
            .setContentText(notification.content)
            .setAutoCancel(true)

        Glide.with(context)
            .asBitmap()
            .load(notification.imageUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    notificationBuilder.setLargeIcon(resource)

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        createNotificationChannel()
                        notificationBuilder.setChannelId(NOTIFICATION_CHANNEL_ID)
                    }

                    notificationManager.notify(
                        notification.createdAt.toInt(),
                        notificationBuilder.build()
                    )
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // Implementación opcional en caso de que se desee hacer algo cuando se borra la imagen cargada
                }
            })
    }


    private fun createNotification(
        notificationBuilder: NotificationCompat.Builder,
        notification: pe.fernanapps.shop.domain.model.notifications.Notification,
    ) {
        val context = this

        notificationBuilder.setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle(notification.title)
            .setContentText(notification.content)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.ic_notifications
                )
            )
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
            notificationBuilder.setChannelId(NOTIFICATION_CHANNEL_ID)
        }
        notificationManager.notify(notification.createdAt.toInt(), notificationBuilder.build())

    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }
    }


    private fun sendNotification(notification: Notification) {
        val image = getBitmapfromUrl(notification.imageUrl)
        val icon = getBitmapfromUrl(notification.imageUrl)
        val id = notification.id
        val intent = Intent(this, MainActivity::class.java).apply {
            action = System.currentTimeMillis().toString()
            putExtra("id", id)
        }

        val largeIcon = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
        val NOTIFICATION_ID = id.toInt()

        val name: CharSequence = NOTIFICATION_CHANNEL_NAME
        val Description = NOTIFICATION_CHANNEL_DESC
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH

            val defaultSoundUri = Settings.System.DEFAULT_NOTIFICATION_URI ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)


            val ringtoneDefaul = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val mChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance).apply {
                description = Description
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
                vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
                setShowBadge(false)
                setSound(defaultSoundUri, android.app.Notification.AUDIO_ATTRIBUTES_DEFAULT)

            }

            notificationManager.createNotificationChannel(mChannel)

        }
        notificationBuilder.apply {
            setSmallIcon(R.drawable.ic_notifications)
            setContentTitle(notification.title)
            setContentText(notification.content)
            setAutoCancel(true)
            if (icon != null) {
                setLargeIcon(icon);
            } else {
                setLargeIcon(largeIcon);
            }
            if (image != null) {
                setStyle(NotificationCompat.BigPictureStyle().bigPicture(image))
            } else {
                setStyle(
                    NotificationCompat.BigTextStyle().bigText(notification.content)
                )
            }
        }

        val stackBuilder = TaskStackBuilder.create(applicationContext)
            .apply {
                addParentStack(MainActivity::class.java)
                addNextIntent(intent)
            }

        val resultPendingIntent =
            stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        notificationBuilder.setContentIntent(resultPendingIntent)
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    /*
     *To get a Bitmap image from the URL received
     * */
    fun getBitmapfromUrl(imageUrl: String?): Bitmap? {
        return try {
            val url = URL(imageUrl)
            val connection =
                url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            // TODO Auto-generated catch block
            e.printStackTrace()
            null
        }
    }


}
