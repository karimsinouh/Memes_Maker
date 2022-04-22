package com.karimsinouh.memesmaker.util.ads

import android.content.Context
import com.google.android.gms.ads.AdRequest

object GetAdRequest {

    operator fun invoke(context:Context):AdRequest{
        return AdRequest.Builder().build()
    }

}