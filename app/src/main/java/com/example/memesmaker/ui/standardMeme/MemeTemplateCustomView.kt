package com.example.memesmaker.ui.standardMeme

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.AbstractComposeView
import com.example.memesmaker.data.Meme

class MemeTemplateCustomView(
    context: Context,
    private val meme:MutableState<Meme>,
    private val onImageClicked:()->Unit,
    private val onTextClicked:()->Unit
):AbstractComposeView(context){

    @Composable
    override fun Content() {

        MemeTemplate(
            meme = meme.value,
            onTextClicked = onTextClicked,
            onImageClicked = onImageClicked
        )

    }



}