package pe.fernanapps.shop

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FernanShopApp : Application() {

    override fun onCreate() {
        super.onCreate()
        //sendActionCommandToService(ACTION_SERVICE_START)
    }

//    private fun sendActionCommandToService(action: String){
//        Intent(this, MyRealtimeService::class.java).apply {
//            this.action = action
//            this@FernanShopApp.startService(this)
//        }
//    }
}