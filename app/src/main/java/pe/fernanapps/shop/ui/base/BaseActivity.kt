package pe.fernanapps.shop.ui.base

import android.annotation.SuppressLint
import android.app.ProgressDialog.show
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import pe.fernanapps.shop.utils.SnackbarController


abstract class BaseActivity<VB: ViewBinding>(
    private val bindingInflater: (inflater: LayoutInflater) -> VB
) : AppCompatActivity(){

    private var _binding: VB? = null
    val bin get() = _binding as VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = bindingInflater.invoke(layoutInflater)
        if(_binding == null){
            throw java.lang.IllegalArgumentException("Binding cannot be null")
        }
        setContentView(bin.root)
        initObserves()
        initViews()
        initActions()
    }

    open fun initViews() = Unit
    open fun initObserves() = Unit
    open fun initActions() = Unit


    protected fun showSnackBar(message: String){
        SnackbarController(bin).show(message)
    }


}