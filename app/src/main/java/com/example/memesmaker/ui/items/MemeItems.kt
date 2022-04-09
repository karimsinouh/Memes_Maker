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
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.memesmaker.R
import com.example.memesmaker.database.MemeEntity
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeableMemeItem(
    memeEntity: MemeEntity,
    onLongClick:()->Unit,
    onClick: () -> Unit,
    onDelete:()->Unit,
    onShare:()->Unit,
) {

    val delete= SwipeAction(
        onSwipe = onDelete,
        icon= {
              Icon(
                  imageVector = Icons.Outlined.Delete,
                  contentDescription = null,
                  tint = Color.White,
                  modifier = Modifier.padding(24.dp)
              )
        },
        background =Color(0xFFE73838),
    )

    val share= SwipeAction(
        onSwipe = onShare,
        icon= {
            Icon(
                imageVector = Icons.Outlined.Share,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.padding(24.dp)
            )
        },
        background =MaterialTheme.colors.primary,

    )

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

        SwipeableActionsBox(
            startActions = listOf(share),
            endActions = listOf(delete)
        ) {

            val uri= Uri.parse(memeEntity.memePath)
            AsyncImage(
                model = uri,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.placeholder),
                error = painterResource(id = R.drawable.placeholder),
            )
        }

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