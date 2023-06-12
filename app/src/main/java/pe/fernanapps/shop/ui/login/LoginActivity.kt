package pe.fernanapps.shop.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import pe.fernanapps.shop.R
import pe.fernanapps.shop.databinding.ActivityLoginBinding
import pe.fernanapps.shop.domain.DataState
import pe.fernanapps.shop.domain.LoginError
import pe.fernanapps.shop.ui.main.MainActivity
import pe.fernanapps.shop.ui.splash.SplashActivity
import pe.fernanapps.shop.utils.DialogUtils
import pe.fernanapps.shop.utils.toast

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var bin: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bin = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(bin.root)
        initObserves()
        initActions()
    }

    fun toMainActivity() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun initObserves() {


        viewModel.loginState.observe(this, Observer { dataState ->
            when (dataState) {
                is DataState.Success<Boolean> -> {
                    //viewModel.getUserData()
                    // Momentaneo
                    hideProgressDialog()
                    if (dataState.data) {
                        toMainActivity()
                    }
                }

                is DataState.Error -> {
                    hideProgressDialog()
                    manageLoginErrorMessages(dataState.exception)
                }

                is DataState.Loading -> {
                    showProgressBar()
                }

                else -> Unit
            }
        })


    }

    private fun loginUser() {
        if (isUserDataOk()) {
            showProgressBar()

            val email = bin.email.text.toString().trim()
            val password = bin.password.text.toString().trim()

            viewModel.login(email, password)
        }
    }

    private fun isUserDataOk(): Boolean {
        return when {
            bin.email.text?.isEmpty() == true -> {
                this.toast(getString(R.string.login__error_enter_email))
                false
            }

            bin.password.text?.isEmpty() == true -> {
                this.toast(getString(R.string.login__error_enter_password))
                false
            }

            else -> true
        }
    }

    private fun manageLoginErrorMessages(exception: Throwable) {
        this.toast(
            getString(
                if (exception is LoginError) {
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

    private fun initActions() {
        with(bin) {

            back.setOnClickListener {

                val intent = Intent(this@LoginActivity, SplashActivity::class.java)
                intent.putExtra(
                    SplashActivity.INTENT_FROM_INSIDE_APP,
                    SplashActivity.INTENT_FROM_INSIDE_APP
                )
                startActivity(intent)
                finish()

            }

            login.setOnClickListener {
                loginUser()

            }


        }
    }


    private val progressDialog by lazy {
        DialogUtils.getProgressAlert(this, getString(R.string.one_moment_logging_in))
    }
    private fun hideProgressDialog() {
        bin.login.isEnabled = true
        progressDialog.dismiss()

    }

    private fun showProgressBar() {
        bin.login.isEnabled = false
        progressDialog.show()

    }
}