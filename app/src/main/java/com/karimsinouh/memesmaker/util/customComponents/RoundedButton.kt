package com.karimsinouh.memesmaker.util.customComponents


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


@Composable
fun RoundedButton(
    text:String,
    modifier: Modifier=Modifier,
    backgroundColor:Color=MaterialTheme.colors.primary,
    contentColor:Color=contentColorFor(backgroundColor = backgroundColor),
    fullWidth:Boolean=false,
    outerPadding:PaddingValues= PaddingValues(0.dp),
    icon:ImageVector?=null,
    onClick:()->Unit
) {

    val modifier2=modifier
        .padding(outerPadding)
        .clip(CircleShape)
        .background(backgroundColor)
        .clickable(onClick = onClick)
        .padding(8.dp)

    if (fullWidth)
        modifier2.fillMaxWidth()

    Row(
        modifier = modifier2,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            color = contentColor,
            modifier=if(fullWidth) Modifier.weight(0.9f) else Modifier.padding(horizontal = 8.dp),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
        icon?.let {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint=contentColor
            )
        }

    }
}


@Composable
fun RoundedButtonProgress(
    text:String,
    modifier:Modifier=Modifier,
    backgroundColor:Color=MaterialTheme.colors.primary,
    contentColor:Color=contentColorFor(backgroundColor = backgroundColor),
    fullWidth:Boolean=false,
    outerPadding:PaddingValues= PaddingValues(0.dp),
    icon:ImageVector?=null,
    isLoading:Boolean=false,
    onClick:()->Unit
) {

    val modifier2= modifier
        .padding(outerPadding)
        .clip(CircleShape)
        .background(backgroundColor)
        .clickable(onClick = onClick)
        .padding(12.dp)

    if (fullWidth)
        modifier2.fillMaxWidth()

    Row(
        modifier = modifier2,
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (!isLoading)
            Text(
                text = text,
                color = contentColor,
                modifier=if(fullWidth) Modifier.weight(0.9f) else Modifier.padding(horizontal = 8.dp),
                textAlign = TextAlign.Center
            )

        if (isLoading)
            CircularProgressIndicator(
                color = contentColor,
                modifier = Modifier.size(25.dp),
                strokeWidth = 3.dp
            )
        else
            icon?.let {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint=contentColor
                )
            }

    }
}