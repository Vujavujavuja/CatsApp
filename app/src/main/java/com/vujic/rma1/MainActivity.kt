package com.vujic.rma1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.vujic.rma1.navigation.AppNavigation
import com.vujic.rma1.ui.theme.VujicTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VujicTheme {
                AppNavigation()
            }
        }
    }
}