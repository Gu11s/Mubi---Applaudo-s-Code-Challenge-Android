package com.gdevs.mubi.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.gdevs.mubi.presentation.navigation.AppNavigation
import com.gdevs.mubi.presentation.ui.theme.MubiTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MubiTheme {
                Surface{
                    val context = LocalContext.current
                    AppNavigation(context)
                }
            }
        }
    }
}