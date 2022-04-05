package com.example.memesmaker.ui.memeEditor

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.content.pm.LauncherActivityInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import com.example.memesmaker.R
import com.example.memesmaker.data.ScreenState
import com.example.memesmaker.data.Tools
import com.example.memesmaker.ui.theme.MemesMakerTheme
import com.example.memesmaker.util.ImagePicker
import com.example.memesmaker.util.SaveMemeToStorage
import com.example.memesmaker.util.ShareImage
import com.example.memesmaker.util.ViewToBitmap
import com.example.memesmaker.util.customComponents.CenterProgress
import com.example.memesmaker.util.customComponents.DialogInput
import com.example.memesmaker.util.customComponents.MessageScreen
import com.example.memesmaker.util.customComponents.RoundedButton
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView

class MemeEditor: ComponentActivity() {


    private val vm by viewModels<MemeEditorViewModel>()

    private val imagePicker by lazy {
        ImagePicker(this)
    }


    private lateinit var cropLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>

    private lateinit var launcher:ManagedActivityResultLauncher<String, Uri?>

    private lateinit var memeCaptureView: MutableState<MemeTemplateCustomView>

    private val saveMemeToStorage=SaveMemeToStorage()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            MemesMakerTheme {

                window.statusBarColor=MaterialTheme.colors.surface.toArgb()

                launcher=rememberLauncherForActivityResult(contract = imagePicker.contract) {
                    it?.let {
                        imagePicker.openCrop(it,cropLauncher)
                    }
                }

                cropLauncher =  rememberLauncherForActivityResult(contract = imagePicker.cropContract) {
                    if (it.resultCode==Activity.RESULT_OK && it.data!=null){
                        val uri=CropImage.getActivityResult(it.data).uri
                        vm.setBitmap(uri,this)
                    }
                }



                Scaffold(
                    topBar = {
                        if(vm.state==ScreenState.IDLE)
                            ThisTopBar()
                             },
                    content = {
                        when(vm.state){
                            ScreenState.LOADING -> CenterProgress()
                            ScreenState.DONE -> MessageScreen(
                                title = "Congrats!",
                                text = getString(R.string.meme_created),
                                button = {
                                    RoundedButton(text = getString(R.string.share)) {
                                        ShareImage(this,vm.uri)
                                    }
                                }
                            )
                            ScreenState.ERROR -> {
                                MessageScreen(
                                    title = "Error",
                                    text = vm.state.message ?: "idk bro"
                                )
                            }
                            ScreenState.IDLE -> Content()
                        }
                    },
                    backgroundColor = MaterialTheme.colors.surface
                )

            }

        }

        memeCaptureView= mutableStateOf(MemeTemplateCustomView(
            context = this,
            meme = vm.meme,
            onImageClicked = {
                launcher.launch("image/*")
            },
            onTextClicked = {
                vm.currentTool=Tools.TEXT
            }
        ))
    }


    @Composable
    private fun ThisTopBar() {
        MemeEditorAppBar(
            onBack = ::finish,
            onSave = ::save,
            title = vm.meme.value.memeName?:"",
            isDark = vm.meme.value.dark,
            onDarkSwitched = {
                vm.onDarkSwitched(it)
            }
        )
    }

    private fun save(){
        vm.state=ScreenState.LOADING
        ViewToBitmap(memeCaptureView.value,window){
            saveMemeToStorage(this,it){result->

                result.onSuccess {uri->
                    vm.uri=uri
                    vm.storeMeme()
                    vm.state=ScreenState.DONE
                }

                result.onFailure {
                    vm.state=ScreenState.ERROR.apply { message=it.message }
                }

            }
        }
    }

    @Composable
    private fun Content() {
        Column {
            Divider()
            MemeUi()
            Divider()
            MemeTools(currentTool = vm.currentTool,onToolClicked = { vm.currentTool = it })
            CurrentTool()
        }
    }


    @Composable
    private fun MemeUi() {

            AndroidView(
                factory = {_ ->
                    memeCaptureView.value
                }
            )

    }

    @Composable
    private fun CurrentTool() =
        when(vm.currentTool){
            Tools.TEXT -> MemeTextTool()
            Tools.TEXT_SIZE -> TextSizeTool()
            Tools.CREDITS -> CreditsTool()
            Tools.NONE -> Unit
            Tools.PADDING -> PaddingTool()
            Tools.IMAGE_HEIGHT -> ImageHeightTool()
            Tools.CORNERS -> CornersTool()
            else -> Unit
        }



    @Composable
    private fun PaddingTool() {
        SliderTool(
            toolName = vm.currentTool.text,
            value = vm.meme.value.padding,
            range = 0f .. 32f,
            onTextSizeChange = { vm.setPadding(it) }
        )
    }

    @Composable
    private fun CreditsTool(){
        DialogInput(
            title = vm.currentTool.text,
            value = vm.meme.value.credits?:"",
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
            value = vm.meme.value.imageHeight,
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
            value = vm.meme.value.text?:"",
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
            value = vm.meme.value.textSize,
            range = 12f .. 40f,
            onTextSizeChange = { vm.setTextSize(it) }
        )
    }

    @Composable
    private fun CornersTool() {
        SliderTool(
            toolName = vm.currentTool.text,
            value = vm.meme.value.corners,
            range = 0f .. 100f,
            onTextSizeChange = { vm.setCorners(it) }
        )
    }

}