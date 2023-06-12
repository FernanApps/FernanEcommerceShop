package pe.fernanapps.shop.ui.signUp

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import pe.fernanapps.shop.R
import pe.fernanapps.shop.databinding.ActivitySignUpBinding
import pe.fernanapps.shop.domain.DataState
import pe.fernanapps.shop.domain.LoginError
import pe.fernanapps.shop.domain.model.user.User
import pe.fernanapps.shop.ui.main.MainActivity
import pe.fernanapps.shop.ui.splash.SplashActivity
import pe.fernanapps.shop.utils.DialogUtils
import pe.fernanapps.shop.utils.toast


@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {

    private lateinit var bin: ActivitySignUpBinding
    private val viewModel: SignUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.bin = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(bin.root)

        initObservers()
        initViews()
        initActions()

    }


    private fun initObservers(){
        viewModel.signUpState.observe(this, Observer { dataState ->
            when (dataState) {
                is DataState.Success<User> -> {
                    //viewModel.saveUser(user = dataState.data)
                    hideProgressDialog()
                    showSuccesfull()
                }
                is DataState.Error -> {
                    hideProgressDialog()
                    manageRegisterErrorMessages(dataState.exception)
                }
                is DataState.Loading -> {
                    showProgressBar()
                }
                else -> Unit
            }
        })
//        viewModel.saveUserState.observe(this, Observer { dataState ->
//            when (dataState) {
//                is DataState.Success<Boolean> -> {
//                    this.toast(getString(R.string.signup__signup_successfully))
//                    //bin.back.performClick()
//                    showSuccesfull()
//                }
//                is DataState.Error -> {
//                    hideProgressDialog()
//                    manageRegisterErrorMessages(dataState.exception)
//                }
//                is DataState.Loading -> {
//                    showProgressBar()
//                }
//                else -> {}
//            }
//        })
    }
    private fun initViews(){

        for (i in 0 until (bin.inputLayoutParent as ViewGroup).childCount) {
            val childView = bin.inputLayoutParent.getChildAt(i)
            if(childView is TextInputLayout){
                childView.typeface = Typeface.DEFAULT_BOLD
            }
        }
    }

    private fun initActions(){
        with(bin){
            back.setOnClickListener {

                val intent = Intent(this@SignUpActivity, SplashActivity::class.java)
                intent.putExtra(SplashActivity.INTENT_FROM_INSIDE_APP, SplashActivity.INTENT_FROM_INSIDE_APP)
                startActivity(intent)
                finish()

            }
            signUp.setOnClickListener {
                if(!acceptTerms.isChecked){
                    acceptTerms.requestFocus()
                    this@SignUpActivity.toast(R.string.need_terms_conds)
                    return@setOnClickListener
                }
                if (isUserDataOk()) {
                    val name = bin.name.text.toString()
                    val email = bin.email.text.toString()

                    viewModel.signUp(
                        User(email = email, name = name),
                        bin.password.text.toString()
                    )
                }
                // Its only Example >>>> showSuccesfull()

            }

            acceptTerms.setOnCheckedChangeListener { buttonView, isChecked ->
                if(isChecked){
                    signUp.alpha = 1f
                } else {
                    signUp.alpha = 0.5f
                }
            }

            startShopping.setOnClickListener {
                val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

        }

    }
    private fun isUserDataOk(): Boolean {
        return when {
            bin.email.text?.isEmpty() == true -> {
                this.toast(getString(R.string.login__error_enter_email))
                false
            }
            isPasswordInsecure() -> false
            else -> true

        }
    }

    private fun isPasswordInsecure(): Boolean {

        return if (bin.password.text.toString().length < 6) {
            this.toast(getString(R.string.signup__error_password_insecure))
            true
        } else if(bin.password.text.toString() != bin.confirmPassword.text.toString()){
            this.toast(getString(R.string.signup__error_passwords_match))
            true
        } else {
            false
        }
    }
    private fun manageRegisterErrorMessages(exception: Throwable) {
        this.toast(
            getString(
                if(exception is LoginError){
                    when (exception) {
                        is LoginError.AuthInvalidUserException -> R.string.auth_error__user_not_exist
                        is LoginError.AuthWeakPasswordException -> R.string.auth_error__weak_password
                        is LoginError.AuthInvalidCredentialsException -> R.string.auth_error__invalid_credentials
                        is LoginError.AuthUserCollisionException -> R.string.auth_error__user_collision
                        is LoginError.AuthRecentLoginRequiredException -> R.string.auth_error__recent_login_required
                        is LoginError.AuthActionCodeException -> R.string.auth_error__action_code
                        else -> R.string.signup__error_unknown_error
                    }
                } else {
                    R.string.signup__error_unknown_error
                }

            )
        )
    }
    private fun showSuccesfull(){
        bin.main.visibility = View.GONE
        bin.successful.visibility = View.VISIBLE
    }

    private val progressDialog by lazy {
        DialogUtils.getProgressAlert(this, getString(R.string.one_moment_sign_up))
    }



    private fun hideProgressDialog() {
        progressDialog.dismiss()
        bin.signUp.isEnabled = true
    }

    private fun showProgressBar() {
        progressDialog.show()
        bin.signUp.isEnabled = false
    }
}