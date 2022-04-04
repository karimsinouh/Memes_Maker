package com.example.memesmaker.ui.customMeme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import com.example.memesmaker.ui.theme.MemesMakerTheme

class CustomMemeActivity:ComponentActivity() {

    private val vm by viewModels<CustomMemeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            MemesMakerTheme {

                window.statusBarColor=MaterialTheme.colors.surface.toArgb()

                Scaffold(
                    topBar = {
                        CustomMemeTopBar(onBack = ::finish, onSave = {})
                             },
                    content = {Content()}
                )

            }

        }

    }

    @Composable
    private fun Content(){

    }

}