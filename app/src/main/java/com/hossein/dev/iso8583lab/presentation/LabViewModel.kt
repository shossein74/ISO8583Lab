package com.hossein.dev.iso8583lab.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hossein.dev.iso8583lab.data.StanRepository
import com.hossein.dev.iso8583lab.util.CalendarUtil
import com.hossein.dev.iso8583lab.util.Constants.MIN_ISO_8583_MESSAGE_LENGTH
import com.hossein.dev.iso8583lab.util.Constants.PAN_LENGTH
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jpos.iso.ISOMsg
import org.jpos.iso.packager.ISO87APackager
import java.time.LocalDateTime
import java.util.Locale


class LabViewModel(
    private val stanRepository: StanRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LabUiState())
    val uiState: StateFlow<LabUiState> = _uiState

    private val _uiEffect = MutableSharedFlow<LabUiEffect>()
    val uiEffect: SharedFlow<LabUiEffect> = _uiEffect

    private var stanCounter: Int = 1

    init {
        viewModelScope.launch {
            stanRepository.stanFlow.collect {
                stanCounter = it
            }
        }
    }

    fun onEvent(event: LabUiEvent) {
        when (event) {
            is LabUiEvent.ChangeTabIndex -> {
                _uiState.value = _uiState.value.copy(
                    selectedTabIndex = event.index
                )
            }

            is LabUiEvent.ChangeCardNumber -> {
                _uiState.value = _uiState.value.copy(
                    messageBuilderUiState = _uiState.value.messageBuilderUiState.copy(
                        cardNumber = event.cardNumber
                    )
                )
            }

            is LabUiEvent.ChangeAmount -> {
                _uiState.value = _uiState.value.copy(
                    messageBuilderUiState = _uiState.value.messageBuilderUiState.copy(
                        amount = event.amount
                    )
                )
            }

            is LabUiEvent.ChangeIsoMessage -> {
                _uiState.value = _uiState.value.copy(
                    messageParserUiState = _uiState.value.messageParserUiState.copy(
                        isoMessage = event.message
                    )
                )
            }

            is LabUiEvent.ClearBuildData -> {
                _uiState.update {
                    it.copy(
                        messageBuilderUiState = it.messageBuilderUiState.copy(
                            field2 = "",
                            field4 = "",
                            field7 = "",
                            field11 = "",
                            builtMessage = "",
                            cardNumber = "",
                            amount = ""
                        )
                    )
                }
            }

            is LabUiEvent.ClearParseData -> {
                _uiState.update {
                    it.copy(
                        messageParserUiState = it.messageParserUiState.copy(
                            field2 = "",
                            field4 = "",
                            field7 = "",
                            field11 = "",
                            isoMessage = ""
                        )
                    )
                }
            }

            is LabUiEvent.BuildIsoMessage -> {
                if (validateBuildInputs()) {
                    buildIsoMessage()
                }
            }

            is LabUiEvent.ParseIsoMessage -> {
                if (validateParseInputs()) {
                    parseIsoMessage()
                }
            }
        }
    }

    private fun buildIsoMessage() {
        val state = _uiState.value.messageBuilderUiState
        if (state.cardNumber.isBlank() || state.amount.isBlank()) return

        try {
            val field2 = state.cardNumber
            val field4 = state.amount.padStart(12, '0')
            val field7 = CalendarUtil.getJalaliDateTimeForIso(LocalDateTime.now())
            val field11 = "%06d".format(Locale.getDefault(), stanCounter)

            incrementStan()

            val isoMessage = ISOMsg().apply {
                packager = ISO87APackager()
                mti = "0200"
                set(2, field2)
                set(4, field4)
                set(7, field7)
                set(11, field11)
            }

            val bytes = isoMessage.pack()
            val builtMessage = bytesToPrintableString(bytes)

            _uiState.update {
                it.copy(
                    messageBuilderUiState = it.messageBuilderUiState.copy(
                        field2 = field2,
                        field4 = field4,
                        field7 = field7,
                        field11 = field11,
                        builtMessage = builtMessage
                    )
                )
            }

        } catch (_: Exception) {
            launchEffect(LabUiEffect.ShowSnackBar("Failed to build ISO 8583 message. Please check your input and try again."))
        }
    }

    private fun incrementStan() {
        viewModelScope.launch {
            val next = (stanCounter + 1).coerceAtMost(999_999)
            stanCounter = if (next > 999_999) 1 else next
            stanRepository.saveStan(stanCounter)
        }
    }

    private fun parseIsoMessage() {
        val message = _uiState.value.messageParserUiState.isoMessage

        if (message.isBlank()) {
            return
        }

        val bytes = message.toByteArray(Charsets.ISO_8859_1)

        val isoMessage = try {
            ISOMsg().apply {
                packager = ISO87APackager()
                unpack(bytes)
            }
        } catch (e: Exception) {
            launchEffect(LabUiEffect.ShowSnackBar("Failed to parse ISO 8583 message. Please make sure the message format is valid."))
            return
        }

        val field2 = isoMessage.getString(2)
        val field4 = isoMessage.getString(4)
        val field7 = isoMessage.getString(7)
        val field11 = isoMessage.getString(11)

        _uiState.update {
            it.copy(
                messageParserUiState = it.messageParserUiState.copy(
                    field2 = field2,
                    field4 = field4,
                    field7 = field7,
                    field11 = field11
                )
            )
        }
    }

    private fun validateBuildInputs(): Boolean {
        val state = _uiState.value.messageBuilderUiState

        val (hasCardNumberError, cardNumberErrorText) = when {
            (state.cardNumber.isBlank()) -> Pair(true, "Please enter your card number.")
            (state.cardNumber.length != PAN_LENGTH) -> Pair(true, "Enter the full card number.")
            else -> Pair(false, "")
        }
        val hasAmountError = state.amount.isBlank()

        _uiState.update {
            it.copy(
                messageBuilderUiState = state.copy(
                    cardNumberValidationError = cardNumberErrorText,
                    hasCardNumberError = hasCardNumberError,

                    amountValidationError = if (hasAmountError) "Enter your amount." else "",
                    hasAmountError = hasAmountError
                )
            )
        }

        return !hasCardNumberError && !hasAmountError
    }

    private fun validateParseInputs(): Boolean {
        val state = _uiState.value.messageParserUiState

        val (hasIsoMessageError, textError) = when {
            state.isoMessage.isBlank() -> Pair(true, "Please enter an ISO 8583 message.")
            state.isoMessage.length < MIN_ISO_8583_MESSAGE_LENGTH -> Pair(
                true,
                "ISO 8583 message is too short."
            )

            else -> Pair(false, "")
        }

        _uiState.update {
            it.copy(
                messageParserUiState = state.copy(
                    isoMessageValidationError = textError,
                    hasIsoMessageError = hasIsoMessageError
                )
            )
        }

        return !hasIsoMessageError
    }

    private fun launchEffect(effect: LabUiEffect) = viewModelScope.launch {
        _uiEffect.emit(effect)
    }

    private fun bytesToPrintableString(bytes: ByteArray): String =
        bytes.joinToString(separator = "") { b ->
            val ub = b.toInt() and 0xFF
            if (ub in 32..126) ub.toChar().toString()
            else "\\x%02X".format(ub)
        }
}

class LabViewModelFactory(
    private val repository: StanRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LabViewModel::class.java)) {
            return LabViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
