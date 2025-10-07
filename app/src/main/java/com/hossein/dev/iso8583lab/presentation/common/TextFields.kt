package com.hossein.dev.iso8583lab.presentation.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PrimaryTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    maxLength: Int? = null
) {
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
        maxLines = 1,
    )
}