package com.karimsinouh.memesmaker.ui.standardMeme

import android.app.Activity
import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.karimsinouh.memesmaker.R
import com.karimsinouh.memesmaker.data.Meme
import com.karimsinouh.memesmaker.data.ScreenState
import com.karimsinouh.memesmaker.data.Tools
import com.karimsinouh.memesmaker.database.MemeEntity
import com.karimsinouh.memesmaker.database.MemesDatabase
import com.karimsinouh.memesmaker.util.SaveMemeToStorage
import com.karimsinouh.memesmaker.util.ViewToBitmap
import com.karimsinouh.memesmaker.util.ads.GetAdRequest
import com.karimsinouh.memesmaker.util.toBitmap
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlinx.coroutines.launch

class StandardMemeViewModel(private val app:Application): AndroidViewModel(app) {

    //ads
    val adRequest=GetAdRequest(app)
    var interstitial:InterstitialAd?=null

    init {
        InterstitialAd.load(
            app,
            app.getString(R.string.standard_meme_interstitial),
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(p0: InterstitialAd) {
                    super.onAdLoaded(p0)
                    interstitial=p0
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    Log.d("MemeEditorViewModel",p0.message)
                }
            }
        )
    }

    private val db = MemesDatabase.getInstance(app)

    val meme = mutableStateOf(Meme())
    var uri :Uri?=null

    var state by mutableStateOf(ScreenState.IDLE)

    var currentTool by mutableStateOf(Tools.NONE)

    fun setText(text:String){
        meme.value=meme.value.copy(text=text)
        unselectTools()
    }

    fun setCredits(credits:String){
        meme.value=meme.value.copy(credits = credits)
        unselectTools()
    }

    fun unselectTools(){
        currentTool=Tools.NONE
    }

    fun setTextSize(size:Float){
        meme.value=meme.value.copy(textSize = size)
    }

    fun setCorners(size:Float){
        meme.value=meme.value.copy(corners = size)
    }

    fun setImageHeight(it: Float) {
        meme.value=meme.value.copy(imageHeight = it)
    }

    fun setPadding(value:Float){
        meme.value=meme.value.copy(padding = value)
    }

    fun onDarkSwitched(value: Boolean) {
        meme.value=meme.value.copy(dark = value)
    }

    fun setBitmap(uri:Uri,context:Context){
        val bitmap=uri.toBitmap(context)
        meme.value=meme.value.copy(picture = bitmap)
    }

    fun storeMeme(){
        val entity=MemeEntity(0,uri.toString())
        viewModelScope.launch {
            db.memes().put(entity)
            state=ScreenState.DONE
        }
    }

    fun save(
        view:MemeTemplateCustomView,
        activity: Activity,
    ){
        interstitial?.show(activity)
        state=ScreenState.LOADING
        ViewToBitmap(view,activity.window){
            SaveMemeToStorage(app,it){result->

                result.onSuccess {uri->
                    this.uri=uri
                    storeMeme()
                }

                result.onFailure {
                    state= ScreenState.ERROR.apply { message=it.message }
                }

            }
        }
    }

}