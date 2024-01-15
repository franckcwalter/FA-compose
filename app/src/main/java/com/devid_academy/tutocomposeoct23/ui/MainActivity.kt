package com.devid_academy.tutocomposeoct23.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.devid_academy.tutocomposeoct23.app.AppNavigation
import com.devid_academy.tutocomposeoct23.ui.theme.FeedArticlesComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FeedArticlesComposeTheme {

                Surface(modifier = Modifier.fillMaxSize(),
                     color = MaterialTheme.colors.background)
                { AppNavigation() }
            }
        }
    }
}