package com.hossein.dev.iso8583lab.presentation.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PrimaryTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    maxLength: Int? = null,
    isError: Boolean = false,
    errorText: String? = null,
    singleLine: Boolean = false,
    maxLines: Int = 1,
    minLines: Int = 1,
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = {
                if (maxLength == null || it.length <= maxLength) {
                    onValueChange(it)
                }
            },
            label = {
                Text(text = label, style = MaterialTheme.typography.bodySmall)
            },
            textStyle = MaterialTheme.typography.bodyMedium,
            shape = MaterialTheme.shapes.medium,
            modifier = modifier.fillMaxWidth(),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            maxLines = maxLines,
            minLines = 1,
            singleLine = singleLine,
            isError = isError,
            colors = OutlinedTextFieldDefaults.colors(
                errorBorderColor = Color.Red,
                errorLabelColor = Color.Red,
            )
        )
        if (isError && errorText != null) {
            Spacer(Modifier.height(8.dp))

            Text(
                text = errorText,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Red
            )
        }
    }
}