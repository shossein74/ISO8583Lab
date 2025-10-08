package com.hossein.dev.iso8583lab.presentation

sealed class LabUiEffect {
    data class ShowSnackBar(val message: String): LabUiEffect()
}