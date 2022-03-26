package com.example.memesmaker.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View

object ViewToBitmap {
    operator fun invoke(view:View):Bitmap{
        val bitmap=Bitmap.createBitmap(view.width,view.height,Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)
        view.layout(
            view.left,
            view.top,
            view.right,
            view.bottom
        )
        view.draw(canvas)

        return bitmap
    }
}