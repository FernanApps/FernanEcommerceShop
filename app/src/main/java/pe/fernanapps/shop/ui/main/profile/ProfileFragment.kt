package pe.fernanapps.shop.ui.main.profile

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import dagger.hilt.android.AndroidEntryPoint
import pe.fernanapps.shop.Constants
import pe.fernanapps.shop.R
import pe.fernanapps.shop.databinding.FragmentProfileBinding
import pe.fernanapps.shop.domain.DataState
import pe.fernanapps.shop.domain.model.user.User
import pe.fernanapps.shop.mvvm.observe
import pe.fernanapps.shop.ui.base.BaseFragment
import pe.fernanapps.shop.ui.chat.ChatActivity
import pe.fernanapps.shop.utils.load
import pe.fernan.list.selector.circularDrawable
import pe.fernan.list.selector.customIconDrawableWithBackground
import pe.fernanapps.shop.ui.base.BaseFragmentHideTopAndBottom


@AndroidEntryPoint
class ProfileFragment : BaseFragmentHideTopAndBottom<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    override val hideBottomBar: Boolean get() = false

    private val userViewModel by viewModels<UserViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val customArrowBackDrawable = customIconDrawableWithBackground(
            iconDrawable = pe.fernan.list.selector.customIconDrawable(
                requireActivity(),
                iconDrawable = R.drawable.ic_arrow_back,
                iconSizeInDp = 24,
                iconColor = Color.WHITE
            ), marginInsideIcon = 0, backgroundDrawable = circularDrawable(Color.BLACK)

        )
        //supportActionBar?.setHomeAsUpIndicator(customArrowBackDrawable)

        return super.onCreateView(inflater, container, savedInstanceState)

    }

    private fun toPersonalDetails() {
        navController.navigate(pe.fernanapps.shop.R.id.action_navigation_page_profile_to_navigation_page_personal_details)
    }

    private fun toOrders() {
        navController.navigate(pe.fernanapps.shop.R.id.action_navigation_page_profile_to_navigation_page_orders)
    }

    private fun toSettings() {
        navController.navigate(pe.fernanapps.shop.R.id.action_navigation_page_profile_to_navigation_page_settings)
    }
    private fun toFavorites() {
        navController.navigate(pe.fernanapps.shop.R.id.action_navigation_page_profile_to_navigation_page_favorites)
    }

    private fun toPrivacyPolicy() {
        startActivity(Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(Constants.PRIVACY_POLICY_URL)
        })
    }
    private fun toContactSupport() {
        startActivity(Intent(_this, ChatActivity::class.java))
    }
    private fun toFaqs() {
        toPrivacyPolicy()
    }


    override fun initViews() {
        with(bin) {

            profileImage.load(
                Constants.PROFILE_IMAGE,
                null,
                RoundedCorners(14)
            )

            val itemActionList1 = listOf(
                Triple(R.string.profile_personal_details, R.drawable.ic_profile) { toPersonalDetails()},
                Triple(R.string.profile_personal_my_order, R.drawable.ic_order) { toOrders() },
                Triple(R.string.profile_personal_my_favourites, R.drawable.ic_favourites) { toFavorites() },
                Triple(R.string.profile_personal_shipping_address, R.drawable.ic_shipping, null),
                Triple(R.string.profile_personal_settings, R.drawable.ic_settings2) { toSettings() }

            ).map {
                ItemAction(getString(it.first), it.second, it.third)
            }

            val itemActionList2 = listOf(
                Triple(R.string.profile_personal_contact_support, R.drawable.ic_contact_support) { toContactSupport() },
                Triple(R.string.profile_personal_faqs, R.drawable.ic_faq) { toFaqs() },
                Triple(R.string.profile_personal_privacy_policy, R.drawable.ic_privacy) { toPrivacyPolicy() },
            ).map {
                ItemAction(getString(it.first), it.second, it.third)
            }


            recyclerProfileItems1.adapter = ItemActionListAdapter(itemActionList1)
            recyclerProfileItems2.adapter = ItemActionListAdapter(itemActionList2)


        }
    }

    override fun initObserves() {
        observe(userViewModel.userIdStatus) {
            when (it) {
                is DataState.Error -> {}
                is DataState.Finished -> {}
                is DataState.Loading -> {}
                is DataState.Success -> {
                    setUser(it.data)
                }
                else -> Unit
            }
        }

    }

    private fun setUser(user: User) {
        bin.profileName.text = user.name
        bin.profileEmail.text = user.email
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        println("Press Item ${item.itemId}")
        return when (item.itemId) {
            android.R.id.home -> {
                requireActivity().onBackPressed()
                true

            }

            else -> super.equals(item)
        }
    }

    override fun onResume() {
        super.onResume()
        userViewModel.getUser()
    }
}