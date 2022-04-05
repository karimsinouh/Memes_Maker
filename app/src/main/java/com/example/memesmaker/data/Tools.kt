package com.example.memesmaker.data

import androidx.annotation.DrawableRes
import com.example.memesmaker.R

enum class Tools(
    val text:String="",
    @DrawableRes val icon:Int= R.drawable.ic_launcher_background,
    val locked:Boolean=false,
) {
    //standard meme
    TEXT("Meme Text",R.drawable.ic_text_size),
    TEXT_SIZE("Text Size",R.drawable.ic_text_size),
    CREDITS("Credits",R.drawable.ic_email),
    NONE("None",0),
    PADDING("Padding",R.drawable.ic_padding),
    IMAGE_HEIGHT("Image Height",R.drawable.ic_height),
    CORNERS("Corners",R.drawable.ic_rounded_corner),

    //custom meme
    ADD_TEXT("Add Text",R.drawable.ic_text_size),
    ADD_IMAGE("Add Image",R.drawable.ic_image),
    BACKGROUND("Background Image",R.drawable.ic_background)

}

fun getAllTools()= listOf(
    Tools.TEXT_SIZE,
    Tools.CREDITS,
    Tools.PADDING,
    Tools.IMAGE_HEIGHT,
    Tools.CORNERS,
)

fun getCustomMemeTools()= listOf(Tools.BACKGROUND,Tools.ADD_TEXT,Tools.ADD_IMAGE)