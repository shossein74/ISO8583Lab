package com.hossein.dev.iso8583lab.presentation

import androidx.compose.runtime.Immutable
import com.hossein.dev.iso8583lab.presentation.builder.MessageBuilderUiState
import com.hossein.dev.iso8583lab.presentation.parser.MessageParserUiState

@Immutable
data class LabUiState(
    val selectedTabIndex: Int = 0,
    val messageBuilderUiState: MessageBuilderUiState = MessageBuilderUiState(),
    val messageParserUiState: MessageParserUiState = MessageParserUiState()
)
