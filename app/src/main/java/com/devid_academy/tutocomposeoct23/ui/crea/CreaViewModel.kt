package com.devid_academy.tutocomposeoct23.ui.crea

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devid_academy.tutocomposeoct23.MyPrefs
import com.devid_academy.tutocomposeoct23.NetworkResult
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
class CreaViewModel
@Inject constructor(
    private val repository: Repository,
    private val myPrefs: MyPrefs
) : ViewModel(){

    private val _navSharedFlow = MutableSharedFlow<Boolean>()
    val navSharedFlow = _navSharedFlow.asSharedFlow()

    private val _userMessageSharedFlow = MutableSharedFlow<String>()
    val userMessageSharedFlow = _userMessageSharedFlow.asSharedFlow()

    fun editArticle(title: String, description: String, imageUrl: String, selectedCategory: Int) {
        viewModelScope.launch{



            if(title.isBlank() || description.isBlank() || imageUrl.isBlank())
                _userMessageSharedFlow.emit("Veuillez remplir tous les champs.")
            else {
                withContext(Dispatchers.IO){
                    repository.createArticle(myPrefs.token!!, myPrefs.user_id, title, description, imageUrl, selectedCategory)
                }.let {

                    when (it) {
                        is NetworkResult.Success -> {

                            _navSharedFlow.emit(true)
                            _userMessageSharedFlow.emit("Votre article a bien été créé.")

                        }
                        is NetworkResult.Error -> {

                            when(it.errorCode){
                                303 -> "L'article n'a pas pu être mis à jour. Problème de paramètres. Veuillez contacter l'administrateur."
                                304 -> "L'article n'a pas pu être mis à jour. Veuillez réessayer plus tard."
                                400 -> "L'article n'a pas pu être mis à jour. Problème de paramètres. Veuillez contacter l'administrateur."
                                401 -> "L'article n'a pas pu être mis à jour. Problème d'autorisation. Veuillez vous reconnecter."
                                503 -> "Le compte n'a pas pu être créé. Erreur de requête mysql. Veuillez contacter l'administrateur.(Erreur 503)"
                                else -> "Erreur. L'article n'a pas pu être mis à jour. Veuillez réessayer plus tard. "
                            }.let {errorMessage ->
                                _userMessageSharedFlow.emit(errorMessage)
                            }

                        }
                        is NetworkResult.Exception -> {
                            _userMessageSharedFlow.emit("Erreur. L'article n'a pas pu être mis à jour. Veuillez réessayer plus tard.")
                        }
                    }
                }
            }
        }
    }
}
