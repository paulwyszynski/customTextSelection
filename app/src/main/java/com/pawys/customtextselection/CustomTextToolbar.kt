package com.pawys.customtextselection

import android.app.SearchManager
import android.content.Intent
import android.speech.tts.TextToSpeech
import android.view.View
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.platform.TextToolbar
import androidx.compose.ui.platform.TextToolbarStatus
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import java.util.Locale

class CustomTextToolbar(private val view: View, private val textFieldValue: TextFieldValue) : TextToolbar,
    TextToSpeech.OnInitListener {

    override var status: TextToolbarStatus by mutableStateOf(TextToolbarStatus.Hidden)
    private val yOffset = 50.dp.value.toInt()
    private var popupPosition by mutableStateOf(IntOffset.Zero)
    private var textToSpeech: TextToSpeech? = null
    private var onCopyRequested: (() -> Unit)? = null
    private var onSearchRequested: (() -> Unit)? = null

    init {
        textToSpeech = TextToSpeech(view.context, this)
    }

    override fun hide() {
        status = TextToolbarStatus.Hidden
    }

    override fun showMenu(
        rect: Rect,
        onCopyRequested: (() -> Unit)?,
        onPasteRequested: (() -> Unit)?,
        onCutRequested: (() -> Unit)?,
        onSelectAllRequested: (() -> Unit)?
    ) {
        this.onCopyRequested = onCopyRequested
        this.onSearchRequested = {
            val selectedText = textFieldValue.text.substring(textFieldValue.selection.start, textFieldValue.selection.end)
            val searchIntent = Intent(Intent.ACTION_WEB_SEARCH).apply {
                putExtra(SearchManager.QUERY, selectedText)
            }
            view.context.startActivity(searchIntent)
        }
        popupPosition = IntOffset(rect.right.toInt(), rect.bottom.toInt() - yOffset)
        status = TextToolbarStatus.Shown
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            textToSpeech?.language = Locale.US
        }
    }

    private fun speakText(text: String) {
        textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    @Composable
    fun ToolbarPopup() {
        if (status == TextToolbarStatus.Shown) {
            Popup(offset = popupPosition) {
                Column {
                    Button(onClick = {
                        onCopyRequested?.invoke()
                        hide()
                    }) {
                        Text("Copy")
                    }
                    Button(onClick = {
                        onSearchRequested?.invoke()
                        hide()
                    }) {
                        Text("Search")
                    }
                    Button(onClick = {
                        val selectedText = textFieldValue.text.substring(textFieldValue.selection.start, textFieldValue.selection.end)
                        speakText(selectedText)
                        hide()
                    }) {
                        Text("Speak")
                    }
                }
            }
        }
    }
}

