package com.devid_academy.tutocomposeoct23.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devid_academy.tutocomposeoct23.MyPrefs
import com.devid_academy.tutocomposeoct23.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel
@Inject constructor(
    private var myPrefs: MyPrefs
) : ViewModel()
{
    private val _navSharedFlow = MutableSharedFlow<String>()
    val navSharedFlow = _navSharedFlow.asSharedFlow()

    fun navigateAfterDelay(){

        viewModelScope.launch {

            delay(1500)
            (if (myPrefs.user_id > 0 && !myPrefs.token.isNullOrEmpty())
                Screen.Main.route
            else Screen.Login.route)
                .let { _navSharedFlow.emit(it) }
        }
    }
}