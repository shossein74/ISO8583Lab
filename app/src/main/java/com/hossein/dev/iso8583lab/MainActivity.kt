package com.hossein.dev.iso8583lab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hossein.dev.iso8583lab.data.StanDataStore
import com.hossein.dev.iso8583lab.data.StanRepository
import com.hossein.dev.iso8583lab.presentation.LabScreen
import com.hossein.dev.iso8583lab.presentation.LabViewModel
import com.hossein.dev.iso8583lab.presentation.LabViewModelFactory
import com.hossein.dev.iso8583lab.ui.theme.ISO8583LabTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            ISO8583LabTheme {
                val context = LocalContext.current
                val viewModel: LabViewModel = viewModel(
                    factory = LabViewModelFactory(
                        StanRepository(StanDataStore(context))
                    )
                )

                LabScreen(viewModel = viewModel)
            }
        }
    }
}