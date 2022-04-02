package com.example.memesmaker.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.PixelCopy
import android.view.View
import android.view.Window
import androidx.annotation.RequiresApi

object ViewToBitmap {

    operator fun invoke(view:View,window: Window,callback: (Bitmap) -> Unit){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            aboveO(view, window, callback)
        else
            belowO(view)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun aboveO(view:View, window: Window, callback:(Bitmap)->Unit){
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val location = IntArray(2)
        view.getLocationInWindow(location)
        PixelCopy.request(window,
            Rect(location[0], location[1], location[0] + view.width, location[1] + view.height),
            bitmap,
            {
                if (it == PixelCopy.SUCCESS) {
                    callback.invoke(bitmap)
                }
            },
            Handler(Looper.getMainLooper())
        )
    }

    private fun belowO(view:View):Bitmap{
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