package pe.fernanapps.shop.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import pe.fernanapps.shop.R
import pe.fernanapps.shop.databinding.ActivitySplashBinding
import pe.fernanapps.shop.domain.DataState
import pe.fernanapps.shop.ui.base.BaseActivity
import pe.fernanapps.shop.ui.login.LoginActivity
import pe.fernanapps.shop.ui.main.MainActivity
import pe.fernanapps.shop.ui.onboarding.OnBoardingActivity
import pe.fernanapps.shop.utils.DialogUtils


@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {

    companion object {
        val INTENT_FROM_INSIDE_APP = "INTENT_FROM_INSIDE_APP"
    }

    private val viewModel by viewModels<SplashViewModel>()

    fun showSignUpOrLogin(){
        bin.splash02.root.visibility = View.VISIBLE
        bin.splash01.root.visibility = View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.extras?.getBoolean(INTENT_FROM_INSIDE_APP) != null) {
            showSignUpOrLogin()
        } else {
            viewModel.initSplashScreen()


        }

    }

    private val progressDialog by lazy {
        DialogUtils.getProgressAlert(this, getString(R.string.one_moment_verifying))
    }

    override fun initObserves() {
        viewModel.loading.observe(this, Observer {
            if (!it) {
                showSignUpOrLogin()
                viewModel.checkSessionAvailability()
            }
        })
        viewModel.isSessionAvailable.observe(this, Observer { dataState ->
            when(dataState){
                is DataState.Success<Boolean> -> {
                    progressDialog.dismiss()
                    if(dataState.data){
                        toMainActivity()
                    }
                }
                is DataState.Error -> progressDialog.dismiss()
                is DataState.Loading -> progressDialog.show()
                else -> Unit
            }
        })

    }

    private fun toMainActivity() {
        val intent = Intent(this@SplashActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
        return

    }

    override fun initActions() {
        with(bin.splash02) {
            signUp.setOnClickListener {
                val intent = Intent(this@SplashActivity, OnBoardingActivity::class.java)
                startActivity(intent)
                finish()

            }
            login.setOnClickListener {
                val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()

            }
        }
    }
}


