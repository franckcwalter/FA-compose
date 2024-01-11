package com.devid_academy.tutocomposeoct23.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devid_academy.tutocomposeoct23.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
@Inject constructor(

)
: ViewModel()
{

    private val _navSharedFlow = MutableSharedFlow<String>()
    val navSharedFlow = _navSharedFlow.asSharedFlow()

    fun logInUser(login: String, password: String) {

        viewModelScope.launch {

            /* TODO : if login et mdp are correct
            *   add id et token to myPref puis naviguer : */

            _navSharedFlow.emit(Screen.Main.route)
        }
    }

    fun navToRegister() {
        viewModelScope.launch {
            _navSharedFlow.emit(Screen.Register.route)
        }
    }
}
