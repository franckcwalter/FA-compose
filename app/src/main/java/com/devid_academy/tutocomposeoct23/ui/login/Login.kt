package com.devid_academy.tutocomposeoct23.ui.login

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.devid_academy.tutocomposeoct23.Screen
import com.devid_academy.tutocomposeoct23.toast
import com.devid_academy.tutocomposeoct23.ui.splash.SplashContent
import com.devid_academy.tutocomposeoct23.ui.splash.SplashViewModel
import com.devid_academy.tutocomposeoct23.ui.theme.TutoComposeOct23Theme


@Composable
fun LoginScreen(
    navController: NavController,
    viewModel : LoginViewModel
){
    LoginContent(
        onLogInButtonClicked =  { login, password ->
            viewModel.logInUser(login, password)},
        onRegisterButtonClicked = { viewModel.navToRegister() }
    )

    LaunchedEffect(true){
        viewModel.navSharedFlow.collect{
            navController.navigate(it){
                if (it == Screen.Main.route){
                    popUpTo(Screen.Login.route){
                        inclusive = true
                    }
                }
            }
        }
    }

    val context = LocalContext.current
    LaunchedEffect(true){
        viewModel.userMessageSharedFlow.collect{
            context.toast(it)
        }
    }

}

@Composable
fun LoginContent(
    onLogInButtonClicked : (String, String) -> Unit,
    onRegisterButtonClicked : () -> Unit
){

    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()){

        Text(text = "Se connecter",
            modifier = Modifier.align(Alignment.TopCenter).padding(top= 48.dp))

        Column (verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize())
        {

            Column {

                Spacer(Modifier.height(48.dp))

                TextField(value = login,
                    onValueChange = {login = it},
                    label = { Text("Login") })

                Spacer(Modifier.height(24.dp))

                TextField(value = password,
                    onValueChange = {password = it},
                    label = { Text("Mot de passe") })
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally)
            {

                Button(onClick = { onLogInButtonClicked.invoke(login, password) })
                {
                    Text("Se connecter")
                }

                Spacer(Modifier.height(24.dp))

                Text(text = "Pas de compte ? Inscrivez-vous !",
                    modifier = Modifier.clickable { onRegisterButtonClicked.invoke() })
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    TutoComposeOct23Theme {
       // LoginContent(){_,_-> }
    }
}