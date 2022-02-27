package com.example.memesmaker.data

import android.graphics.Bitmap

data class Meme(
    val text:String?="Text Here",
    val credits:String?="@Memes Maker",
    val picture:Bitmap?=null,
    val textSize:Float?=24f,
    val imageHeight:Float?=200f,
    val corners:Float?=24f,
    val memeName:String?="new meme",
    val padding:Float?=12f,
    val dark:Boolean?=false
)
