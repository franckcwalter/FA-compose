package com.devid_academy.tutocomposeoct23.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devid_academy.tutocomposeoct23.MyPrefs
import com.devid_academy.tutocomposeoct23.NetworkResult
import com.devid_academy.tutocomposeoct23.Screen
import com.devid_academy.tutocomposeoct23.network.ApiInterface
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
)
: ViewModel()
{

    private val _navSharedFlow = MutableSharedFlow<String>()
    val navSharedFlow = _navSharedFlow.asSharedFlow()

    private val _userMessageSharedFlow = MutableSharedFlow<String>()
    val userMessageSharedFlow = _userMessageSharedFlow.asSharedFlow()


    fun logInUser(login: String, password: String) {

        viewModelScope.launch {

            if(login.isBlank() || password.isBlank())
                _userMessageSharedFlow.emit("Veuillez remplir tous les champs.")
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
                            _userMessageSharedFlow.emit("Ravi de vous revoir, $login.")

                        }
                        is NetworkResult.Error -> {

                            when(it.errorCode){
                                304 -> "Problème de sécurité. Veuillez vous déconnecter et vous reconnecter. (Erreur 304)"
                                400 -> "Vous n'avez pas pu être connecté. Problème de paramètres. Veuillez contacter l'administrateur. (Erreur 400)"
                                401 -> "Le nom d'utilisateur ou le mot de passe est incorrect. Veuillez réessayer. (Erreur 401)"
                                503 -> "Le compte n'a pas pu être créé. Erreur de requête mysql. Veuillez contacter l'administrateur.(Erreur 503)"
                                else -> "Erreur. Vous n'avez pas pu être connecté. Veuillez réessayer plus tard. "
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

    fun navToRegister() {
        viewModelScope.launch {
            _navSharedFlow.emit(Screen.Register.route)
        }
    }
}
