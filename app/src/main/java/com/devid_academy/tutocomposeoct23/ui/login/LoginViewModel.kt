package com.devid_academy.tutocomposeoct23.ui.login

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
@Inject constructor(

)
: ViewModel()
{
    private val _navSharedFlow = MutableSharedFlow<String>()
    val navSharedFlow = _navSharedFlow.asSharedFlow()


}