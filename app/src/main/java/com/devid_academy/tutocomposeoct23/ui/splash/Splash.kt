package com.devid_academy.tutocomposeoct23.ui.splash

import android.window.SplashScreen
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.devid_academy.tutocomposeoct23.R
import com.devid_academy.tutocomposeoct23.Screen
import com.devid_academy.tutocomposeoct23.ui.theme.TutoComposeOct23Theme
import java.io.File


@Composable
fun SplashScreen(
    navController: NavController,
    viewModel : SplashViewModel
){
    SplashContent()

    LaunchedEffect(true){
        viewModel.navSharedFlow.collect{
            navController.navigate(it){
                popUpTo(Screen.Splash.route){
                    inclusive = true
                }
            }
        }
    }

    viewModel.navigateAfterDelay()
}

@Composable
fun SplashContent(){
    Column (modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally)
    {
        Image(painter = painterResource(R.drawable.feedarticles_logo),
            contentDescription = "Feed articles logo",
            modifier = Modifier.size(300.dp))

        Text(text = "Feed Articles",
            fontSize = 50.sp,
            fontWeight = FontWeight.Bold)
    }
}


@Preview(showBackground = true)
@Composable
fun SplashPreview() {
    TutoComposeOct23Theme {
        SplashContent()
    }
}