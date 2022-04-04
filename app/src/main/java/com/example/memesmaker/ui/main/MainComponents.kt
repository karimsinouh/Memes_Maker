package com.example.memesmaker.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.memesmaker.R

@Composable
fun RowScope.CreateMemeTemplate(
    icon:Painter,
    title:String,
    background:Color,
    onClick:()->Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(background)
            .clickable { onClick() }
            .padding(32.dp)
            .weight(0.5f),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = icon,
            contentDescription = null,
            colorFilter = ColorFilter.tint(contentColorFor(backgroundColor = background)),
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            color = contentColorFor(backgroundColor = background),
            fontSize = 12.sp
        )
    }
}


@Composable
fun ChooseTemplate(
    onStandard:()->Unit,
    onCustom:()->Unit
) {
    Row {
        CreateMemeTemplate(
            icon = painterResource(id = R.drawable.ic_custom),
            title = stringResource(R.string.custom),
            background = MaterialTheme.colors.primary,
            onClick = onCustom
        )

        Spacer(modifier = Modifier.width(12.dp))

        CreateMemeTemplate(
            icon = painterResource(id = R.drawable.ic_create),
            title = stringResource(R.string.standard),
            background = MaterialTheme.colors.surface,
            onClick = onStandard
        )
    }
}