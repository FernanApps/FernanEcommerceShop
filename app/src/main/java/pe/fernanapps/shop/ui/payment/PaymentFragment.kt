package pe.fernanapps.shop.ui.payment

import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.github.credit_card_view.data.CardSide
import dagger.hilt.android.AndroidEntryPoint
import pe.fernanapps.shop.R
import pe.fernanapps.shop.databinding.FragmentPaymentBinding
import pe.fernanapps.shop.domain.DataState
import pe.fernanapps.shop.domain.SimpleState
import pe.fernanapps.shop.domain.model.user.Card
import pe.fernanapps.shop.domain.model.user.User
import pe.fernanapps.shop.mvvm.observe
import pe.fernanapps.shop.ui.base.BaseFragmentHideTopAndBottom
import pe.fernanapps.shop.ui.main.cart.CartViewModel
import pe.fernanapps.shop.ui.main.profile.UserViewModel
import pe.fernanapps.shop.ui.payment.card.CardAdapter
import pe.fernanapps.shop.utils.CustomProgressDialog
import pe.fernanapps.shop.utils.StringExt


@AndroidEntryPoint
class PaymentFragment :
    BaseFragmentHideTopAndBottom<FragmentPaymentBinding>(FragmentPaymentBinding::inflate) {

    private val cardViewModel by viewModels<CartViewModel>()
    private val viewModel by viewModels<PaymentViewModel>()
    private val userViewModel by viewModels<UserViewModel>()

    private val progressDialog by lazy {
        CustomProgressDialog(_this, getString(R.string.one_moment_payment_initiated))
    }

    private val cardAdapter by lazy {
        CardAdapter()
    }

    //private val args: PaymentFragmentArgs by navArgs()

    override fun initViews() {
        bin.recyclerCards.adapter = cardAdapter
        bin.loading.title.setText(R.string.loading_your_cards)
        //bin.totalPrice.text = StringExt.formatCurrency(args.priceTotal)
    }

    override fun initObserves() {
        /** UserViewModel */
        with(userViewModel) {
            observe(addressStatus) {
                when (it) {
                    is SimpleState.Error -> {
                        bin.userAddress.text = it.message
                        bin.btnConfigAddress.isVisible = true
                        bin.btnContinue.alpha = 0.5f
                        bin.btnContinue.isEnabled = false

                    }

                    is SimpleState.Success -> {
                        bin.userAddress.text = it.data
                        bin.btnConfigAddress.isVisible = false
                        bin.btnContinue.alpha = 1f
                        bin.btnContinue.isEnabled = true
                    }
                }
            }

            observe(userName) {
                bin.userName.text = it
            }

            observe(userIdStatus) {
                when (it) {
                    is DataState.Error -> Unit
                    DataState.Finished -> Unit
                    DataState.Loading -> Unit
                    is DataState.Progress -> Unit
                    is DataState.Success -> {
                        val user = it.data
                        setUserView(user)
                    }
                }
            }

        }

        with(viewModel) {

            observe(cards) {
                when (it) {
                    is DataState.Error -> {
                        bin.loading.progressBar.isInvisible = true
                        bin.loading.title.setText(R.string.not_cards)
                    }
                    DataState.Finished -> {
                        bin.loading.progressBar.isInvisible = true
                        bin.loading.title.setText(R.string.not_cards)
                    }

                    DataState.Loading -> Unit
                    is DataState.Progress -> Unit
                    is DataState.Success -> {
                        cardAdapter.submitList(it.data)
                        bin.loading.root.isVisible = false
                        bin.recyclerCards.isVisible = true
                    }
                }
            }


            observe(showBackCard) {
                if (it) {
                    bin.creditCardView.flipCard(CardSide.BACK)
                }
            }

            observe(message) {
                if (it.isNotBlank()) {
                    bin.btnContinue.isEnabled = true
                }
                showSnackBar(it)
            }
            observe(paymentResult) {
                println("paymentResult $it")

                when (it) {
                    is DataState.Progress -> progressDialog.addTitle(getString(pe.fernanapps.shop.R.string.one_moment_payment_progress))

                    is DataState.Error -> {
                        progressDialog.changeIcon(pe.fernanapps.shop.R.drawable.ic_error)
                        progressDialog.setButtonAction {
                            progressDialog.dismiss()
                            bin.btnContinue.isEnabled = true
                        }
                        progressDialog.addTitle(getString(pe.fernanapps.shop.R.string.one_moment_payment_failure))
                        progressDialog.addTitle(it.exception.message ?: "Error")


                    }

                    is DataState.Finished -> {
                        //progressDialog.dismiss()
                    }

                    is DataState.Loading -> progressDialog.show()

                    is DataState.Success -> {
                        progressDialog.addTitle(getString(pe.fernanapps.shop.R.string.one_moment_payment_successfully))
                        viewModel.generateOrder(it.data)
                    }
                }

            }

            observe(finishPayment) {
                when (it) {
                    is DataState.Error -> {
                        progressDialog.changeIcon(pe.fernanapps.shop.R.drawable.ic_error)
                        progressDialog.setButtonAction {
                            progressDialog.dismiss()
                            bin.btnContinue.isEnabled = true
                        }
                        progressDialog.addTitle(getString(pe.fernanapps.shop.R.string.one_moment_order_failure))
                        progressDialog.addTitle(it.exception.message ?: "Error")
                    }

                    is DataState.Success -> {
                        if (it.data) {
                            progressDialog.changeIcon(pe.fernanapps.shop.R.drawable.ic_check_circle)
                            progressDialog.addTitle(getString(pe.fernanapps.shop.R.string.one_moment_order_success))

                        }
                        progressDialog.setButtonAction(getString(pe.fernanapps.shop.R.string.continue_shopping)) {
                            progressDialog.dismiss()
                            navController.navigate(R.id.navigation_page_home)
                            //MainActivity.navigateCartFragment.set(true)
                        }
                    }

                    is DataState.Loading -> {
                        progressDialog.addTitle(getString(pe.fernanapps.shop.R.string.one_moment_order_generating))

                    }

                    else -> Unit

                }
            }
            observe(cardViewModel.priceTotal){
                bin.totalPrice.text = StringExt.formatCurrency(it)
            }
            cardViewModel.getUltimateProductsInCart()

        }
    }


    override fun initActions() {
        with(bin) {
            btnContinue.setOnClickListener {
                if (cardAdapter.currentList.isEmpty()) {
                    showSnackBar(R.string.not_cards)
                    return@setOnClickListener
                }
                btnContinue.isEnabled = false
                viewModel.processPayment(
                    cardAdapter.getCurrentSelected()
                )
            }

            btnConfigAddress.setOnClickListener {
                toProfileDetails()
            }

            btnEditAddress.setOnClickListener {
                toProfileDetails()
            }

            addCard.setOnClickListener {
                navController.navigate(R.id.navigation_page_payment_card)
            }

            back.setOnClickListener {
                if (navController.navigateUp()) {
                    navController.navigate(R.id.navigation_page_home)
                }
            }


        }
    }

    private fun toProfileDetails() {
        navController.navigate(R.id.navigation_page_personal_details)
    }


    fun setUserView(user: User) {
        with(bin) {
            userName.text = user.name
            val address = user.address


            val addressText =
                "${address.line1}, ${address.line2}\n${address.city}, ${address.state}, ${address.postalCode}\n${user.phone}"
            userAddress.text = addressText

        }
    }


    override fun onResume() {
        super.onResume()
        userViewModel.checkAddressAndPhone()
        cardViewModel.getUltimateProductsInCart()
        viewModel.getCards()
    }


}