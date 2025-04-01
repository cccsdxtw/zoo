import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6200EE), // 紫色 (Light Mode)
    onPrimary = Color.White, // 白色文字
    secondary = Color(0xFF03DAC5), // 綠色 (Light Mode)
    onSecondary = Color.Black, // 黑色文字
    background = Color.White, // 淺紫色背景 (Light Mode)
    onBackground = Color.Black, // 黑色文字
    surface = Color.White, // 白色背景 (Light Mode)
    onSurface = Color.Black // 黑色文字
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF3700B3), // 紫色 (Dark Mode)
    onPrimary = Color.White, // 白色文字
    secondary = Color(0xFF03DAC6), // 綠色 (Dark Mode)
    onSecondary = Color.Black, // 黑色文字
    background = Color(0xFF121212), // 深黑背景 (Dark Mode)
    onBackground = Color.White, // 白色文字
    surface = Color(0xFF121212), // 深黑背景 (Dark Mode)
    onSurface = Color.White // 白色文字
)


@Composable
fun ZooTheme(content: @Composable () -> Unit) {
    val darkTheme = isSystemInDarkTheme()

    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme, // 根據系統主題切換
        typography = typography,
        content = content
    )
}
