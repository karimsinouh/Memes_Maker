package com.example.memesmaker.data

import androidx.annotation.DrawableRes
import com.example.memesmaker.R

enum class Tools(
    val text:String="",
    @DrawableRes val icon:Int= R.drawable.ic_launcher_background,
    val locked:Boolean=false,
) {
    TEXT("Meme Text",R.drawable.ic_text_size),
    TEXT_SIZE("Text Size",R.drawable.ic_text_size),
    TEXT_COLOR("Color",R.drawable.ic_color),
    CREDITS("Credits",R.drawable.ic_email),
    PICTURE("Picture",R.drawable.ic_image),
    NONE("None",0),
    PADDING("Padding",R.drawable.ic_padding),
    BACKGROUND("Background",R.drawable.ic_color),
    IMAGE_HEIGHT("Image Height",R.drawable.ic_height),
    CORNERS("Corners",R.drawable.ic_rounded_corner)
}

fun getAllTools()= listOf(
    Tools.TEXT_SIZE,
    Tools.TEXT_COLOR,
    Tools.CREDITS,
    Tools.PICTURE,
    Tools.PADDING,
    Tools.BACKGROUND,
    Tools.IMAGE_HEIGHT,
    Tools.CORNERS,
)