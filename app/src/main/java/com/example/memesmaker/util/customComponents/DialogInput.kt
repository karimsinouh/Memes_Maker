package com.example.memesmaker.util.customComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun DialogInput(
    title:String,
    value: String,
    button:String,
    placeholder: String,
    onConfirm:(String)->Unit,
    onDismiss:()->Unit
) {

    var value2 by remember {
        mutableStateOf(value)
    }

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colors.surface)
                .padding(12.dp)
        ) {

            Text(
                text = title,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )

            CustomInput(
                modifier = Modifier
                    .weight(0.9f)
                    .fillMaxWidth(),
                value=value2,
                placeholder = placeholder
            ){
                value2=it
            }


            RoundedButton(text = button,fullWidth = true) {
                onConfirm(value2)
            }
        }
    }


}

@Composable
private fun CustomInput(
    modifier: Modifier,
    value:String,
    placeholder:String,
    onValueChanged:(String)->Unit,
) {
    Box(
        modifier=modifier,
        contentAlignment = Alignment.Center
    ){

        if (value.isEmpty())
            Text(
                text = placeholder,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
            )

        BasicTextField(
            value = value,
            onValueChange = {onValueChanged(it)},
            textStyle = TextStyle(
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
            ),
            modifier= Modifier
                .fillMaxWidth()
                .padding(12.dp),
            cursorBrush = SolidColor(MaterialTheme.colors.primary),

        )

    }
}