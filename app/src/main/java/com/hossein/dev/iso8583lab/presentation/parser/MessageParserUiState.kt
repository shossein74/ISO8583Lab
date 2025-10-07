package com.hossein.dev.iso8583lab.presentation.parser

data class MessageParserUiState(
    val isoMessage: String = "",
    val field2: String = "",
    val field4: String = "",
    val field7: String = "",
    val field11: String = "",
) {
    val isParsedState: Boolean =
        field2.isNotEmpty() && field4.isNotEmpty() && field7.isNotEmpty() && field11.isNotEmpty()
}
