package com.devid_academy.tutocomposeoct23.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.devid_academy.tutocomposeoct23.R
import com.devid_academy.tutocomposeoct23.app.Screen
import com.devid_academy.tutocomposeoct23.ui.theme.FeedArticlesComposeTheme


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

    Column (modifier = Modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colors.primary),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally)
    {
        Image(painter = painterResource(R.drawable.feedarticles_logo),
            contentDescription = stringResource(R.string.desc_logo) ,
            modifier = Modifier.size(300.dp))

        Text(text = stringResource(R.string.app_name),
            fontSize = 50.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.background)
    }
}

@Preview(showBackground = true)
@Composable
fun SplashPreview() {
    FeedArticlesComposeTheme {
        SplashContent()
    }
}