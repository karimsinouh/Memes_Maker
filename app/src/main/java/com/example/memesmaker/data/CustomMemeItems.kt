package com.example.memesmaker.data

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color

data class CustomMemeItems(
    val type:Int,
    val text:String?=null,
    val textColor: Color?=Color.White,
    val bitmap:Bitmap?=null,
    val timestamp:Long,
){

    companion object{
        const val TYPE_TEXT=0
        const val TYPE_IMAGE=1
    }

    fun isText()=type== TYPE_TEXT
}
