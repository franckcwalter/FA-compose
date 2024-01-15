package com.devid_academy.tutocomposeoct23.ui.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devid_academy.tutocomposeoct23.MyPrefs
import com.devid_academy.tutocomposeoct23.NetworkResult
import com.devid_academy.tutocomposeoct23.R
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


    private val _articleToEditStateFlow =
        MutableStateFlow(ArticleDto(0, "", "","",0,"",0))
    val articleToEditStateFlow = _articleToEditStateFlow.asStateFlow()


    private val _navSharedFlow = MutableSharedFlow<Boolean>()
    val navSharedFlow = _navSharedFlow.asSharedFlow()

    private val _userMessageSharedFlow = MutableSharedFlow<Int>()
    val userMessageSharedFlow = _userMessageSharedFlow.asSharedFlow()


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
                                303 -> R.string.message_getarticle_article_not_found
                                400 -> R.string.message_getarticle_parameter_problem
                                401 -> R.string.message_getarticle_auth_problem
                                503 -> R.string.message_getarticle_mysql_problem
                                else -> R.string.message_getarticle_article_could_not_be_fetched
                            }.let { errorMessage ->
                                _userMessageSharedFlow.emit(errorMessage)
                            }
                        }

                        is NetworkResult.Exception -> {
                            _userMessageSharedFlow.emit(R.string.message_getarticle_article_could_not_be_fetched)
                        }
                    }
                }
            }
        }
    }

    fun updateArticle(
        idArticle : Long,
        title: String,
        description: String,
        imageUrl : String,
        selectedCategory: Int
    )
    {
        viewModelScope.launch {

            if(title.isBlank() || description.isBlank() || imageUrl.isBlank())
                _userMessageSharedFlow.emit(R.string.message_fill_out_all_fields)
            else {
                withContext(Dispatchers.IO){
                    repository.updateArticle(idArticle, myPrefs.token!!,
                                            title, description, imageUrl, selectedCategory)
                }.let {

                    when (it) {
                        is NetworkResult.Success -> {

                            _navSharedFlow.emit(true)
                            _userMessageSharedFlow.emit(R.string.message_article_updated)

                        }
                        is NetworkResult.Error -> {

                            when(it.errorCode){
                                303 -> R.string.message_article_not_updated_parameter_problem
                                304 -> R.string.message_article_not_updated
                                400 -> R.string.message_article_not_updated_parameter_problem
                                401 -> R.string.message_article_not_updated_auth_problem
                                503 -> R.string.message_article_not_updated_mysql_problem
                                else -> R.string.message_article_not_updated
                            }.let {errorMessage ->
                                _userMessageSharedFlow.emit(errorMessage)
                            }
                        }
                        is NetworkResult.Exception -> {
                            _userMessageSharedFlow.emit(R.string.message_article_not_updated)
                        }
                    }
                }
            }
        }
    }
}
