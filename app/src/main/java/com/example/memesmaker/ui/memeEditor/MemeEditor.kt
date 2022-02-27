package com.example.memesmaker.ui.memeEditor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import com.example.memesmaker.data.Tools
import com.example.memesmaker.ui.theme.MemesMakerTheme
import com.example.memesmaker.util.customComponents.DialogInput

class MemeEditor: ComponentActivity() {


    private val vm by viewModels<MemeEditorViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            MemesMakerTheme {

                window.statusBarColor=MaterialTheme.colors.surface.toArgb()

                Scaffold(
                    topBar = {
                        MemeEditorAppBar(
                            onBack = ::finish,
                            onSave = { },
                            title = vm.meme.memeName?:""
                        )
                    }
                ) {

                    Content()

                }

            }

        }
    }


    @Composable
    private fun Content() {
        Column {
            Divider()
            MemeTemplate(
                meme = vm.meme,
                onTextClicked = {
                    vm.currentTool=Tools.TEXT
                },
                onImageClicked = {}
            )
            Divider()
            MemeTools(onToolClicked = { vm.currentTool = it })
            CurrentTool()
        }
    }

    @Composable
    private fun CurrentTool() =
        when(vm.currentTool){
            Tools.TEXT -> MemeTextTool()
            Tools.TEXT_SIZE -> TextSizeTool()
            Tools.TEXT_COLOR -> TODO()
            Tools.CREDITS -> CreditsTool()
            Tools.PICTURE -> TODO()
            Tools.NONE -> {}
            Tools.PADDING -> PaddingTool()
            Tools.BACKGROUND -> TODO()
            Tools.IMAGE_HEIGHT -> ImageHeightTool()
            Tools.CORNERS -> CornersTool()
        }



    @Composable
    private fun PaddingTool() {
        SliderTool(
            toolName = vm.currentTool.text,
            value = vm.meme.padding,
            range = 0f .. 32f,
            onTextSizeChange = { vm.setPadding(it) }
        )
    }

    @Composable
    private fun CreditsTool(){
        DialogInput(
            title = vm.currentTool.text,
            value = vm.meme.credits?:"",
            button = "Done",
            placeholder = "Enter Your name or instagram page...",
            onConfirm = { vm.setCredits(it) }
        ) {
            vm.unselectTools()
        }
    }

    @Composable
    private fun ImageHeightTool() {
        SliderTool(
            toolName = vm.currentTool.text,
            value = vm.meme.imageHeight,
            range = 100f..300f,
            onTextSizeChange = {
                vm.setImageHeight(it)
            }
        )
    }

    @Composable
    private fun MemeTextTool() {
        DialogInput(
            title = vm.currentTool.text,
            value = vm.meme.text?:"",
            button = "Done",
            placeholder = vm.currentTool.text,
            onConfirm = { vm.setText(it) }
        ) {
            vm.unselectTools()
        }
    }

    @Composable
    private fun TextSizeTool() {
        SliderTool(
            toolName = vm.currentTool.text,
            value = vm.meme.textSize,
            range = 12f .. 40f,
            onTextSizeChange = { vm.setTextSize(it) }
        )
    }

    @Composable
    private fun CornersTool() {
        SliderTool(
            toolName = vm.currentTool.text,
            value = vm.meme.corners,
            range = 0f .. 70f,
            onTextSizeChange = { vm.setCorners(it) }
        )
    }

}