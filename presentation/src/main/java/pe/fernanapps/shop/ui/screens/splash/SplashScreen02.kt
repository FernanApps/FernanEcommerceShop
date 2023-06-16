package pe.fernanapps.shop.ui.screens.splash

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import pe.fernanapps.shop.R
import pe.fernanapps.shop.ui.LoginActivity
import pe.fernanapps.shop.ui.SignUpActivity
import pe.fernanapps.shop.ui.utils.paddingRes


@Preview
@Composable
fun SplashScreen02() {
    val context = LocalContext.current

    val modifierText = Modifier.padding(vertical = 5.dp)

    val modifier =
        Modifier
            .paddingRes(horizontal = R.dimen.padding_general)
            .padding(vertical = 2.dp)


    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {

        val constraints = createRefs()

        val (
            imageView,
            loginButton,
            signUpButton,
            logo,
        ) = constraints

        val guideline = createGuidelineFromTop(0.5f)

        Image(
            painter = painterResource(R.drawable.login),
            contentDescription = null,
            modifier = Modifier
                .constrainAs(imageView) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Button(
            onClick = { toActivity(context, LoginActivity::class.java) },
            colors = ButtonDefaults.elevatedButtonColors(containerColor = Color.White),
            modifier = modifier.constrainAs(loginButton) {
                bottom.linkTo(signUpButton.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }
        ) {
            Text(
                text = stringResource(R.string.login),
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = modifierText
            )
        }

        OutlinedButton(
            onClick = { toActivity(context, SignUpActivity::class.java) },
            border = BorderStroke(2.dp, Color.White),
            modifier = modifier
                .constrainAs(signUpButton) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints

                }
                .paddingRes(bottom = R.dimen.padding_general)

        ) {
            Text(
                text = stringResource(R.string.sign_up),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily.SansSerif,
                modifier = modifierText
            )
        }

        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = null,
            modifier = Modifier
                .constrainAs(logo) {
                    bottom.linkTo(guideline)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }

                .width(300.dp)
                .height(100.dp),
            contentScale = ContentScale.Fit
        )


        createGuidelineFromTop(0.5f)
    }
}


fun toActivity(context: Context, activityClass: Class<*>) {
    val activity: Activity? =
        if (context is Fragment) (context).requireActivity() else context as? Activity

    activity?.apply {
        startActivity(Intent(this, activityClass))
        finish()
    }
}

