package com.pawys.customtextselection

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalTextToolbar
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pawys.customtextselection.ui.theme.CustomTextSelectionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CustomTextSelectionTheme {
                Scaffold(modifier = Modifier.fillMaxSize().padding(top = 32.dp)) { innerPadding ->
                    ShowText(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun ShowText(modifier: Modifier = Modifier) {
    val text = """
        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. 
        Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. 
        Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. 
        Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
    """.trimIndent()
    var textInput by remember { mutableStateOf(TextFieldValue(text)) }
    val view = LocalView.current
    val customTextToolbar = CustomTextToolbar(view, textInput)
    CompositionLocalProvider(LocalTextToolbar provides customTextToolbar) {
        Box(modifier = Modifier.fillMaxHeight()) {
            TextField(
                modifier = modifier,
                value = textInput,
                onValueChange = { newValue ->
                    textInput = newValue
                },
                readOnly = true,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    selectionColors = TextSelectionColors(Color.Red, Color.Yellow),
                ),
            )
            customTextToolbar.ToolbarPopup()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CustomTextSelectionTheme {
        ShowText()
    }
}