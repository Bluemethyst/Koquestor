package dev.bluemethyst.apps.koquestor.gui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun Sidebar() {
    Column {
        Text("Koquestor")
        Button(onClick = { println("Button Pressed") }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1FAA59))) {
            Text("Send Request")
        }
    }
}