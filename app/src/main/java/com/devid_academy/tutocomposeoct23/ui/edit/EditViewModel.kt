package com.devid_academy.tutocomposeoct23.ui.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devid_academy.tutocomposeoct23.MyPrefs
import com.devid_academy.tutocomposeoct23.NetworkResult
import com.devid_academy.tutocomposeoct23.network.ArticleDto
import com.devid_academy.tutocomposeoct23.network.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EditViewModel
@Inject constructor(
    private val repository: Repository,
    private val myPrefs: MyPrefs
) : ViewModel(){



    private val _navSharedFlow = MutableSharedFlow<Boolean>()
    val navSharedFlow = _navSharedFlow.asSharedFlow()

    private val _userMessageSharedFlow = MutableSharedFlow<String>()
    val userMessageSharedFlow = _userMessageSharedFlow.asSharedFlow()


    private val _articleToEditStateFlow =  MutableStateFlow(ArticleDto(0, "", "","",0,"",0))
    val articleToEditStateFlow = _articleToEditStateFlow.asStateFlow()



    fun fetchArticle(articleId: Long){

        viewModelScope.launch {

            if (myPrefs.user_id > 0 && !myPrefs.token.isNullOrEmpty()) {

                withContext(Dispatchers.IO) {

                    repository.getArticle(articleId, myPrefs.token!!)

                }.let {
                    when (it) {
                        is NetworkResult.Success -> {

                            _articleToEditStateFlow.value = it.responseBodyData

                        }

                        is NetworkResult.Error -> {

                            when (it.errorCode) {
                                303 -> "L'article demandé n'existe pas. (Erreur 303)"
                                400 -> "L'article n'a pas pu être récupéré. Problème de paramètres. Veuillez contacter l'administrateur. (Erreur 400)"
                                401 -> "Accès non autorisé. Veuillez vous déconnecter puis vous reconnecter. (Erreur 401)"
                                503 -> "L'article n'a pas pu être récupéré. Erreur de requête mysql. Veuillez contacter l'administrateur.(Erreur 503)"
                                else -> "Erreur. article n'a pas pu être récupéré. Veuillez réessayer plus tard. "
                            }.let { errorMessage ->
                                _userMessageSharedFlow.emit(errorMessage)
                            }
                        }

                        is NetworkResult.Exception -> {
                            _userMessageSharedFlow.emit("Erreur. article n'a pas pu être récupéré. Veuillez réessayer plus tard.")
                        }
                    }
                }
            }
        }
    }



    fun updateArticle(idArticle : Long, title: String, description: String, imageUrl: String, selectedCategory: Int) {

        viewModelScope.launch {

            if(title.isBlank() || description.isBlank() || imageUrl.isBlank())
                _userMessageSharedFlow.emit("Veuillez remplir tous les champs.")
            else {
                withContext(Dispatchers.IO){
                    repository.updateArticle(idArticle, myPrefs.token!!, title, description, imageUrl, selectedCategory)
                }.let {

                    when (it) {
                        is NetworkResult.Success -> {

                            _navSharedFlow.emit(true)
                            _userMessageSharedFlow.emit("Votre article a bien été mis à jour.")

                        }
                        is NetworkResult.Error -> {

                            when(it.errorCode){
                                303 -> "L'article n'a pas pu être mis à jour. Problème de paramètres. Veuillez contacter l'administrateur. (Erreur 303)"
                                304 -> "L'article n'a pas pu être mis à jour. Veuillez réessayer plus tard. (Erreur 304)"
                                400 -> "L'article n'a pas pu être mis à jour. Problème de paramètres. Veuillez contacter l'administrateur. (Erreur 400)"
                                401 -> "L'article n'a pas pu être mis à jour. Problème d'autorisation. Veuillez vous reconnecter. (Erreur 401)"
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
