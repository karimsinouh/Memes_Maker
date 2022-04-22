package com.karimsinouh.memesmaker.ui.customMeme

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.platform.AbstractComposeView
import com.karimsinouh.memesmaker.data.CustomMemeItems

class CustomMemeView(
    context:Context,
    private val background: MutableState<Bitmap?>,
    private val items: SnapshotStateList<CustomMemeItems>,
    private val selectedItem: MutableState<CustomMemeItems?>,
    private val onBackgroundClicked:()->Unit,
    private val onItemSelected:(CustomMemeItems)->Unit
):AbstractComposeView(context) {

    @Composable
    override fun Content() {
        CustomMemeTemplate(
            background = background.value,
            items=items,
            selectedItem=selectedItem.value,
            onBackgroundClicked = onBackgroundClicked,
            onItemSelected=onItemSelected
        )
    }
}