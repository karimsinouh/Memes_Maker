package com.example.memesmaker.ui.memeEditor

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.memesmaker.data.Meme
import com.example.memesmaker.data.Tools
import com.example.memesmaker.util.toBitmap

class MemeEditorViewModel: ViewModel() {

    var meme by mutableStateOf(Meme())

    var currentTool by mutableStateOf(Tools.NONE)

    fun setText(text:String){
        meme=meme.copy(text=text)
        unselectTools()
    }

    fun setCredits(credits:String){
        meme=meme.copy(credits = credits)
        unselectTools()
    }

    fun unselectTools(){
        currentTool=Tools.NONE
    }

    fun setTextSize(size:Float){
        meme=meme.copy(textSize = size)
    }

    fun setCorners(size:Float){
        meme=meme.copy(corners = size)
    }

    fun setImageHeight(it: Float) {
        meme=meme.copy(imageHeight = it)
    }

    fun setPadding(value:Float){
        meme=meme.copy(padding = value)
    }

    fun onDarkSwitched(value: Boolean) {
        meme=meme.copy(dark = value)
    }

    fun setBitmap(uri:Uri,context:Context){
        val bitmap=uri.toBitmap(context)
        meme=meme.copy(picture = bitmap)
    }

}