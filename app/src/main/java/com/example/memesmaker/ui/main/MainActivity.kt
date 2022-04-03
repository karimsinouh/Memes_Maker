package com.example.memesmaker.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.memesmaker.ui.memeEditor.MemeEditor
import com.example.memesmaker.ui.theme.MemesMakerTheme
import com.example.memesmaker.util.customComponents.MessageScreen
import com.example.memesmaker.util.customComponents.RoundedButton
import com.example.memesmaker.R

class MainActivity : ComponentActivity() {

    private val vm by viewModels<MainViewModel>()

    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MemesMakerTheme {

                window.statusBarColor=MaterialTheme.colors.surface.toArgb()

                Scaffold(
                    content={Content()},
                    topBar = { MainTopBar() },
                    backgroundColor = MaterialTheme.colors.background,
                )
            }
        }
    }

    @Composable
    private fun Content() {

        val memes=vm.memesList.observeAsState().value ?: emptyList()

        if (memes.isEmpty()){
            MessageScreen(
                title= stringResource(R.string.bit_empty),
                text = stringResource(R.string.no_memes_yet)
            ) {
                RoundedButton(text = stringResource(R.string.create)) {
                    openEditorActivity()
                }
            }
            return
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
                        text = getString(R.string.app_name),
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                          IconButton(onClick = ::openEditorActivity) {
                              Icon(Icons.Default.Add, null)
                          }
                },
                backgroundColor = MaterialTheme.colors.surface,
                contentColor = MaterialTheme.colors.onSurface,
                elevation = 0.dp,
                navigationIcon = {
                    IconButton(onClick = ::openEditorActivity) {
                        Icon(Icons.Default.Menu, null)
                    }
                }
            )
            Divider()
        }
    }
}
