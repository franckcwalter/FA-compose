package com.devid_academy.tutocomposeoct23.ui.crea

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
class CreaViewModel
@Inject constructor(
    private val repository: Repository,
    private val myPrefs: MyPrefs
) : ViewModel(){


    private val _navSharedFlow = MutableSharedFlow<Boolean>()
    val navSharedFlow = _navSharedFlow.asSharedFlow()

    private val _userMessageSharedFlow = MutableSharedFlow<Int>()
    val userMessageSharedFlow = _userMessageSharedFlow.asSharedFlow()


    fun createArticle(
        title: String,
        description: String,
        imageUrl: String,
        selectedCategory: Int
    )
    {
        viewModelScope.launch {

            if(title.isBlank() || description.isBlank() || imageUrl.isBlank())
                _userMessageSharedFlow.emit(R.string.message_fill_out_all_fields)
            else {
                withContext(Dispatchers.IO){
                    repository.createArticle(myPrefs.token!!, myPrefs.user_id,
                                            title, description, imageUrl, selectedCategory)
                }.let {

                    when (it) {
                        is NetworkResult.Success -> {

                            _navSharedFlow.emit(true)
                            _userMessageSharedFlow.emit(R.string.message_article_created)

                        }
                        is NetworkResult.Error -> {

                            when(it.errorCode){
                                304 -> R.string.message_error_article_not_created
                                400 -> R.string.message_error_article_not_created_parameter_problem
                                401 -> R.string.message_error_article_not_created_auth_problem
                                503 -> R.string.message_error_article_not_created_mysql_problem
                                else -> R.string.message_error_article_not_created
                            }.let {errorMessage ->
                                _userMessageSharedFlow.emit(errorMessage)
                            }
                        }
                        is NetworkResult.Exception -> {
                            _userMessageSharedFlow.emit(R.string.message_error_article_not_created)
                        }
                    }
                }
            }
        }
    }
}
