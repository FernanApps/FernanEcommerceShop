package pe.fernanapps.shop.ui.main.settings

import android.content.Intent
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pe.fernanapps.shop.Constants
import pe.fernanapps.shop.R
import pe.fernanapps.shop.databinding.FragmentSettingsBinding
import pe.fernanapps.shop.domain.DataState
import pe.fernanapps.shop.mvvm.observe
import pe.fernanapps.shop.ui.base.BaseFragmentHideTopAndBottom
import pe.fernanapps.shop.ui.favorite.FavoriteViewModel
import pe.fernanapps.shop.ui.login.LoginActivity
import pe.fernanapps.shop.ui.login.LoginViewModel
import pe.fernanapps.shop.ui.main.cart.CartViewModel
import pe.fernanapps.shop.ui.main.profile.ItemAction
import pe.fernanapps.shop.ui.main.profile.ItemActionListAdapter
import pe.fernanapps.shop.ui.main.profile.UserViewModel
import pe.fernanapps.shop.utils.DialogUtils
import pe.fernanapps.shop.utils.load
import pe.fernanapps.shop.utils.toast


@AndroidEntryPoint
class SettingsFragment : BaseFragmentHideTopAndBottom<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {

    private val viewModel: LoginViewModel by viewModels()
    private val favViewModel: FavoriteViewModel by viewModels()
    private val cartViewModel: CartViewModel by viewModels()
    private val userViewModel by viewModels<UserViewModel>()


    override fun initViews() {
        with(bin) {

            profileImage.load(
                Constants.PROFILE_IMAGE,
                null,
                RoundedCorners(14)
            )


            val settingsList = listOf(
                Triple(
                    R.string.settings_language,
                    R.drawable.ic_language,
                    null
                ),
                Triple(
                    R.string.settings_notifications,
                    R.drawable.ic_notification,
                    null
                ),
                Triple(
                    R.string.settings_dark_mode,
                    R.drawable.ic_dark_mode,
                    null
                ),
                Triple(
                    R.string.settings_help_center,
                    R.drawable.ic_help,
                    null
                )
            ).map {
                ItemAction(getString(it.first), it.second, it.third)
            }
            recyclerSettings.adapter = ItemActionListAdapter(settingsList)
        }

    }

    override fun initActions() {
        with(bin) {
            back.setOnClickListener {
                navController.navigateUp()
            }

            logOut.setOnClickListener {
                viewModel.logOut()
            }
        }
    }

    private val progressDialog by lazy {
        DialogUtils.getProgressAlert(requireActivity(), getString(R.string.one_moment_coming_out))
    }

    override fun initObserves() {

        with(viewModel) {

            observe(logOutState) {
                when (it) {
                    is DataState.Error -> {
                        progressDialog.dismiss()
                        toast(it.exception.message)
                    }

                    is DataState.Finished -> {
                        favViewModel.deleteAll()
                        cartViewModel.deleteAll()
                        // temp

                        lifecycleScope.launch {
                            withContext(Dispatchers.IO) {
                                delay(5000)
                            }
                            progressDialog.dismiss()
                            val intent = Intent(activity, LoginActivity::class.java)
                            startActivity(intent)
                            activity?.finish()

                        }


                    }

                    is DataState.Loading -> progressDialog.show()
                    is DataState.Success -> {}
                    else -> Unit
                }
            }
        }

        observe(userViewModel.userIdStatus) {
            when (it) {
                is DataState.Error -> {}
                is DataState.Finished -> {}
                is DataState.Loading -> {}
                is DataState.Success -> {
                    bin.profileName.setText(it.data.name)
                    bin.profileEmail.setText(it.data.email)
                }
                else -> Unit
            }
        }

    }




}



