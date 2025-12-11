package co.uk.revoroute.thermalgrowth.ui.components

import android.os.Bundle
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

/**
 * Universal AdMob banner container.
 * Mirrors the iOS BannerContainer() — fixed height and sits at bottom of screen.
 */
@Composable
fun AdBanner(
    modifier: Modifier = Modifier,
    adUnitId: String
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            // Initialize AdMob SDK once
            MobileAds.initialize(context) {}

            AdView(context).apply {
                setAdSize(AdSize.BANNER)

                // IMPORTANT:
                // Replace with your real Android Ad Unit ID before release.
                this.adUnitId = adUnitId

                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )

                val adRequest = AdRequest.Builder().build()
                loadAd(adRequest)
            }
        }
    )
}