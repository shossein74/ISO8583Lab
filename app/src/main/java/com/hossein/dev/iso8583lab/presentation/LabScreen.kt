package com.hossein.dev.iso8583lab.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hossein.dev.iso8583lab.data.StanDataStore
import com.hossein.dev.iso8583lab.data.StanRepository
import com.hossein.dev.iso8583lab.presentation.builder.MessageBuilderBox
import com.hossein.dev.iso8583lab.presentation.common.LabTabBar
import com.hossein.dev.iso8583lab.presentation.parser.MessageParserBox
import com.hossein.dev.iso8583lab.ui.theme.ISO8583LabTheme
import com.hossein.dev.iso8583lab.ui.theme.Primary
import com.hossein.dev.iso8583lab.util.safeNavigationBarsPadding

@Composable
fun LabScreen(modifier: Modifier = Modifier, viewModel: LabViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val tabs = remember { listOf("Message Builder", "Message Parser") }
    val topBarColor = remember { Primary.copy(alpha = 0.06f) }
    val hostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is LabUiEffect.ShowSnackBar -> hostState.showSnackbar(effect.message)
            }
        }
    }

    Scaffold(
        topBar = {
            Column {
                Spacer(
                    Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                        .background(color = topBarColor)
                )
                LabTabBar(
                    tabs = tabs,
                    selectedIndex = uiState.selectedTabIndex,
                    onTabSelected = { index ->
                        viewModel.onEvent(LabUiEvent.ChangeTabIndex(index))
                    },
                    containerColor = topBarColor,
                    modifier = Modifier
                )
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState, modifier = Modifier
                    .imePadding()
            )
        },
        modifier = modifier.safeNavigationBarsPadding()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            when (uiState.selectedTabIndex) {
                0 -> MessageBuilderBox(
                    uiState = uiState.messageBuilderUiState,
                    onEvent = viewModel::onEvent
                )

                1 -> MessageParserBox(
                    uiState = uiState.messageParserUiState,
                    onEvent = viewModel::onEvent
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun LabScreenPreview(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val viewModel = remember {
        val dataStore = StanDataStore(context)
        val repository = StanRepository(dataStore)
        LabViewModel(repository)
    }
    ISO8583LabTheme {
        LabScreen(modifier, viewModel = viewModel)
    }
}