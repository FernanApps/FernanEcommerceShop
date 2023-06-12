package pe.fernanapps.shop.ui.main.notifications

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import pe.fernanapps.shop.R
import pe.fernanapps.shop.domain.DataState
import pe.fernanapps.shop.mvvm.observe
import pe.fernanapps.shop.ui.base.BaseFragmentMain

@AndroidEntryPoint
class NotificationsFragment : BaseFragmentMain() {

    private val viewModel by viewModels<NotificationsViewModel>()

    override val hideBottomBar: Boolean get() = false

    override val adapter by lazy {
        NotificationAdapter()
    }
    override val fragmentTitle: Int get() = R.string.navigation_page_title_notification

    override fun initViews() {
        super.initViews()
        bin.empty.title.text = getString(R.string.you_dont_have_any_notification)
    }

    override fun initObserves() {
        observe(viewModel.notifications){
            if(it.isEmpty()){
                showView(true)
            } else {
                showView(false)
                adapter.submitList(it)
            }
        }

        observe(viewModel.notificationsStatus){
            when(it){
                is DataState.Error -> showView(true)
                DataState.Finished -> Unit
                DataState.Loading -> Unit
                is DataState.Progress -> Unit
                is DataState.Success -> {
                    viewModel.saveInLocal(it.data)
                    if(it.data.isEmpty()){
                        showView(true)
                    } else {
                        showView(false)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllLocalNotifications()
    }

}