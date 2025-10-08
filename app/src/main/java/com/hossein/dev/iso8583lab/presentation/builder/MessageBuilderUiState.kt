package com.hossein.dev.iso8583lab.presentation.builder

import androidx.compose.runtime.Immutable

@Immutable
data class MessageBuilderUiState(
    val cardNumber : String = "",
    val amount: String = "",
    val field2: String = "",
    val field4: String = "",
    val field7: String = "",
    val field11: String = "",
    val builtMessage: String = "",

    val cardNumberValidationError: String = "",
    val amountValidationError: String = "",
    val hasCardNumberError: Boolean = false,
    val hasAmountError: Boolean = false,
) {
    val isBuiltMessage: Boolean = builtMessage.isNotEmpty()
}
