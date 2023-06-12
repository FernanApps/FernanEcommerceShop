package pe.fernanapps.shop.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import pe.fernanapps.shop.R
import pe.fernanapps.shop.databinding.FragmentBaseBinding
import pe.fernanapps.shop.databinding.FragmentOrdersBinding
import pe.fernanapps.shop.ui.main.notifications.NotificationsViewModel
import pe.fernanapps.shop.utils.SnackbarController



abstract class BaseFragment<VB : ViewBinding>(
    private val bindingInflater: (inflater: LayoutInflater) -> VB
) : Fragment() {



    val _this get() = this.context!!

    private var _binding: VB? = null
    val bin get() = _binding as VB


    val supportActionBar get() = (requireActivity() as AppCompatActivity?)?.supportActionBar
    val bottomNavigationView: BottomNavigationView? get() = requireActivity().findViewById(R.id.bottom_nav_view)
    val navController get() = findNavController()


    /*
    fun <T> LifecycleOwner.observe(liveData: LiveData<T>, action: (t: T) -> Unit) {
        liveData.observe(this) { it?.let { t -> action(t) } }
    }
     */




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater)
        if (_binding == null) {
            throw java.lang.IllegalArgumentException("Binding cannot be null")
        }
        initObserves()
        initViews()
        initActions()
        return bin.root

    }

    open fun initViews() = Unit
    open fun initObserves() = Unit
    open fun initActions() = Unit

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    protected fun showSnackBar(message: String){
        SnackbarController(bin).show(message)
    }
    protected fun showSnackBar(@StringRes message: Int){
        SnackbarController(bin).show(getString(message))
    }

}



abstract class BaseFragmentHideTopAndBottom<VB : ViewBinding>(
    bindingInflater: (inflater: LayoutInflater) -> VB
) : BaseFragment<VB>(bindingInflater) {

    open val hideActionBar = true
    open val hideBottomBar = true

    override fun onResume() {
        super.onResume()
        if(hideActionBar){
            supportActionBar?.hide()
        }
        if(hideBottomBar){
            bottomNavigationView?.visibility = View.GONE
        }

    }

    override fun onPause() {
        super.onPause()
        if(hideActionBar){
            //supportActionBar?.show()
        }
        if(hideBottomBar){
            bottomNavigationView?.visibility = View.VISIBLE
        }


    }
}





abstract class BaseFragmentMain : BaseFragmentHideTopAndBottom<FragmentBaseBinding>(FragmentBaseBinding::inflate) {

    abstract val adapter: RecyclerView.Adapter<*>
    @get:StringRes abstract  val fragmentTitle:  Int



    override fun initViews() {
        super.initViews()
        with(bin){
            title.text = getString(fragmentTitle)
            recycler.adapter = adapter
        }
    }

    protected fun showView(isEmpty: Boolean) {

        with(bin) {
            empty.root.isVisible = isEmpty
            recycler.isVisible = !isEmpty
            loading.root.isVisible = false

        }
    }

}

