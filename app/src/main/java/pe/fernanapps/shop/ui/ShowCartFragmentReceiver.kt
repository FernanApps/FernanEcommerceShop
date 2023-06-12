package pe.fernanapps.shop.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import pe.fernanapps.shop.Constants

class ShowCartFragmentReceiver : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("BROADCAST", "onReceived")
        if (intent?.action == Constants.ACTION_SHOW_CART_FRAGMENT) {

        }
    }

}

