package com.karimsinouh.memesmaker.ui.customMeme

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
import com.karimsinouh.memesmaker.util.toBitmap
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
                        if(vm.state==ScreenState.IDLE)
                            CustomMemeTopBar(
                                onBack = ::finish,
                                onSave = { vm.save(this) },
                                onDelete = {vm.remove(vm.selectedItem.value)},
                                selectedItem = vm.selectedItem.value
                            )
                    },
                    content = {
                        when(vm.state){
                            ScreenState.LOADING -> CenterProgress()
                            ScreenState.DONE -> MessageScreen(
                                title = getString(R.string.congrats),
                                text = getString(R.string.meme_created),
                                button = {
                                    RoundedButton(text = getString(R.string.share)) {
                                        ShareImage(this,vm.uri)
                                    }
                                }
                            )
                            ScreenState.ERROR -> {
                                MessageScreen(
                                    title = getString(R.string.error),
                                    text = vm.state.message ?: "idk bro",
                                    button={
                                        RoundedButton(text = getString(R.string.okkay)) {
                                            vm.state=ScreenState.IDLE
                                        }
                                    }
                                )
                            }
                            ScreenState.IDLE -> Content()
                        }
                    },
                    backgroundColor = MaterialTheme.colors.surface,
                    bottomBar = {
                        AnchoredAdaptiveBanner(
                            adUnitId = stringResource(id = R.string.custom_meme_banner),
                            adRequest = vm.adRequest
                        )
                    }
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
                title = stringResource(R.string.add_text),
                value = "",
                button = stringResource(R.string.add),
                placeholder = stringResource(R.string.text),
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