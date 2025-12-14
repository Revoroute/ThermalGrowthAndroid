package co.uk.revoroute.thermalgrowth.ui.splash

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import co.uk.revoroute.thermalgrowth.R
import kotlinx.coroutines.delay

@Composable
fun SplashOverlay(onFinished: () -> Unit) {

    var visible by remember { mutableStateOf(true) }

    val screenHeight: Dp =
        LocalConfiguration.current.screenHeightDp.dp

    val offsetY by animateDpAsState(
        targetValue = if (visible) 0.dp else -screenHeight,
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        )
    )

    LaunchedEffect(Unit) {
        delay(800)      // slightly longer for branding
        visible = false // trigger fade
    }

    LaunchedEffect(visible) {
        if (!visible) {
            // wait for slide animation to finish
            delay(1050)
            onFinished()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .offset(y = offsetY),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon_splash),
                contentDescription = "App Splash Icon",
                modifier = Modifier.size(160.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Thermal Growth",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}