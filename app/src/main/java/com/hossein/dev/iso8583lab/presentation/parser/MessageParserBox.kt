package com.hossein.dev.iso8583lab.presentation.parser

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hossein.dev.iso8583lab.presentation.FieldShowCase
import com.hossein.dev.iso8583lab.presentation.LabUiEvent
import com.hossein.dev.iso8583lab.presentation.common.ClearButton
import com.hossein.dev.iso8583lab.presentation.common.LabAnimatedVisibility
import com.hossein.dev.iso8583lab.presentation.common.PrimaryButton
import com.hossein.dev.iso8583lab.presentation.common.PrimaryTextField
import com.hossein.dev.iso8583lab.ui.theme.ISO8583LabTheme

@Composable
fun MessageParserBox(
    uiState: MessageParserUiState,
    onEvent: (LabUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(top = 12.dp)
            .imePadding(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        PrimaryTextField(
            label = "ISO 8583 Message",
            value = uiState.isoMessage,
            onValueChange = {
                onEvent(LabUiEvent.ChangeIsoMessage(it))
            },
            isError = uiState.hasIsoMessageError,
            errorText = uiState.isoMessageValidationError,
            singleLine = false,
            maxLines = 4,
            minLines = 2,
            modifier = Modifier.height(96.dp)
        )

        PrimaryButton(
            text = "PARSE MESSAGE",
            onClick = { onEvent(LabUiEvent.ParseIsoMessage) }
        )

        Spacer(Modifier.height(2.dp))

        LabAnimatedVisibility(
            visible = uiState.isParsedState,
        ) {
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ParsedFieldsSection(uiState)

                ClearButton(onClick = { onEvent(LabUiEvent.ClearParseData) })
            }
        }
    }
}

@Composable
private fun ParsedFieldsSection(uiState: MessageParserUiState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = MaterialTheme.shapes.small
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        FieldShowCase(title = "Field 2 (PAN)", value = uiState.field2)
        FieldShowCase(title = "Field 4 (Amount)", value = uiState.field4)
        FieldShowCase(title = "Field 7 (Timestamp)", value = uiState.field7)
        FieldShowCase(title = "Field 11 (STAN)", value = uiState.field11)
    }
}

@Preview(showBackground = true)
@Composable
private fun MessageParserBoxPreview() {
    ISO8583LabTheme {
        MessageParserBox(
            uiState = MessageParserUiState(),
            onEvent = {}
        )
    }
}