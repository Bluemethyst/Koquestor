package dev.bluemethyst.apps.koquestor

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import dev.bluemethyst.apps.koquestor.gui.Sidebar
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

val DarkColorPalette = darkColorScheme(
    background = Color(0xFF121212),
    primary = Color(0xFFBB86FC),
    secondary = Color(0xFF03DAC6)
)

val LightColorPalette = lightColorScheme(
    background = Color(0xFFFFFFFF),
    primary = Color(0xFF1FAA59),
    secondary = Color(0xFFB2FF59)
)

@Composable
@Preview
fun App() {
    val colorScheme = if (isSystemInDarkTheme()) DarkColorPalette else LightColorPalette
    MaterialTheme(colorScheme = colorScheme) {
        Box(modifier = Modifier.fillMaxSize().background(colorScheme.background)) {
        var url by remember { mutableStateOf(TextFieldValue()) }
        var response by remember { mutableStateOf("") }
        var responseCode by remember { mutableStateOf(0) }
        var sendRequest by remember { mutableStateOf(false) }
        Row {
            Sidebar()
            Column {
                TextField(value = url, onValueChange = { url = it })
                Button(onClick = { sendRequest = true }) {
                    Text("Send Request")
                }
                Column(modifier = Modifier.verticalScroll(
                    state = rememberScrollState()
                )) {
                    Text("Response Code: $responseCode")
                    Text("Response: $response")
                    if (sendRequest) {
                        LaunchedEffect(key1 = sendRequest) {
                            val client = HttpClient()
                            var httpResponse: HttpResponse? = null
                            try {
                                httpResponse = client.get(url.text)
                                response = httpResponse.bodyAsText()
                                responseCode = httpResponse.status.value
                                sendRequest = false
                            } catch (e: Exception) {
                                if (e.message == "Connection refused: getsockopt") {
                                    response = "Connection refused, Maybe incorrect URL?"
                                } else {
                                    response = e.message ?: "Unknown error"
                                    println(e)
                                }
                                if (httpResponse != null) {
                                    responseCode = httpResponse.status.value
                                }
                                sendRequest = false
                            }
                        }
                    }
                }
            }
        }
    }
}}


fun main() = application {
    val state = rememberWindowState(  // https://stackoverflow.com/questions/78532146/how-can-i-change-the-default-window-dimensions-for-kotlin-compose-desktop
        width = 1280.dp,
        height = 720.dp,
    )
    Window(
        onCloseRequest = ::exitApplication,
        title = "Koquestor",
        state = state,
        icon = BitmapPainter(useResource("assets/koquestor.ico", ::loadImageBitmap)),
    ) {
        App()
    }
}
