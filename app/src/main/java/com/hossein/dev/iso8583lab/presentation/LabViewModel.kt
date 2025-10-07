package com.hossein.dev.iso8583lab.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hossein.dev.iso8583lab.data.StanRepository
import com.hossein.dev.iso8583lab.util.CalendarUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jpos.iso.ISOMsg
import org.jpos.iso.packager.ISO87APackager
import java.time.LocalDateTime
import java.util.Locale


class LabViewModel(
    private val stanRepository: StanRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(LabUiState())
    val uiState: StateFlow<LabUiState> = _uiState

    var stanCounter = 1

    init {
        viewModelScope.launch {
            stanRepository.stanFlow.collect {
                stanCounter = it
            }
        }
    }

    fun onEvent(event: LabUiEvent) {
        when(event) {
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
            is LabUiEvent.BuildIsoMessage -> buildIsoMessage()
            is LabUiEvent.ParseIsoMessage -> parseIsoMessage()
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

            nextStan()

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

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun nextStan() {
        viewModelScope.launch {
            stanRepository.saveStan(stanCounter + 1)
            stanCounter++
        }
    }

    private fun parseIsoMessage() {
        val message = _uiState.value.messageParserUiState.isoMessage

        if (message.isEmpty()) {
            return
        }

        val bytes = message.toByteArray(Charsets.ISO_8859_1)

        val isoMessage = try {
            ISOMsg().apply {
                packager = ISO87APackager()
                unpack(bytes)
            }
        } catch (e: Exception) {
            e.printStackTrace()
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

    private fun bytesToPrintableString(bytes: ByteArray): String =
        buildString {
            for (b in bytes) {
                val ub = b.toInt() and 0xFF
                if (ub in 32..126) append(ub.toChar())
                else append("\\x%02X".format(ub))
            }
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
