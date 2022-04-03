package com.example.memesmaker.ui.memeEditor

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.memesmaker.data.Meme
import com.example.memesmaker.data.ScreenState
import com.example.memesmaker.data.Tools
import com.example.memesmaker.database.MemeEntity
import com.example.memesmaker.database.MemesDatabase
import com.example.memesmaker.util.toBitmap
import kotlinx.coroutines.launch

class MemeEditorViewModel(app:Application): AndroidViewModel(app) {

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
        }
    }

}