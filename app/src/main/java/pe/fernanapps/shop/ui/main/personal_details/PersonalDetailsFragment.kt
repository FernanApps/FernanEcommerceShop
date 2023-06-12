package pe.fernanapps.shop.ui.main.personal_details

import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import pe.fernanapps.shop.R
import pe.fernanapps.shop.databinding.FragmentPersonalDetailsBinding
import pe.fernanapps.shop.domain.Constants
import pe.fernanapps.shop.domain.DataState
import pe.fernanapps.shop.domain.model.user.User
import pe.fernanapps.shop.mvvm.observe
import pe.fernanapps.shop.ui.base.BaseFragmentHideTopAndBottom
import pe.fernanapps.shop.ui.main.profile.UserViewModel
import pe.fernanapps.shop.utils.DialogUtils


@AndroidEntryPoint
class PersonalDetailsFragment : BaseFragmentHideTopAndBottom<FragmentPersonalDetailsBinding>(FragmentPersonalDetailsBinding::inflate) {


    private val userViewModel by viewModels<UserViewModel>()

    private val progressDialog by lazy {
        DialogUtils.getProgressAlert(_this, getString(R.string.one_moment_saving_user))

    }
    override fun initActions() {
        with(bin) {
            back.setOnClickListener {
                navController.navigateUp()
            }
            saveUser.setOnClickListener {

                val (result, status)= userViewModel.checkUserAddressAndPhone()
                if(!result){
                    showSnackBar(status)
                } else {
                    bin.saveUser.isEnabled = false
                    bin.saveUser.alpha = 0.5f
                    userViewModel.saveUserInRemote()
                }
            }

            with(userViewModel){
                name.addTextChangedListener {
                    setName(it.toString())
                }

                age.addTextChangedListener {

                }

                phone.addTextChangedListener {
                    setPhone(it.toString())
                }
                city.addTextChangedListener {
                    setCity(it.toString())
                }
                country.addTextChangedListener {
                    setCountry(it.toString())
                }
                line1.addTextChangedListener {
                    setLine1(it.toString())
                }
                line2.addTextChangedListener {
                    setLine2(it.toString())
                }
                postalCode.addTextChangedListener {
                    setPostalCode(it.toString())
                }
                state.addTextChangedListener {
                    setState(it.toString())
                }
            }


        }
    }



    override fun initObserves() {
        with(bin){
            observe(userViewModel.userIdStatus){
                if(it is DataState.Success<User>){
                    val user = it.data
                    name.setText(user.name)
                    email.setText(user.email)

                    if(user.phone != Constants.INFO_NOT_SET){
                        phone.setText(user.phone)
                    }
                    val address = user.address
                    if(address.country != null){
                        country.setText(address.country)
                    }
                    if(address.city != null){
                        city.setText(address.city)
                    }
                    if(address.state != null){
                        state.setText(address.state)
                    }
                    if(address.line1 != null){
                        line1.setText(address.line1)
                    }
                    if(address.line2 != null){
                        line2.setText(address.line2)
                    }
                    if(address.postalCode != null){
                        postalCode.setText(address.postalCode)
                    }

                }
            }

            observe(userViewModel.saveUserRemoteStatus){
                when(it){
                    is DataState.Loading -> progressDialog.show()
                    is DataState.Error -> {
                        progressDialog.dismiss()
                        bin.saveUser.isEnabled = true
                        bin.saveUser.alpha = 1f
                        showSnackBar(getString(R.string.there_was_a_mistake))
                    }
                    is DataState.Finished -> {
                        progressDialog.dismiss()
                        userViewModel.saveUserInLocal()
                        showSnackBar(R.string.saved_successfully)
                    }
                    else -> Unit
                }
            }




        }
    }




}