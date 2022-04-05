package com.example.memesmaker.ui.customMeme

import android.graphics.Bitmap
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Delete
import com.example.memesmaker.R
import com.example.memesmaker.data.CustomMemeItems
import com.example.memesmaker.data.Tools
import com.example.memesmaker.data.getAllTools
import com.example.memesmaker.data.getCustomMemeTools
import com.example.memesmaker.ui.memeEditor.ToolsButton

@Composable
fun CustomMemeTopBar(
    onBack:()->Unit,
    onSave:()->Unit,
    onDelete: ()->Unit,
    selectedItem: CustomMemeItems?=null,
){
    Column {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.app_name),
                    fontWeight = FontWeight.Bold
                )
            },
            actions = {

                AnimatedVisibility(visible = selectedItem != null) {
                    IconButton(onClick = onDelete) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                    }
                }

                IconButton(onClick = onSave) {
                    Icon(
                        painter=painterResource(id = R.drawable.ic_save),
                        tint = MaterialTheme.colors.primary,
                        contentDescription = null
                    )
                }

            },
            backgroundColor = MaterialTheme.colors.surface,
            contentColor = MaterialTheme.colors.onSurface,
            elevation = 0.dp,
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, null)
                }
            }
        )
        Divider()
    }
}


@Composable
fun TransformableText(
    item:CustomMemeItems,
    isSelected:Boolean,
    onClick:()->Unit,
) {
    // set up all transformation states
    var scale by remember { mutableStateOf(1f) }
    var rotation by remember { mutableStateOf(0f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
        scale *= zoomChange
        rotation += rotationChange
        offset += offsetChange
    }



        val modifier=if (isSelected)
                Modifier
                    //Drag and drop
                    /**
                    .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                    change.consumeAllChanges()
                    offset = Offset(offset.x + dragAmount.x, offset.y + dragAmount.y)
                    }
                    }*/
                    // apply other transformations like rotation and zoom
                    // on the pizza slice emoji
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        rotationZ = rotation,
                        translationX = offset.x,
                        translationY = offset.y
                    )
                    // add transformable to listen to multitouch transformation events
                    // after offset
                    .transformable(state = state)
                    .border(4.dp, MaterialTheme.colors.primary)
        else
            Modifier
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    rotationZ = rotation,
                    translationX = offset.x,
                    translationY = offset.y
                )

        Text(
            modifier= modifier
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable { onClick() }
                .padding(12.dp),
            text=item.text?:"",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color=item.textColor?:Color.White
        )


}

@Composable
fun TransformableImage(
    item:CustomMemeItems,
    isSelected:Boolean,
    onClick:()->Unit,
) {
    // set up all transformation states
    var scale by remember { mutableStateOf(1f) }
    var rotation by remember { mutableStateOf(0f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
        scale *= zoomChange
        rotation += rotationChange
        offset += offsetChange
    }


        val modifier=if(isSelected)

                Modifier
                    // apply other transformations like rotation and zoom
                    // on the pizza slice emoji
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        rotationZ = rotation,
                        translationX = offset.x,
                        translationY = offset.y
                    )
                    // add transformable to listen to multitouch transformation events
                    // after offset
                    .transformable(state = state)
                    .border(4.dp, MaterialTheme.colors.primary)

        else
            Modifier
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    rotationZ = rotation,
                    translationX = offset.x,
                    translationY = offset.y
                )

        Image(
            modifier= modifier.clickable { onClick() },
            bitmap = item.bitmap?.asImageBitmap()!!,
            contentDescription = null
        )

}

@Composable
fun CustomMemeTemplate(
    background: Bitmap?=null,
    items:List<CustomMemeItems>,
    selectedItem:CustomMemeItems?=null,
    onBackgroundClicked:()->Unit,
    onItemSelected:(CustomMemeItems)->Unit
) {
    BoxWithConstraints {

        if (background==null)
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = null,
                modifier= Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clickable { onBackgroundClicked() },
                contentScale = ContentScale.Crop
            )
        else
            Image(
                bitmap = background.asImageBitmap(),
                contentDescription = null,
                modifier= Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                contentScale = ContentScale.Crop
            )

        //items
        items.forEach {

            val isSelected=it.timestamp==selectedItem?.timestamp

            if (it.isText())
                TransformableText(it,isSelected){onItemSelected(it)}
            else
                TransformableImage(it,isSelected){onItemSelected(it)}
        }


    }
}


@Composable
fun CustomMemeTools(
    currentTool: Tools,
    onToolClicked:(Tools)->Unit,
) {
    val scrollState = rememberScrollState()
    Row(
        modifier= Modifier
            .horizontalScroll(scrollState)
            .fillMaxWidth(),
    ) {
        getCustomMemeTools().forEach {
            val isSelected=currentTool==it
            ToolsButton(tool = it,isSelected = isSelected) {
                onToolClicked(it)
            }
        }
    }
}