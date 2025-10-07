package com.hossein.dev.iso8583lab.presentation.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun LabAnimatedVisibility(
    visible: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable() (AnimatedVisibilityScope.() -> Unit)
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically { -it / 4 } + fadeIn(),
        exit = slideOutVertically { -it / 4 } + fadeOut(),
        modifier = modifier,
        content = content
    )
}