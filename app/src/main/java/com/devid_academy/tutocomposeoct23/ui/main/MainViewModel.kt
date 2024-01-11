package com.devid_academy.tutocomposeoct23.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devid_academy.tutocomposeoct23.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject constructor(

) : ViewModel()
{
    private val _navSharedFlow = MutableSharedFlow<String>()
    val navSharedFlow = _navSharedFlow.asSharedFlow()

    fun navToCrea() {
        viewModelScope.launch{
            _navSharedFlow.emit(Screen.Crea.route)
        }
    }

    fun logoutUser() {
        viewModelScope.launch{

            /*TODO :  DELETE INFO FROM MR PREFS  */
            _navSharedFlow.emit(Screen.Login.route)
        }
    }
}
