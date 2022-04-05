package com.example.memesmaker.ui.customMeme

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import com.example.memesmaker.R
import com.example.memesmaker.data.ScreenState
import com.example.memesmaker.data.Tools
import com.example.memesmaker.ui.theme.MemesMakerTheme
import com.example.memesmaker.util.ImagePicker
import com.example.memesmaker.util.ShareImage
import com.example.memesmaker.util.customComponents.CenterProgress
import com.example.memesmaker.util.customComponents.DialogInput
import com.example.memesmaker.util.customComponents.MessageScreen
import com.example.memesmaker.util.customComponents.RoundedButton
import com.example.memesmaker.util.toBitmap
import com.theartofdev.edmodo.cropper.CropImage

class CustomMemeActivity:ComponentActivity() {

    private val vm by viewModels<CustomMemeViewModel>()

    private val imagePicker by lazy {
        ImagePicker(this)
    }


    private lateinit var cropLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>

    private lateinit var launcher: ManagedActivityResultLauncher<String, Uri?>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            launcher= rememberLauncherForActivityResult(contract = imagePicker.contract) {
                it?.let {
                    imagePicker.openCrop(it,cropLauncher)
                }
            }

            cropLauncher =  rememberLauncherForActivityResult(contract = imagePicker.cropContract) {
                if (it.resultCode== RESULT_OK && it.data!=null){
                    val uri= CropImage.getActivityResult(it.data).uri

                    val bitmap=uri.toBitmap(this)

                    if (vm.currentTool.value==Tools.ADD_IMAGE)
                        vm.addImage(bitmap)
                    else
                        vm.background.value=bitmap

                    vm.currentTool.value=Tools.NONE
                }
            }



            MemesMakerTheme {

                window.statusBarColor=MaterialTheme.colors.surface.toArgb()

                Scaffold(
                    topBar = {
                        CustomMemeTopBar(
                            onBack = ::finish,
                            onSave = { vm.save(window) },
                            onDelete = {vm.remove(vm.selectedItem.value)},
                            selectedItem = vm.selectedItem.value
                        )
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

        vm.customMemeView=
            CustomMemeView(
                context = this,
                background = vm.background,
                items = vm.items,
                onBackgroundClicked = {
                    vm.currentTool.value= Tools.BACKGROUND
                    launcher.launch("image/*")
                },
                onItemSelected = {
                    vm.selectItem(it)
                },
                selectedItem = vm.selectedItem
            )

    }

    @Composable
    private fun Content(){

        if(vm.currentTool.value==Tools.ADD_TEXT)
            DialogInput(
                title = "Add Text",
                value = "",
                button = "Add",
                placeholder = "Text",
                onConfirm = {
                    vm.addText(it, Color.White)
                    vm.currentTool.value=Tools.NONE
                },
                onDismiss = {vm.currentTool.value=Tools.NONE}
            )


        Column {

            AndroidView(
                factory = { _ ->
                    if (vm.customMemeView!=null)
                        vm.customMemeView!!
                    else
                        View(this@CustomMemeActivity)
                }
            )

            Divider()

            CustomMemeTools(
                currentTool = vm.currentTool.value,
                onToolClicked = {
                    vm.currentTool.value= it
                    when(it){
                        Tools.ADD_IMAGE->launcher.launch("image/*")
                        Tools.BACKGROUND->launcher.launch("image/*")
                        else->Unit
                    }
                }
            )
        }

    }

}