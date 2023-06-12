package pe.fernanapps.shop.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import pe.fernanapps.shop.R
import pe.fernanapps.shop.databinding.DialogProgressBinding

object DialogUtils {

    //    fun getProgressAlert(context: Context): AlertDialog {
//        return AlertDialog.Builder(context).apply {
//            setView(LayoutInflater.from(context).inflate(R.layout.dialog_progress, null))
//            setCancelable(false)
//        }.create()
//    }
    fun getProgressAlert(context: Context, titleProgress: String = ""): AlertDialog {
        val inflater = LayoutInflater.from(context)

        val bin = DialogProgressBinding.bind(
            inflater.inflate(R.layout.dialog_progress, null)
        ).apply {
            if (titleProgress.isNotEmpty()) {
                this.title.text = titleProgress
            }
        }

        return MaterialAlertDialogBuilder(context)
            .setView(bin.root)
            .setCancelable(false)
            .create()
    }

}


class CustomProgressDialog(private val context: Context, private val titleProgress: String = "") {

    private var bin: DialogProgressBinding
    private var alertDialog: AlertDialog

    init {
        val inflater = LayoutInflater.from(context)
        bin = DialogProgressBinding.inflate(inflater)

        initViews()
        initActions()

        alertDialog = MaterialAlertDialogBuilder(context)
            .setView(bin.root)
            .setCancelable(false)
            .create()

    }

    fun initViews() {
        bin.title.text = titleProgress
    }

    fun initActions() {

    }

    fun setButtonAction(title: String = "", action: () -> Unit){
        bin.btn.isVisible = true
        if(title.isNotEmpty()){
            bin.btn.text = title
        }
        bin.btn.setOnClickListener {
            action.invoke()
        }
    }

    fun changeIcon(@DrawableRes int: Int) {
        bin.progressBar.isVisible = false
        bin.icon.apply {
            isVisible = true
            setImageResource(int)
        }
    }

    private fun resetViews(){
        with(bin){
            progressBar.isVisible = true
            icon.isVisible = false
            btn.isVisible = false
            icon.setImageResource(R.drawable.ic_launcher_foreground)
            bin.title.text = titleProgress

        }
    }


    @SuppressLint("SetTextI18n")
    fun addTitle(title: String) {
        if (title.isNotEmpty()) {
            val titleNow = bin.title.text
            bin.title.text = "$titleNow \n$title"
        }
    }


    @SuppressLint("SetTextI18n")
    fun addTitle(@StringRes titleRes: Int) {
        val title = getString(titleRes)
        if (title.isNotEmpty()) {
            val titleNow = bin.title.text
            bin.title.text = "$titleNow \n$title"
        }
    }

    fun changeTitle(@StringRes titleRes: Int) {
        val title  = getString(titleRes)
        if (title.isNotEmpty()) {
            bin.title.text = title
        }
    }

    fun changeTitle(title: String) {
        if (title.isNotEmpty()) {
            bin.title.text = title
        }
    }

    fun show() {
        alertDialog.show()
    }

    fun dismiss() {
        // Reset Values
        resetViews()
        alertDialog.dismiss()
    }


    fun getString(@StringRes title: Int): String {
        return context.resources.getString(title)
    }
}

