package com.example.memesmaker.ui.customMeme

import android.app.Application
import android.graphics.Bitmap
import android.os.Build
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import com.example.memesmaker.data.CustomMemeItems
import com.example.memesmaker.data.Tools

class CustomMemeViewModel(app:Application) :AndroidViewModel(app) {

    val items = mutableStateListOf<CustomMemeItems>()

    var background by mutableStateOf<Bitmap?>(null)

    var currentTool by mutableStateOf(Tools.NONE)

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

    fun remove(item:CustomMemeItems){
        items.remove(item)
    }

}