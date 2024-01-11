package com.devid_academy.tutocomposeoct23.ui.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditViewModel
@Inject constructor(
) : ViewModel(){

    private val _navSharedFlow = MutableSharedFlow<Boolean>()
    val navSharedFlow = _navSharedFlow.asSharedFlow()


    fun createArticle(title: String, description: String, imageUrl: String, selectedCategory: Int) {

        /*TODO : send info to API to creat article */

        viewModelScope.launch {
            _navSharedFlow.emit(true)
        }
    }
}
