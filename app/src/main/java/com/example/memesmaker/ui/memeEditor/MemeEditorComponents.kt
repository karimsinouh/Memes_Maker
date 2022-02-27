package com.example.memesmaker.ui.memeEditor

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.memesmaker.R
import com.example.memesmaker.data.Meme
import com.example.memesmaker.data.Tools
import com.example.memesmaker.data.getAllTools

@Composable
fun MemeTemplate(meme: Meme,onTextClicked:()->Unit,onImageClicked:()->Unit) {
    Column(
        modifier= Modifier
            .background(Color.White)
            .padding(meme.padding?.dp ?: 12.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = meme.text!!,
            fontSize = meme.textSize?.sp?:24.sp,
            color = Color(meme.textColor!!),
            modifier = Modifier.clickable {
                onTextClicked()
            }
        )

        val image = meme.picture?.asImageBitmap()
        val alternateImage= painterResource(id = R.drawable.ic_launcher_background)

        Spacer(modifier = Modifier.height(12.dp))

        if (image!=null)
            Image(
                bitmap = image,
                contentDescription = null,
                modifier= Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(meme.corners?.dp ?: 24.dp))
                    .height(meme.imageHeight?.dp ?: 200.dp)
                    .clickable { onImageClicked() },
                contentScale = ContentScale.Crop
            )
        else
            Image(
                painter = alternateImage,
                contentDescription = null,
                modifier= Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(meme.corners?.dp ?: 24.dp))
                    .height(meme.imageHeight?.dp ?: 200.dp)
                    .clickable { onImageClicked() },
                contentScale = ContentScale.Crop
            )

        Text(
            text = meme.credits!!,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
            modifier=Modifier.align(Alignment.End)
        )

    }
}


@Composable
fun MemeEditorAppBar(
    onBack:()->Unit,
    onSave:()->Unit,
    title:String,
) {
    TopAppBar(
        title={ Text(text = title) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = null)
            }
        },
        actions = {
            IconButton(onClick = onSave) {
                Icon(
                    painter= painterResource(id = R.drawable.ic_save),
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary
                )
            }
        },
        backgroundColor = MaterialTheme.colors.surface,
        contentColor = MaterialTheme.colors.onSurface,
        elevation = 0.dp
    )
}

@Composable
fun MemeTools(
    onToolClicked:(Tools)->Unit
) {
    val scrollState = rememberScrollState()
    Row(
        modifier= Modifier
            .horizontalScroll(scrollState)
            .fillMaxWidth(),
    ) {
        getAllTools().forEach {
            ToolsButton(tool = it) {
                onToolClicked(it)
            }
        }
    }
}

@Composable
fun ToolsButton(
    tool: Tools,
    onClick:()->Unit
) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .clickable { onClick() }
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val painter= painterResource(id = tool.icon)
        Icon(painter = painter, contentDescription = null)
        Text(text = tool.text)
    }
}

@Composable
fun SliderTool(
    toolName:String,
    value:Float?,
    range:ClosedFloatingPointRange<Float>,
    modifier: Modifier= Modifier
        .fillMaxWidth()
        .padding(12.dp),
    onTextSizeChange:(Float)->Unit,
) {
    Column(modifier=modifier,verticalArrangement = Arrangement.spacedBy(12.dp)) {

        Text(text = toolName)

        Slider(
            value = value?:24f,
            onValueChange = {onTextSizeChange(it)},
            valueRange = range
        )

    }

}