package co.uk.revoroute.thermalgrowth.ui.splash

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import co.uk.revoroute.thermalgrowth.R
import kotlinx.coroutines.delay

@Composable
fun SplashOverlay(onFinished: () -> Unit) {

    var visible by remember { mutableStateOf(true) }

    // Fade-out animation
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 600),
        finishedListener = {
            if (!visible) onFinished()
        }
    )

    LaunchedEffect(Unit) {
        delay(600)      // show briefly, like iOS
        visible = false // trigger fade
    }

    if (alpha > 0f) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .alpha(alpha),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon_splash),
                contentDescription = "App Splash Icon",
                modifier = Modifier.size(120.dp)
            )
        }
    }
}