package com.devid_academy.tutocomposeoct23.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel
@Inject constructor(

) : ViewModel()
{

    private val _navSharedFlow = MutableSharedFlow<Boolean>()
    val navSharedFlow = _navSharedFlow.asSharedFlow()

    fun registerUser(login: String, password: String, passwordConfirm: String) {


        viewModelScope.launch {

            /* TODO : SI LE USER EST BIEN ENREGISTRÉ */

            _navSharedFlow.emit(true)

        }
    }
}
