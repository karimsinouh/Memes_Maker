package com.karimsinouh.memesmaker.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.karimsinouh.memesmaker.R
import com.karimsinouh.memesmaker.ui.customMeme.CustomMemeActivity
import com.karimsinouh.memesmaker.ui.items.SwipeableMemeItem
import com.karimsinouh.memesmaker.ui.standardMeme.StandardMemeActivity
import com.karimsinouh.memesmaker.ui.theme.MemesMakerTheme
import com.karimsinouh.memesmaker.util.ShareImage
import com.karimsinouh.memesmaker.util.ads.AnchoredAdaptiveBanner
import com.karimsinouh.memesmaker.util.customComponents.MessageScreen
import com.karimsinouh.memesmaker.util.customComponents.RoundedButton
import com.karimsinouh.memesmaker.util.customComponents.customStickyHeader
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import java.util.*

class MainActivity : ComponentActivity() {

    private val vm by viewModels<MainViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply{
            this.setKeepOnScreenCondition{
                vm.memesList.value!=null
            }
        }

        setContent {
            MemesMakerTheme {

                window.statusBarColor=MaterialTheme.colors.surface.toArgb()

                Scaffold(
                    content={Content()},
                    topBar = { MainTopBar() },
                    backgroundColor = MaterialTheme.colors.background,
                    bottomBar = {
                        AnchoredAdaptiveBanner(
                            adUnitId = stringResource(id = R.string.main_activity_banner),
                            adRequest = vm.adRequest
                        )
                    }
                )
            }
        }

        //requestTestAds()
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    private fun Content() {

        val memes=vm.memesList.observeAsState().value ?: emptyList()

        if (memes.isEmpty()){
            MessageScreen(
                title= stringResource(R.string.bit_empty),
                text = stringResource(R.string.no_memes_yet)
            ) {
                RoundedButton(text = stringResource(R.string.create)) {
                    openActivity(StandardMemeActivity::class.java)
                }
            }
            return
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(12.dp)
        ){

            customStickyHeader("Create New")

            item {
                ChooseTemplate(
                    onCustom = { openActivity(CustomMemeActivity::class.java) },
                    onStandard = {openActivity(StandardMemeActivity::class.java)}
                )
            }

            customStickyHeader("Created Memes")

            items(
                items=memes,
                key={
                    it.id
                }
            ){meme->

                val boxModifier=if (vm.isSelected(meme.id))
                    Modifier
                        .border(
                            6.dp,
                            MaterialTheme.colors.primary,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .animateItemPlacement()
                else
                    Modifier.animateItemPlacement()

                Box(boxModifier){
                    SwipeableMemeItem(
                        memeEntity = meme,
                        onLongClick = {
                            vm.select(meme.id)
                        },
                        onClick = {
                            if (vm.isSelectionMode())
                                vm.select(meme.id)
                        },
                        onDelete = {
                                   vm.delete(meme)
                        },
                        onShare = {
                            ShareImage(this@MainActivity, Uri.parse(meme.memePath))
                        }
                    )
                }

            }
        }
    }

    private fun <T> openActivity(activity:Class<T>){
        val i=Intent(this,activity)
        startActivity(i)
    }

    @Composable
    @Preview
    private fun MainTopBar() {
        Column {
            TopAppBar(
                title = {
                    Text(
                        text = getString(R.string.app_name),
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    AnimatedVisibility(visible = vm.isSelectionMode()) {
                        IconButton(onClick = vm::delete) {
                            Icon(Icons.Default.Delete, null)
                        }
                    }

                    AnimatedVisibility(visible = !vm.isSelectionMode()) {
                        IconButton(onClick = { openActivity(StandardMemeActivity::class.java) }) {
                            Icon(Icons.Default.Add, null)
                        }
                    }

                },
                backgroundColor = MaterialTheme.colors.surface,
                contentColor = MaterialTheme.colors.onSurface,
                elevation = 0.dp,
            )
            Divider()
        }
    }

    private fun requestTestAds(){
        val configurations= RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("CB7256F8AC9A39A0DAC57B133AAC720F"))
        MobileAds.setRequestConfiguration(configurations.build())
    }
}
