package pe.fernanapps.shop.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import pe.fernanapps.shop.ui.screens.login.LoginScreen
import pe.fernanapps.shop.ui.screens.login.LoginViewModel
import pe.fernanapps.shop.ui.theme.FXEcommerceShopTheme


@AndroidEntryPoint
class SignUpActivity : ComponentActivity() {

    private val viewModel by viewModels<LoginViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FXEcommerceShopTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    LoginScreen(viewModel)
                }
            }
        }
    }
}


