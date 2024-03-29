package com.karimsinouh.memesmaker.ui.standardMeme

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import com.karimsinouh.memesmaker.R
import com.karimsinouh.memesmaker.data.ScreenState
import com.karimsinouh.memesmaker.data.Tools
import com.karimsinouh.memesmaker.ui.theme.MemesMakerTheme
import com.karimsinouh.memesmaker.util.ImagePicker
import com.karimsinouh.memesmaker.util.ShareImage
import com.karimsinouh.memesmaker.util.ads.AnchoredAdaptiveBanner
import com.karimsinouh.memesmaker.util.customComponents.CenterProgress
import com.karimsinouh.memesmaker.util.customComponents.DialogInput
import com.karimsinouh.memesmaker.util.customComponents.MessageScreen
import com.karimsinouh.memesmaker.util.customComponents.RoundedButton
import com.theartofdev.edmodo.cropper.CropImage

class StandardMemeActivity: ComponentActivity() {


    private val vm by viewModels<StandardMemeViewModel>()

    private val imagePicker by lazy {
        ImagePicker(this)
    }


    private lateinit var cropLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>

    private lateinit var launcher:ManagedActivityResultLauncher<String, Uri?>

    private lateinit var memeCaptureView: MutableState<MemeTemplateCustomView>


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
                                title = stringResource(id = R.string.congrats),
                                text = getString(R.string.meme_created),
                                button = {
                                    RoundedButton(text = getString(R.string.share)) {
                                        ShareImage(this,vm.uri)
                                    }
                                }
                            )
                            ScreenState.ERROR -> {
                                MessageScreen(
                                    title = stringResource(id = R.string.error),
                                    text = vm.state.message ?: "idk bro"
                                )
                            }
                            ScreenState.IDLE -> Content()
                        }
                    },
                    backgroundColor = MaterialTheme.colors.surface,
                    bottomBar = {
                        AnchoredAdaptiveBanner(
                            adUnitId = stringResource(id = R.string.standard_meme_banner),
                            adRequest = vm.adRequest
                        )
                    }
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
            onSave = {
                     vm.save(memeCaptureView.value,this)
            },
            title = vm.meme.value.memeName?:"",
            isDark = vm.meme.value.dark,
            onDarkSwitched = {
                vm.onDarkSwitched(it)
            }
        )
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