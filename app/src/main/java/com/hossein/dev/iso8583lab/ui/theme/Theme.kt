package com.hossein.dev.iso8583lab.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Primary,
    onPrimary = Color(0xFFFFFFFF),
    secondary = Secondary,
    tertiary = Tertiary,
    background = BackgroundDark,
    onBackground = BackgroundLight,
    secondaryContainer = Color(0xFF343434)
)

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = Color(0xFFFFFFFF),
    secondary = Secondary,
    tertiary = Tertiary,
    background = BackgroundLight,
    onBackground = BackgroundDark,
    secondaryContainer = Color(0xFFE7E7E7)
)

@Composable
fun ISO8583LabTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}