package pe.fernanapps.shop.ui.payment.card

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import pe.fernanapps.shop.R
import pe.fernanapps.shop.databinding.FragmentPaymentCardBinding
import pe.fernanapps.shop.domain.DataState
import pe.fernanapps.shop.mvvm.observe
import pe.fernanapps.shop.ui.base.BaseFragmentHideTopAndBottom
import pe.fernanapps.shop.utils.CustomProgressDialog


@AndroidEntryPoint
class CardFragment :
    BaseFragmentHideTopAndBottom<FragmentPaymentCardBinding>(FragmentPaymentCardBinding::inflate) {

    private val viewModel by viewModels<CardViewModel>()


    fun showProgressError(){
        progressDialog.changeIcon(R.drawable.ic_error)
        progressDialog.addTitle(R.string.there_was_a_mistake)
        progressDialog.setButtonAction("Ok"){
            progressDialog.dismiss()
            bin.confirm.isEnabled = true
        }
    }

    override fun initObserves() {

        with(viewModel){
            observe(message){
                showSnackBar(it)
                bin.confirm.isEnabled = true
            }
            observe(saveCard){
                when(it){
                    is DataState.Error -> {
                        showProgressError()
                    }
                    DataState.Finished -> Unit
                    DataState.Loading -> progressDialog.show()
                    is DataState.Progress -> Unit
                    is DataState.Success -> {
                        if(it.data){
                            progressDialog.changeIcon(R.drawable.ic_check_circle)
                            progressDialog.addTitle(R.string.saved_successfully)
                            progressDialog.setButtonAction("Ok"){
                                progressDialog.dismiss()
                                toBack()
                            }
                        } else {
                            showProgressError()
                        }


                    }
                }
            }
        }
    }

    private val progressDialog by lazy {
        CustomProgressDialog(_this, getString(R.string.one_moment_payment_initiated))
    }

    override fun initViews() {
        progressDialog.changeTitle(R.string.one_moment_saving_card)

        with(bin) {
            formatEdit(edit = bin.cardNumber)
            formatEdit(textAdd = " / ", spaceInsideSeparate = 2, maxInterVal = 1, edit = bin.cardDate)

        }


    }


    fun toBack(){
        if (navController.navigateUp()) {
            navController.navigate(R.id.navigation_page_payment)
        }
    }


    override fun initActions() {
        with(bin) {
            back.setOnClickListener {
                toBack()
            }
            cancel.setOnClickListener {
                toBack()
            }

            confirm.setOnClickListener {
                confirm.isEnabled = false
                viewModel.saveCard(
                    cardNumber = cardNumber.text.toString(),
                    cardExpDate = cardDate.text.toString(),
                    cardCVC = cardCvc.text.toString()
                )
            }


        }
    }






    fun formatEdit(textAdd: String = "  ", spaceInsideSeparate: Int = 4, maxInterVal: Int = 5, edit: EditText) {
        try {
            val lengthTextAdd = textAdd.length

            fun getNumberPosition(position: Int): Int {
                // lengthTextAdd = 2
                // spaceInside = 4
                // spaceInside * 2 + lengthTextAdd = 10
                // spaceInside * 3 + (lengthTextAdd * 2) = 16
                // spaceInside * 4 + (lengthTextAdd * 3) = 22
                // spaceInside * 5 + (lengthTextAdd * 4) = 28
                return spaceInsideSeparate * position + lengthTextAdd * (position - 1)
            }

            val listNumbers = mutableListOf<Int>()
            for (i in 1..maxInterVal) {
                listNumbers.add(getNumberPosition(i))
            }


            edit.addTextChangedListener(object : TextWatcher {
                private var previousLength = 0
                private var isFormatting = false

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    previousLength = s?.length ?: 0
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // No se requiere ninguna acción en este método
                }

                override fun afterTextChanged(s: Editable?) {
                    if (isFormatting) {
                        // Evitar llamadas recursivas cuando se modifica el texto
                        return
                    }

                    val currentLength = s?.length ?: 0
                    val text = s?.toString()

                    if (currentLength > previousLength) {

                        if (currentLength in listNumbers) {
                            isFormatting = true

                            val selectionStart = edit.selectionStart
                            val formattedText = StringBuilder(text)
                            formattedText.insert(selectionStart, textAdd)
                            edit.setText(formattedText)
                            edit.setSelection(selectionStart + lengthTextAdd)

                            isFormatting = false
                        }
                    } else {
                        if (currentLength in listNumbers) {
                            isFormatting = true

                            try {
                                val selectionStart = edit.selectionStart
                                val formattedText = StringBuilder(text)
                                formattedText.delete(selectionStart - lengthTextAdd, selectionStart)
                                edit.setText(formattedText)
                                edit.setSelection(selectionStart - lengthTextAdd)
                            } catch (e: Exception){

                            }


                            isFormatting = false
                        }
                    }
                }
            })
        } catch (e: Exception){

        }


    }

}