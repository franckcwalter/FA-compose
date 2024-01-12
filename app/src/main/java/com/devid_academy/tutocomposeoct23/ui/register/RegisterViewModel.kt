package com.devid_academy.tutocomposeoct23.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devid_academy.tutocomposeoct23.network.ApiInterface
import com.devid_academy.tutocomposeoct23.network.RegisterDto
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
    private val apiInterface : ApiInterface
) : ViewModel()
{

    private val _navSharedFlow = MutableSharedFlow<Boolean>()
    val navSharedFlow = _navSharedFlow.asSharedFlow()

    private val _userMessageSharedFlow = MutableSharedFlow<String>()
    val userMessageSharedFlow = _userMessageSharedFlow.asSharedFlow()

    fun registerUser(login: String, password: String, passwordConfirm: String) {

        viewModelScope.launch {

            if(login.isBlank() || password.isBlank() || passwordConfirm.isBlank())
                _userMessageSharedFlow.emit("Veuillez remplir tous les champs.")
            else if (password != passwordConfirm)
                _userMessageSharedFlow.emit("Les mots de passe ne correspondent pas.")
            else {

                withContext(Dispatchers.IO){
                    apiInterface.register(RegisterDto(login,password))
                }.let {

                    _userMessageSharedFlow.emit(it!!.body()!!.token)




                    /* TODO : SI LE USER EST BIEN ENREGISTRÉ */
                    /* TODO : ajouter les infos dans my prefs*/

                    _navSharedFlow.emit(true)



                }
            }
        }
    }
}
