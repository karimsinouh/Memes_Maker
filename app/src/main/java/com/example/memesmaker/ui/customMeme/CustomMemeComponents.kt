package com.example.memesmaker.ui.customMeme

import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.memesmaker.R

@Composable
fun CustomMemeTopBar(
    onBack:()->Unit,
    onSave:()->Unit
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