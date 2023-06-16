package pe.fernanapps.shop.ui.screens.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import pe.fernanapps.shop.ui.activities.SplashScreen01

//@Composable
//fun SplashScreen2(viewModel: SplashViewModel) {
//
//    val isLoading: Boolean by viewModel.loading.observeAsState(initial = true)
//
//    ConstraintLayout(
//        modifier = Modifier.fillMaxSize()
//    ) {
//        val splash01 = createRef()
//        val splash02 = createRef()
//
//        if(isLoading){
//            Box(
//                modifier = Modifier
//                    .constrainAs(splash01) {
//                        top.linkTo(parent.top)
//                        bottom.linkTo(parent.bottom)
//                        start.linkTo(parent.start)
//                        end.linkTo(parent.end)
//                    }
//            ) {
//                SplashScreen01()
//            }
//        } else {
//            Box(
//                modifier = Modifier
//                    .constrainAs(splash02) {
//                        top.linkTo(parent.top)
//                        bottom.linkTo(parent.bottom)
//                        start.linkTo(parent.start)
//                        end.linkTo(parent.end)
//                    }
//            ) {
//                SplashScreen02()
//            }
//        }
//
//    }
//
//
//    viewModel.initSplashScreen()


@Composable
fun SplashScreen(viewModel: SplashViewModel) {

    val isLoading: Boolean by viewModel.loading.observeAsState(initial = true)

    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val splash01 = createRef()
        val splash02 = createRef()

        Crossfade(
            targetState = isLoading,
            animationSpec = tween(durationMillis = 2000)
        ) { loadingState ->
            if (loadingState) {
                Box(
                    modifier = Modifier
                        .constrainAs(splash01) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                ) {
                    SplashScreen01()
                }
            } else {
                Box(
                    modifier = Modifier
                        .constrainAs(splash02) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                ) {
                    SplashScreen02()
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.initSplashScreen()
    }
}

//@Composable
//fun SplashScreen4(viewModel: SplashViewModel) {
//
//    val isLoading: Boolean by viewModel.loading.observeAsState(initial = true)
//
//    ConstraintLayout(
//        modifier = Modifier.fillMaxSize()
//    ) {
//        val splash01 = createRef()
//        val splash02 = createRef()
//
//        AnimatedVisibility(visible = isLoading) {
//            Box(
//                modifier = Modifier
//                    .constrainAs(splash01) {
//                        top.linkTo(parent.top)
//                        bottom.linkTo(parent.bottom)
//                        start.linkTo(parent.start)
//                        end.linkTo(parent.end)
//                    }
//            ) {
//                SplashScreen01()
//            }
//        }
//
//        AnimatedVisibility(visible = !isLoading) {
//            Box(
//                modifier = Modifier
//                    .constrainAs(splash02) {
//                        top.linkTo(parent.top)
//                        bottom.linkTo(parent.bottom)
//                        start.linkTo(parent.start)
//                        end.linkTo(parent.end)
//                    }
//            ) {
//                SplashScreen02()
//            }
//        }
//    }
//
//    LaunchedEffect(Unit) {
//        viewModel.initSplashScreen()
//    }
//}


