package com.example.memesmaker.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.memesmaker.ui.memeEditor.MemeEditor
import com.example.memesmaker.ui.theme.MemesMakerTheme
import com.example.memesmaker.util.customComponents.MessageScreen
import com.example.memesmaker.util.customComponents.RoundedButton

class MainActivity : ComponentActivity() {


    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MemesMakerTheme {

                window.statusBarColor=MaterialTheme.colors.surface.toArgb()

                Scaffold(
                    content={Content()},
                    topBar = {
                        MainTopBar()
                    },
                    backgroundColor = MaterialTheme.colors.background
                )
            }
        }
    }

    @Composable
    private fun Content() {
        MessageScreen(
            title= "It's a bit empty here",
            text = "You haven't created any memes yet. All memes will be shown here"
        ) {
            RoundedButton(text = "Create") {
                openEditorActivity()
            }
        }
    }

    private fun openEditorActivity(){
        val i=Intent(this,MemeEditor::class.java)
        startActivity(i)
    }

    @Composable
    @Preview
    private fun MainTopBar() {
        Column {
            TopAppBar(
                title = {
                    Text(
                        text = "Memes Maker",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                    )
                },
                actions = {},
                backgroundColor = MaterialTheme.colors.surface,
                contentColor = MaterialTheme.colors.onSurface,
                elevation = 0.dp
            )
            Divider()
        }
    }
}
