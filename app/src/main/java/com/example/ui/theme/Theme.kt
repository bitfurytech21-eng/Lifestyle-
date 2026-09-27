package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = BrickRed,
    onPrimary = White,
    primaryContainer = PaperCardBg,
    onPrimaryContainer = NearBlackInk,
    secondary = DenimBlue,
    onSecondary = White,
    secondaryContainer = Color(0xFFDCE4F0),
    onSecondaryContainer = DenimBlueDark,
    tertiary = MustardGold,
    onTertiary = NearBlackInk,
    background = WarmPaperCream,
    onBackground = NearBlackInk,
    surface = WarmPaperCream,
    onSurface = NearBlackInk,
    surfaceVariant = PaperCardBg,
    onSurfaceVariant = MutedInk,
    outline = NearBlackInk,
    outlineVariant = HairlineSubtle
)

private val DarkColorScheme = darkColorScheme(
    primary = BrickRedLight,
    onPrimary = NearBlackInk,
    primaryContainer = Color(0xFF3B2827),
    onPrimaryContainer = WarmPaperCream,
    secondary = DenimBlueLight,
    onSecondary = NearBlackInk,
    secondaryContainer = Color(0xFF1E2D42),
    onSecondaryContainer = WarmPaperCream,
    tertiary = MustardLight,
    onTertiary = NearBlackInk,
    background = Color(0xFF1E1C1A),
    onBackground = Color(0xFFEDE6D8),
    surface = Color(0xFF242220),
    onSurface = Color(0xFFEDE6D8),
    surfaceVariant = Color(0xFF2E2B27),
    onSurfaceVariant = Color(0xFFBFB7AA),
    outline = Color(0xFFEDE6D8),
    outlineVariant = Color(0xFF47433E)
)

@Composable
fun DailyAmericanTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // We intentionally maintain the Americana paper palette for consistent editorial aesthetic
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
