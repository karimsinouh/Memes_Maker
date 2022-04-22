package com.karimsinouh.memesmaker.util.ads

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView


@Composable
fun InlineAdaptiveBanner(
    adUnitId:String,
    adRequest: AdRequest
) {
    val context= LocalContext.current


    BoxWithConstraints(
        modifier=Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        val adSize=AdSize.getCurrentOrientationInlineAdaptiveBannerAdSize(context,maxWidth.value.toInt())
        AndroidView(
            factory = {
                AdView(context).apply {
                    this.adUnitId=adUnitId
                    this.adSize= adSize
                    this.loadAd(adRequest)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Composable
fun AnchoredAdaptiveBanner(
    adUnitId:String,
    adRequest: AdRequest
) {
    val context= LocalContext.current


    BoxWithConstraints(
        modifier=Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        val adSize=AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context,maxWidth.value.toInt())
        AndroidView(
            factory = {
                AdView(context).apply {
                    this.adUnitId=adUnitId
                    this.adSize= adSize
                    this.loadAd(adRequest)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun Banner(
    adUnitId:String,
    adRequest: AdRequest,
) {
    val context= LocalContext.current

    AndroidView(
        factory = {
            AdView(context).apply {
                this.adUnitId=adUnitId
                this.adSize= AdSize.BANNER
                this.loadAd(adRequest)
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}