package com.hossein.dev.iso8583lab.presentation

sealed class LabUiEvent {

    data class ChangeTabIndex(val index: Int): LabUiEvent()
}