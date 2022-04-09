package com.example.memesmaker.ui.customMeme

import android.app.Activity
import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.Window
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.memesmaker.R
import com.example.memesmaker.data.CustomMemeItems
import com.example.memesmaker.data.ScreenState
import com.example.memesmaker.data.Tools
import com.example.memesmaker.database.MemeEntity
import com.example.memesmaker.database.MemesDatabase
import com.example.memesmaker.util.SaveMemeToStorage
import com.example.memesmaker.util.ViewToBitmap
import com.example.memesmaker.util.ads.GetAdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlinx.coroutines.launch

class CustomMemeViewModel(private val app:Application) :AndroidViewModel(app) {

    //ads
    val adRequest= GetAdRequest(app)
    var interstitial: InterstitialAd?=null

    init {
        InterstitialAd.load(
            app,
            app.getString(R.string.custom_meme_interstitial),
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(p0: InterstitialAd) {
                    super.onAdLoaded(p0)
                    interstitial=p0
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    Log.d("CustomMemeViewModel",p0.message)
                }
            }
        )
    }

    private val db = MemesDatabase.getInstance(app)

    val items = mutableStateListOf<CustomMemeItems>()

    val background = mutableStateOf<Bitmap?>(null)

    val currentTool = mutableStateOf(Tools.NONE)

    val selectedItem = mutableStateOf<CustomMemeItems?>(null)

    var customMemeView by mutableStateOf<CustomMemeView?>(null)

    var state by mutableStateOf(ScreenState.IDLE)

    var uri:Uri?=null

    fun selectItem(item:CustomMemeItems){
        selectedItem.value = if (selectedItem.value?.timestamp==item.timestamp)
            null
        else
            item
    }

    fun addText(
        text:String,
        color:Color
    ){
        val item= CustomMemeItems(
            type = CustomMemeItems.TYPE_TEXT,
            text = text,
            textColor = color,
            timestamp = System.currentTimeMillis()
        )
        items.add(item)
    }

    fun addImage(
        bitmap:Bitmap?
    ){
        if (bitmap==null)
            return
        val item=CustomMemeItems(
            type = CustomMemeItems.TYPE_IMAGE,
            bitmap = bitmap,
            timestamp = System.currentTimeMillis()
        )

        items.add(item)
    }

    fun remove(item:CustomMemeItems?){
        if (item==null)
            return
        items.remove(item)
        selectedItem.value=null
    }

    fun save(activity: Activity){
        if (items.isEmpty()){
            state=ScreenState.ERROR.apply { message= "You need to add at least one text or image to save this meme" }
            return
        }

        interstitial?.show(activity)

        state=ScreenState.LOADING
        selectedItem.value=null
        ViewToBitmap(customMemeView!!,activity.window){bitmap->

            SaveMemeToStorage(app,bitmap){result->

                result.onSuccess {
                    uri=it
                    val item=MemeEntity(0,it.toString())
                    viewModelScope.launch {
                        db.memes().put(item)
                    }
                    state=ScreenState.DONE
                }

                result.onFailure {
                    state=ScreenState.ERROR.apply { message=it.message }
                }

            }

        }
    }

}