package com.devid_academy.tutocomposeoct23.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devid_academy.tutocomposeoct23.MyPrefs
import com.devid_academy.tutocomposeoct23.NetworkResult
import com.devid_academy.tutocomposeoct23.R
import com.devid_academy.tutocomposeoct23.Screen
import com.devid_academy.tutocomposeoct23.network.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
@Inject constructor(
    private val repository: Repository,
    private val myPrefs: MyPrefs
) : ViewModel()
{


    private val _navSharedFlow = MutableSharedFlow<String>()
    val navSharedFlow = _navSharedFlow.asSharedFlow()

    private val _userMessageSharedFlow = MutableSharedFlow<Int>()
    val userMessageSharedFlow = _userMessageSharedFlow.asSharedFlow()


    fun logInUser(login: String, password: String) {

        viewModelScope.launch {

            if(login.isBlank() || password.isBlank())
                _userMessageSharedFlow.emit(R.string.message_fill_out_all_fields)
            else {

                withContext(Dispatchers.IO){

                    repository.login(login, password)

                }.let {

                    when (it) {
                        is NetworkResult.Success -> {

                            with(myPrefs){
                                user_id = it.responseBodyData.id
                                token = it.responseBodyData.token
                            }

                            _navSharedFlow.emit(Screen.Main.route)
                            _userMessageSharedFlow.emit(R.string.welcome_message + R.string.rblabel_misc)

                        }
                        is NetworkResult.Error -> {

                            when(it.errorCode){
                                304 -> R.string.message_security_problem
                                400 -> R.string.message_login_failed_parameter_problem
                                401 -> R.string.message_login_failed_login_or_password_incorrect
                                503 -> R.string.message_login_failed_mysql_problem
                                else ->  R.string.message_login_failed
                            }.let {errorMessage ->
                                _userMessageSharedFlow.emit(errorMessage)
                            }

                        }
                        is NetworkResult.Exception -> {
                            _userMessageSharedFlow.emit(R.string.message_login_failed)
                        }
                    }
                }
            }
        }
    }

    fun navToRegister() {
        viewModelScope.launch {
            _navSharedFlow.emit(Screen.Register.route)
        }
    }
}
