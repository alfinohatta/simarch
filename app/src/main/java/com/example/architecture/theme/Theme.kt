package com.example.architecture.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = ActiveComponent,
    onPrimary = LightSurfaceColor,
    primaryContainer = Color(0xFFD1FAE5),
    onPrimaryContainer = Color(0xFF065F46),
    secondary = DataFlowColor,
    onSecondary = LightSurfaceColor,
    secondaryContainer = Color(0xFFCCFBF1),
    onSecondaryContainer = Color(0xFF115E59),
    tertiary = ControlFlowColor,
    onTertiary = LightSurfaceColor,
    tertiaryContainer = Color(0xFFDBEAFE),
    onTertiaryContainer = Color(0xFF1E40AF),
    background = LightAppBackground,
    onBackground = LightTextPrimary,
    surface = LightSurfaceColor,
    onSurface = LightTextPrimary,
    surfaceVariant = LightSurfaceTonal,
    onSurfaceVariant = LightTextSecondary,
    outline = LightBorderColor,
    outlineVariant = Color(0xFFCBD5E1)
)

private val DarkColorScheme = darkColorScheme(
    primary = ActiveComponent,
    onPrimary = DarkAppBackground,
    primaryContainer = Color(0xFF064E3B),
    onPrimaryContainer = Color(0xFFA7F3D0),
    secondary = DataFlowColor,
    onSecondary = DarkAppBackground,
    secondaryContainer = Color(0xFF134E4A),
    onSecondaryContainer = Color(0xFF99F6E4),
    tertiary = ControlFlowColor,
    onTertiary = DarkAppBackground,
    tertiaryContainer = Color(0xFF1E3A8A),
    onTertiaryContainer = Color(0xFFBFDBFE),
    background = DarkAppBackground,
    onBackground = DarkTextPrimary,
    surface = DarkSurfaceColor,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSurfaceTonal,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkBorderColor,
    outlineVariant = Color(0xFF475569)
)

@Composable
fun ArchitectureTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
