package pe.fernanapps.shop

import android.app.Application
import android.content.Intent
import androidx.core.content.ContentProviderCompat.requireContext
import dagger.hilt.android.HiltAndroidApp
import pe.fernanapps.shop.services.ACTION_SERVICE_START
import pe.fernanapps.shop.services.MyRealtimeService

@HiltAndroidApp
class FernanShopApp : Application() {

    override fun onCreate() {
        super.onCreate()
        sendActionCommandToService(ACTION_SERVICE_START)
    }

    private fun sendActionCommandToService(action: String){
        Intent(this, MyRealtimeService::class.java).apply {
            this.action = action
            this@FernanShopApp.startService(this)
        }
    }
}