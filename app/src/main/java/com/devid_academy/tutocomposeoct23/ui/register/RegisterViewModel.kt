package com.devid_academy.tutocomposeoct23.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devid_academy.tutocomposeoct23.MyPrefs
import com.devid_academy.tutocomposeoct23.NetworkResult
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

                    repository.register(login, password)

                }.let {

                    when (it) {
                        is NetworkResult.Success -> {

                            with(myPrefs){
                                user_id = it.responseBodyData.id
                                token = it.responseBodyData.token
                            }

                            _navSharedFlow.emit(true)
                            _userMessageSharedFlow.emit("Bonjour $login, votre compte a bien été créé.")

                        }
                        is NetworkResult.Error -> {

                            when(it.errorCode){
                                303 -> "Le nom d'tilisateur est déjà utilisé. (Erreur 303)"
                                304 -> "Le compte n'a pas pu être créé. Veuillez réessayer plus tard. (Erreur 304)"
                                400 -> "Le compte n'a pas pu être créé. Problème de paramètres. Veuillez contacter l'administrateur. (Erreur 400)"
                                503 -> "Le compte n'a pas pu être créé. Erreur de requête mysql. Veuillez contacter l'administrateur.(Erreur 503)"
                                else -> "Erreur. Le compte n'a pas pu être créé. Veuillez réessayer plus tard. "
                            }.let {errorMessage ->
                                _userMessageSharedFlow.emit(errorMessage)
                            }

                        }
                        is NetworkResult.Exception -> {
                            _userMessageSharedFlow.emit("Erreur réseau. Veuillez vérifier votre connexion Internet et réessayer.")
                        }
                    }
                }
            }
        }
    }
}
