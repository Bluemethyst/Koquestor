package dev.bluemethyst.apps.koquestor

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

val DarkColorPalette = darkColorScheme(
    background = Color(0xFF121212),
    primary = Color(0xFFBB86FC),
    secondary = Color(0xFF03DAC6)
)

val LightColorPalette = lightColorScheme(
    primary = Color(0xFF1FAA59),
    secondary = Color(0xFFB2FF59)
)

@Composable
@Preview
fun App() {
    MaterialTheme(colorScheme = if (isSystemInDarkTheme()) DarkColorPalette else LightColorPalette) {
        var url by remember { mutableStateOf(TextFieldValue()) }
        var response by remember { mutableStateOf("") }
        var responseCode by remember { mutableStateOf(0) }
        var sendRequest by remember { mutableStateOf(false) }

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

/*fun FrameWindowScope.setMinSize() {
    window.minimumSize = Dimension(1280, 720)  // doesnt work for changing dimensions
}*/                                            // https://stackoverflow.com/questions/78532146/how-can-i-change-the-default-window-dimensions-for-kotlin-compose-desktop

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Koquestor",
        icon = BitmapPainter(useResource("assets/koquestor.ico", ::loadImageBitmap)),

    ) {
        App()
    }
}