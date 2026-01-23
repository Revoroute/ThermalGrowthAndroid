package co.uk.revoroute.thermalgrowth.ui.components

import android.util.Log
import android.view.ViewGroup
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.android.gms.ads.*
import com.google.android.gms.ads.LoadAdError

@Composable
fun AdBanner(
    modifier: Modifier = Modifier,
    adUnitId: String
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Create the AdView once and keep it stable across recompositions
    val adView = remember {
        AdView(context).apply {
            setAdSize(AdSize.BANNER) // fine for now; adaptive is nicer later
            this.adUnitId = adUnitId
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            adListener = object : AdListener() {
                override fun onAdLoaded() {
                    Log.d("AdBanner", "Ad loaded")
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.w("AdBanner", "Ad failed: ${error.code} ${error.message}")
                }
            }
        }
    }

    // Reload key increments when screen resumes
    var reloadKey by remember { mutableIntStateOf(0) }

    // Tie the AdView to lifecycle + trigger reload on resume
    DisposableEffect(lifecycleOwner, adView) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    adView.resume()
                    reloadKey++
                }
                Lifecycle.Event.ON_PAUSE -> adView.pause()
                Lifecycle.Event.ON_DESTROY -> adView.destroy()
                else -> Unit
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            adView.destroy()
        }
    }

    // Actually load (or reload) the ad
    LaunchedEffect(reloadKey, adUnitId) {
        val request = AdRequest.Builder().build()
        adView.loadAd(request)
    }

    AndroidView(
        modifier = modifier,
        factory = { adView }
    )
}