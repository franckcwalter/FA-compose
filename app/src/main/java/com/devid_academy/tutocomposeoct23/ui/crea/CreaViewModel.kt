package com.devid_academy.tutocomposeoct23.ui.crea

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreaViewModel
@Inject constructor(

) : ViewModel(){

    private val _navSharedFlow = MutableSharedFlow<Boolean>()
    val navSharedFlow = _navSharedFlow.asSharedFlow()

    fun editArticle(title: String, description: String, imageUrl: String, selectedCategory: Int) {
        viewModelScope.launch{
            _navSharedFlow.emit(true)
        }
    }
}
