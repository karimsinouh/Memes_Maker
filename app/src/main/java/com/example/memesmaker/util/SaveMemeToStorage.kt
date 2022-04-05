package com.example.memesmaker.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.example.memesmaker.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

object SaveMemeToStorage {

    operator fun invoke(context: Context,bitmap:Bitmap,listener:(Result<Uri>)->Unit){

        val path="${Environment.getExternalStorageDirectory()}/${context.getString(R.string.app_name)}"
        val fileName="${System.currentTimeMillis()}.png"

        val outputStream:OutputStream?
        val uri:Uri?

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {

            val resolver=context.contentResolver

            val values=ContentValues()
            values.put(MediaStore.MediaColumns.DISPLAY_NAME,fileName)
            values.put(MediaStore.MediaColumns.DISPLAY_NAME,"image/png")
            values.put(MediaStore.MediaColumns.RELATIVE_PATH,Environment.DIRECTORY_PICTURES)
            values.put(MediaStore.MediaColumns.MIME_TYPE,"image/png")
            uri=resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values)
            outputStream=resolver.openOutputStream(uri!!)

        }else{
            //check for permission first

            val directory=File(path)
            try {

                if (!directory.exists())
                    if (!directory.mkdir()) {
                        listener(Result.failure(Throwable(context.getString(R.string.permission_required))))
                        return
                    }


            }catch (e:IOException){
                listener(Result.failure(e))
            }

            val file=File(directory,fileName)
            outputStream=FileOutputStream(file)
            uri=Uri.fromFile(file)
            scanFile(context,file)
        }


        //try to save file
        try {

            CoroutineScope(Dispatchers.IO).launch {

                bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream)
                bitmap.recycle()

                withContext(Dispatchers.Main){
                    if (uri!=null)
                        listener(Result.success(uri))
                    else
                        listener(Result.failure(Throwable(context.getString(R.string.something_went_wrong))))
                }


            }

        }catch (e:IOException){
            listener(Result.failure(e))
        }


    }

    private fun scanFile(context: Context,file:File){
        MediaScannerConnection.scanFile(context, arrayOf(file.toString()), arrayOf(file.name),null)
    }

}