package com.hossein.dev.iso8583lab.util

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Modifier.safeSystemBarsPadding(): Modifier =
    this.padding(WindowInsets.navigationBars.asPaddingValues())
        .padding(WindowInsets.statusBars.asPaddingValues())

@Composable
fun Modifier.safeNavigationBarsPadding(): Modifier =
    this.padding(WindowInsets.navigationBars.asPaddingValues())