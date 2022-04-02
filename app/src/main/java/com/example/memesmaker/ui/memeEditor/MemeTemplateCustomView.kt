package com.example.memesmaker.ui.memeEditor

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.memesmaker.R
import com.example.memesmaker.data.Meme
import com.example.memesmaker.ui.theme.DarkMemeBackground
import com.example.memesmaker.ui.theme.LightMemeBackground

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