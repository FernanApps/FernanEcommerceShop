package pe.fernanapps.shop.utils

import android.annotation.SuppressLint
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import pe.fernanapps.shop.utils.UIUtils.themeAccentColor

class SnackbarController <VB: ViewBinding>(private val bin: VB) {
     fun show(message: String) {
            Snackbar.make(bin.root, message, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(bin.root.context.themeAccentColor())
                .show()
        }
    }