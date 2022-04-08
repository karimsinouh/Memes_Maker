package com.example.memesmaker.ui.items

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.memesmaker.database.MemeEntity


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeableMemeItem(
    memeEntity: MemeEntity,
    onLongClick:()->Unit,
    onClick: () -> Unit,
    onDelete:()->Unit,
    onShare:()->Unit,
) {
    val state = rememberDismissState(
        confirmStateChange = {
            when(it){
                DismissValue.Default -> Unit
                DismissValue.DismissedToEnd -> {
                    onShare()
                }
                DismissValue.DismissedToStart -> onDelete()
            }
            true
        }
    )

    SwipeToDismiss(
        state = state,
        background = {
            when(state.targetValue){
                DismissValue.Default -> Unit
                DismissValue.DismissedToEnd -> SwipeBackground(
                    icon = Icons.Default.Share,
                    color = MaterialTheme.colors.primary,
                    direction = state.targetValue,
                )
                DismissValue.DismissedToStart -> SwipeBackground(
                    icon = Icons.Default.Delete,
                    color = Color(0xFFE63D3D),
                    direction = state.targetValue,
                )
            }
        },
        directions = setOf(DismissDirection.EndToStart,DismissDirection.StartToEnd),
    ){
        MemeItem(
            memeEntity = memeEntity,
            onLongClick = onLongClick,
            onClick = onClick
        )
    }

}

@Composable
private fun SwipeBackground(
    icon:ImageVector,
    color:Color,
    direction:DismissValue,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(12.dp))
            .background(color)
    ) {
        val alignment=when(direction){
            DismissValue.Default -> Modifier
            DismissValue.DismissedToEnd -> Modifier.align(Alignment.CenterStart)
            DismissValue.DismissedToStart -> Modifier.align(Alignment.CenterEnd)
        }
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier=alignment.padding(24.dp),
            tint = contentColorFor(backgroundColor = color)
        )
    }
}

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
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        onLongClick()
                    },
                    onTap = {
                        onClick()
                    },
                )
            },
        shape = RoundedCornerShape(8.dp),
    ){
        AsyncImage(
            model = uri,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            contentScale = ContentScale.Crop,
        )
    }
}