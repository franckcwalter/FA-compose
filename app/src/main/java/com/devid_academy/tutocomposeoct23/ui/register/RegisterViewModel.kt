package com.devid_academy.tutocomposeoct23.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devid_academy.tutocomposeoct23.MyPrefs
import com.devid_academy.tutocomposeoct23.NetworkResult
import com.devid_academy.tutocomposeoct23.R
import com.devid_academy.tutocomposeoct23.network.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel
@Inject constructor(
    private val repository: Repository,
    private val myPrefs: MyPrefs
) : ViewModel()
{

    private val _navSharedFlow = MutableSharedFlow<Boolean>()
    val navSharedFlow = _navSharedFlow.asSharedFlow()

    private val _userMessageSharedFlow = MutableSharedFlow<Int>()
    val userMessageSharedFlow = _userMessageSharedFlow.asSharedFlow()

    fun registerUser(
        login: String,
        password: String,
        passwordConfirm: String
    )
    {

        viewModelScope.launch {

            if(login.isBlank() || password.isBlank() || passwordConfirm.isBlank())
                _userMessageSharedFlow.emit(R.string.message_fill_out_all_fields)
            else if (password != passwordConfirm)
                _userMessageSharedFlow.emit(R.string.message_passwords_do_not_match)
            else {

                withContext(Dispatchers.IO){

                    repository.register(login, password)

                }.let {

                    when (it) {
                        is NetworkResult.Success -> {

                            with(myPrefs){
                                user_id = it.responseBodyData.id
                                token = it.responseBodyData.token
                            }

                            _navSharedFlow.emit(true)
                            _userMessageSharedFlow.emit(R.string.message_account_created)

                        }
                        is NetworkResult.Error -> {

                            when(it.errorCode){
                                303 -> R.string.message_account_not_created_login_already_in_use
                                304 -> R.string.message_account_not_created
                                400 -> R.string.message_account_not_created_parameter_problem
                                503 -> R.string.message_account_not_created_myssql_error
                                else -> R.string.message_account_not_created
                            }.let {errorMessage ->
                                _userMessageSharedFlow.emit(errorMessage)
                            }
                        }
                        is NetworkResult.Exception -> {
                            _userMessageSharedFlow.emit(R.string.message_account_not_created)
                        }
                    }
                }
            }
        }
    }
}
