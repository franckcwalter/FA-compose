package com.devid_academy.tutocomposeoct23.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devid_academy.tutocomposeoct23.MyPrefs
import com.devid_academy.tutocomposeoct23.NetworkResult
import com.devid_academy.tutocomposeoct23.R
import com.devid_academy.tutocomposeoct23.app.Screen
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

    private val _userMessageSharedFlow = MutableSharedFlow<Int>()
    val userMessageSharedFlow = _userMessageSharedFlow.asSharedFlow()


    fun changeSelectedCategoryAndFetchArticles(selectedCategory: Int) {
        _selectedCategoryStateFlow.value = selectedCategory
        fetchArticles()
    }

    fun fetchArticles() {

        viewModelScope.launch {

            if (myPrefs.user_id > 0 && !myPrefs.token.isNullOrEmpty()) {

                withContext(Dispatchers.IO) {

                    repository.getArticles(myPrefs.token!!)

                }.let {
                    when (it) {

                        is NetworkResult.Success -> {

                            _articleList.value = it.responseBodyData

                            if (_selectedCategoryStateFlow.value > 0) {
                                _articleList.value = it.responseBodyData.filter { article ->
                                    article.categorie == _selectedCategoryStateFlow.value
                                }
                            }
                        }

                        is NetworkResult.Error -> {

                            when (it.errorCode) {
                                401 -> R.string.message_fetch_articles_failed_auth_problem
                                503 -> R.string.message_fetch_articles_failed_mysql_error
                                else -> R.string.message_fetch_articles_failed
                            }.let { errorMessage ->
                                _userMessageSharedFlow.emit(errorMessage)
                            }
                        }

                        is NetworkResult.Exception -> {
                            _userMessageSharedFlow.emit(R.string.message_fetch_articles_failed)
                        }
                    }
                }
            }
        }
    }

    fun deleteArticle(
        articleId: Long,
        articleUserId: Long
    ) {
        if (myPrefs.user_id == articleUserId) {

            viewModelScope.launch {

                withContext(Dispatchers.IO) {

                    repository.deleteArticle(articleId, myPrefs.token!!)

                }.let {

                    when (it) {

                        is NetworkResult.Success -> {

                            _userMessageSharedFlow.emit(R.string.message_article_deleted)
                            _articleWasDeletedStateFlow.emit(true)
                            fetchArticles()

                        }

                        is NetworkResult.Error -> {

                            when (it.errorCode) {
                                304 -> R.string.message_article_not_deleted
                                400 -> R.string.message_article_not_deleted_parameter_problem
                                401 -> R.string.message_article_not_deleted_auth_problem
                                503 -> R.string.message_article_not_deleted_mysql_problem
                                else -> R.string.message_article_not_deleted
                            }.let { errorMessage ->
                                _userMessageSharedFlow.emit(errorMessage)
                            }
                        }

                        is NetworkResult.Exception -> {
                            _userMessageSharedFlow.emit(R.string.message_article_not_deleted)
                        }
                    }
                }
            }
        }
    }

    fun expandArticleOrGoToEdit(clickedArticleId: Long, clickedArticleUserId: Long) {

        if (clickedArticleUserId == myPrefs.user_id) {

            viewModelScope.launch {
                _navSharedFlow.emit(Screen.Edit.route + "/$clickedArticleId")
            }

        } else {

            if (expandedArticleIdStateFlow.value == clickedArticleId) {
                _expandedArticleIdStateFlow.value = 0
            } else {
                _expandedArticleIdStateFlow.value = clickedArticleId
            }
        }

    }

    fun navToCrea() {

        viewModelScope.launch {
            _navSharedFlow.emit(Screen.Crea.route)
        }
    }


    fun logoutUser() {

        viewModelScope.launch {

            _navSharedFlow.emit(Screen.Login.route)

            with(myPrefs) {
                user_id = 0
                token = null
            }
        }
    }
}