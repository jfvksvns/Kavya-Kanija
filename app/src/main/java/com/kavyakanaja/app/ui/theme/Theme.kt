package com.kavyakanaja.app.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = SaffronOrange,
    secondary = DeepKumkum,
    background = CreamParchment,
    surface = CreamParchment,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    onBackground = WarmBrown,
    onSurface = WarmBrown,
    tertiary = GoldenAccent
)

private val DarkColorScheme = darkColorScheme(
    primary = LightSaffron,
    secondary = DeepKumkum,
    background = DarkBackground,
    surface = DarkSurface,
    onPrimary = DarkBackground,
    onBackground = LightCream,
    onSurface = LightCream,
    tertiary = GoldenAccent
)

@Composable
fun KavyaKanajaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = KavyaTypography,
        content = content
    )
}