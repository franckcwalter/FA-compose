package com.devid_academy.tutocomposeoct23.ui.main

import androidx.compose.material.DismissState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devid_academy.tutocomposeoct23.MyPrefs
import com.devid_academy.tutocomposeoct23.NetworkResult
import com.devid_academy.tutocomposeoct23.Screen
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
class MainViewModel
@Inject constructor(
    private var myPrefs: MyPrefs,
    private var repository: Repository
) : ViewModel()
{

    private val _articleList = MutableStateFlow(emptyList<ArticleDto>())
    val articleList = _articleList.asStateFlow()

    private val _selectedCategoryStateFlow = MutableStateFlow(0)
    val selectedCategoryStateFlow = _selectedCategoryStateFlow.asStateFlow()

    private val _expandedArticleIdStateFlow = MutableStateFlow(0L)
    val expandedArticleIdStateFlow = _expandedArticleIdStateFlow.asStateFlow()

    private val _articleWasDeletedStateFlow = MutableStateFlow(false)
    val articleWasDeletedStateFlow = _articleWasDeletedStateFlow.asStateFlow()


    private val _navSharedFlow = MutableSharedFlow<String>()
    val navSharedFlow = _navSharedFlow.asSharedFlow()

    private val _userMessageSharedFlow = MutableSharedFlow<String>()
    val userMessageSharedFlow = _userMessageSharedFlow.asSharedFlow()


    fun changeSelectedCategoryAndFetchArticles(selectedCategory: Int) {
        _selectedCategoryStateFlow.value = selectedCategory
        fetchArticles()
    }

    fun fetchArticles(){

        viewModelScope.launch {

            if (myPrefs.user_id > 0 && !myPrefs.token.isNullOrEmpty()) {

                withContext(Dispatchers.IO) {

                    repository.getArticles(myPrefs.token!!)

                }.let {
                    when (it) {
                        is NetworkResult.Success -> {

                            _articleList.value = it.responseBodyData

                            if (_selectedCategoryStateFlow.value > 0){
                                _articleList.value = it.responseBodyData.filter{ article ->
                                    article.categorie == _selectedCategoryStateFlow.value
                                }
                            }
                        }

                        is NetworkResult.Error -> {

                            when (it.errorCode) {
                                400 -> "Les articles n'ont pas pu être récupérés. Problème de paramètres. Veuillez contacter l'administrateur. (Erreur 400)"
                                401 -> "Accès non autorisé. Veuillez vous déconnecter puis vous reconnecter. (Erreur 401)"
                                503 -> "Les articles n'ont pas pu être récupérés. Erreur de requête mysql. Veuillez contacter l'administrateur.(Erreur 503)"
                                else -> "Erreur. Les articles n'ont pas pu être récupérés. Veuillez réessayer plus tard. "
                            }.let { errorMessage ->
                                _userMessageSharedFlow.emit(errorMessage)
                            }

                        }

                        is NetworkResult.Exception -> {
                            _userMessageSharedFlow.emit("Erreur. Les articles n'ont pas pu être récupérés. Veuillez réessayer plus tard.")
                        }

                    }
                }
            }
        }
    }

    fun navToCrea() {
        viewModelScope.launch{
            _navSharedFlow.emit(Screen.Crea.route)
        }
    }

    fun logoutUser() {

        viewModelScope.launch{

            _navSharedFlow.emit(Screen.Login.route)

            with(myPrefs){
                user_id = 0
                token = null
            }
        }
    }



    fun deleteArticle(articleId : Long, articleUserId : Long){

        if(myPrefs.user_id == articleUserId){
            viewModelScope.launch {
                withContext(Dispatchers.IO){
                    repository.deleteArticle(articleId, myPrefs.token!!)
                }.let {

                    when (it) {
                        is NetworkResult.Success -> {

                            _userMessageSharedFlow.emit("Votre article a bien été supprimé.")
                            _articleWasDeletedStateFlow.emit(true)
                            fetchArticles()

                        }
                        is NetworkResult.Error -> {

                            when(it.errorCode){
                                304 -> "L'article n'a pas pu être supprimé. Veuillez réessayer plus tard."
                                400 -> "L'article n'a pas pu être supprimé. Problème de paramètres. Veuillez contacter l'administrateur."
                                401 -> "L'article n'a pas pu être supprimé. Problème d'autorisation. Veuillez vous reconnecter."
                                503 -> "Le compte n'a pas pu être créé. Erreur de requête mysql. Veuillez contacter l'administrateur.(Erreur 503)"
                                else -> "Erreur. L'article n'a pas pu être supprimé. Veuillez réessayer plus tard. "
                            }.let {errorMessage ->
                                _userMessageSharedFlow.emit(errorMessage)
                            }

                        }
                        is NetworkResult.Exception -> {
                            _userMessageSharedFlow.emit("Erreur. L'article n'a pas pu être supprimé. Veuillez réessayer plus tard.")
                        }
                    }
                }
            }
        }
    }

    fun expandArticleOrGoToEdit(clickedArticleId : Long, clickedArticleUserId : Long) {

        if(clickedArticleUserId == myPrefs.user_id){
            viewModelScope.launch{
                _navSharedFlow.emit(Screen.Edit.route + "/$clickedArticleId" )
            }

        }else{

            if(expandedArticleIdStateFlow.value == clickedArticleId){
                _expandedArticleIdStateFlow.value = 0
            } else {
                _expandedArticleIdStateFlow.value = clickedArticleId

            }
        }
    }


}
