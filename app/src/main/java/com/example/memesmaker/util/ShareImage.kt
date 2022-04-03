package com.example.memesmaker.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.memesmaker.R

object ShareImage {

    operator fun invoke(context:Context,uri:Uri?){
        if (uri==null)
            return
        val intent= Intent(Intent.ACTION_SEND).apply {
            type="image/*"
            putExtra(Intent.EXTRA_STREAM,uri)
            context.startActivity(Intent.createChooser(this,context.getString(R.string.share_meme)))
        }
    }

}