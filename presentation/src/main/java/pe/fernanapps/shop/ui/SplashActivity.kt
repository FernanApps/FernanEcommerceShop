package pe.fernanapps.shop.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import pe.fernanapps.shop.ui.screens.splash.SplashScreen
import pe.fernanapps.shop.ui.screens.splash.SplashViewModel
import pe.fernanapps.shop.ui.theme.FXEcommerceShopTheme

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : ComponentActivity() {

    companion object {
        val INTENT_FROM_INSIDE_APP = "INTENT_FROM_INSIDE_APP"
    }

    private val viewModel by viewModels<SplashViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.extras?.getBoolean(INTENT_FROM_INSIDE_APP) == true) {
            println("Extras ${intent.extras.toString()}")
            viewModel.timeDefault = 0L
        }

        setContent {
            FXEcommerceShopTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SplashScreen(viewModel)
                }
            }
        }
    }
}















//class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {
//
//
//
//    private val viewModel by viewModels<SplashViewModel>()
//
//    fun showSignUpOrLogin(show: Boolean){
//        bin.splash02.root.isVisible = show
//        bin.splash01.root.isVisible = !show
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        showSignUpOrLogin(false)
//        println("Antes")
//        if (intent.extras?.getBoolean(INTENT_FROM_INSIDE_APP) == true) {
//            println("Extras ${intent.extras.toString()}")
//            showSignUpOrLogin(true)
//        } else {
//            println("No Extras")
//            viewModel.initSplashScreen()
//
//        }
//
//    }
//
//    private val progressDialog by lazy {
//        DialogUtils.getProgressAlert(this, getString(R.string.one_moment_verifying))
//    }
//
//    override fun initObserves() {
//        viewModel.loading.observe(this, Observer {
//            println("viewModel Loading $it")
//            if (!it) {
//                showSignUpOrLogin(true)
//                viewModel.checkSessionAvailability()
//            }
//        })
//        viewModel.isSessionAvailable.observe(this, Observer { dataState ->
//            when(dataState){
//                is DataState.Success<Boolean> -> {
//                    progressDialog.dismiss()
//                    if(dataState.data){
//                        toMainActivity()
//                    }
//                }
//                is DataState.Error -> progressDialog.dismiss()
//                is DataState.Loading -> progressDialog.show()
//                else -> Unit
//            }
//        })
//
//    }
//
//    private fun toMainActivity() {
//        val intent = Intent(this@SplashActivity, MainActivity::class.java)
//        startActivity(intent)
//        finish()
//        return
//
//    }
//
//    override fun initActions() {
//        with(bin.splash02) {
//            signUp.setOnClickListener {
//                val intent = Intent(this@SplashActivity, OnBoardingActivity::class.java)
//                startActivity(intent)
//                finish()
//
//            }
//            login.setOnClickListener {
//                val intent = Intent(this@SplashActivity, LoginActivity::class.java)
//                startActivity(intent)
//                finish()
//
//            }
//        }
//    }
//}








