package com.hossein.dev.iso8583lab.presentation

sealed class LabUiEvent {

    data class ChangeTabIndex(val index: Int): LabUiEvent()
    data class ChangeCardNumber(val cardNumber: String): LabUiEvent()
    data class ChangeAmount(val amount: String): LabUiEvent()
    data class ChangeIsoMessage(val message: String): LabUiEvent()
    data object ParseIsoMessage: LabUiEvent()
    data object BuildIsoMessage: LabUiEvent()
    data object ClearParseData: LabUiEvent()
    data object ClearBuildData: LabUiEvent()
}