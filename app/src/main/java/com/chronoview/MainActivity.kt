package com.chronoview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chronoview.ui.ChronoViewApp
import com.chronoview.ui.theme.ChronoViewTheme
import com.chronoview.viewmodel.ChronoViewViewModel
import com.chronoview.viewmodel.ChronoViewViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChronoViewTheme {
                val chronoViewModel: ChronoViewViewModel = viewModel(
                    factory = ChronoViewViewModelFactory(applicationContext)
                )
                ChronoViewApp(viewModel = chronoViewModel)
            }
        }
    }
}
