package com.example.memesmaker.ui.customMeme

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.memesmaker.data.Tools
import com.example.memesmaker.ui.theme.MemesMakerTheme
import com.example.memesmaker.util.ImagePicker
import com.example.memesmaker.util.customComponents.DialogInput
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

                    if (vm.currentTool==Tools.ADD_IMAGE)
                        vm.addImage(bitmap)
                    else
                        vm.background=bitmap

                    vm.currentTool=Tools.NONE
                }
            }

            MemesMakerTheme {

                window.statusBarColor=MaterialTheme.colors.surface.toArgb()

                Scaffold(
                    topBar = {
                        CustomMemeTopBar(
                            onBack = ::finish,
                            onSave = {},
                            onDelete = {vm.remove(vm.selectedItem)},
                            selectedItem = vm.selectedItem
                        )
                    },
                    content = { Content() },
                    backgroundColor = MaterialTheme.colors.surface
                )

            }

        }

    }

    @Composable
    private fun Content(){

        if(vm.currentTool==Tools.ADD_TEXT)
            DialogInput(
                title = "Add Text",
                value = "",
                button = "Add",
                placeholder = "Text",
                onConfirm = {
                    vm.addText(it, Color.White)
                    vm.currentTool=Tools.NONE
                },
                onDismiss = {vm.currentTool=Tools.NONE}
            )


        Column {

            CustomMemeTemplate(
                background = vm.background,
                items = vm.items,
                onBackgroundClicked = {
                    vm.currentTool= Tools.BACKGROUND
                    launcher.launch("image/*")
                },
                onItemSelected = {
                    vm.selectItem(it)
                },
                selectedItem = vm.selectedItem
            )

            Divider()

            CustomMemeTools(
                currentTool = vm.currentTool,
                onToolClicked = {
                    vm.currentTool = it
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