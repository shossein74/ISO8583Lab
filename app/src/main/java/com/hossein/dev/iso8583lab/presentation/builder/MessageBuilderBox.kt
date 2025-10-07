package com.hossein.dev.iso8583lab.presentation.builder

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hossein.dev.iso8583lab.presentation.FieldShowCase
import com.hossein.dev.iso8583lab.presentation.LabUiEvent
import com.hossein.dev.iso8583lab.presentation.common.ClearButton
import com.hossein.dev.iso8583lab.presentation.common.LabAnimatedVisibility
import com.hossein.dev.iso8583lab.presentation.common.PrimaryButton
import com.hossein.dev.iso8583lab.presentation.common.PrimaryTextField
import com.hossein.dev.iso8583lab.ui.theme.ISO8583LabTheme
import com.hossein.dev.iso8583lab.util.Constants.AMOUNT_LENGTH
import com.hossein.dev.iso8583lab.util.Constants.PAN_LENGTH

@Composable
fun MessageBuilderBox(
    uiState: MessageBuilderUiState,
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
            value = uiState.cardNumber,
            onValueChange = { onEvent(LabUiEvent.ChangeCardNumber(it)) },
            label = "Card Number",
            maxLength = PAN_LENGTH,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )

        PrimaryTextField(
            value = uiState.amount,
            onValueChange = { onEvent(LabUiEvent.ChangeAmount(it)) },
            label = "Amount",
            maxLength = AMOUNT_LENGTH,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )

        Spacer(Modifier.height(2.dp))

        PrimaryButton(
            text = "BUILD ISO MESSAGE",
            onClick = { onEvent(LabUiEvent.BuildIsoMessage) },
        )

        Spacer(Modifier.height(2.dp))

        LabAnimatedVisibility(
            visible = uiState.isBuiltMessage,
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ParsedFieldsSection(uiState)

                ClearButton(onClick = { onEvent(LabUiEvent.ClearBuildData) })
            }
        }
    }
}

@Composable
private fun ParsedFieldsSection(uiState: MessageBuilderUiState, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = MaterialTheme.shapes.small
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        FieldShowCase(
            title = "Field 2 (PAN)",
            value = uiState.field2
        )
        FieldShowCase(
            title = "Field 4 (Amount)",
            value = uiState.field4
        )
        FieldShowCase(
            title = "Field 7 (Timestamp)",
            value = uiState.field7
        )
        FieldShowCase(
            title = "Field 11 (Stan)",
            value = uiState.field11
        )
        FieldShowCase(
            title = "Full Message",
            value = uiState.builtMessage
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MessageBuilderFormPreview() {
    ISO8583LabTheme {
        MessageBuilderBox(
            uiState = MessageBuilderUiState(),
            onEvent = {}
        )
    }
}