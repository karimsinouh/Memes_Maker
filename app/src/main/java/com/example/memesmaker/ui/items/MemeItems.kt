package com.example.memesmaker.ui.items

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.memesmaker.database.MemeEntity

@Composable
fun MemeItem(
    memeEntity: MemeEntity,
    onLongClick:()->Unit,
    onClick: () -> Unit,
) {
    val uri= Uri.parse(memeEntity.memePath)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit){
                detectTapGestures(
                    onLongPress = {
                        onLongClick()
                    },
                    onTap = {
                        onClick()
                    }
                )
            },
        shape = RoundedCornerShape(8.dp),
    ){
        AsyncImage(
            model = uri,
            contentDescription = null,
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            contentScale = ContentScale.Crop,
        )
    }
}