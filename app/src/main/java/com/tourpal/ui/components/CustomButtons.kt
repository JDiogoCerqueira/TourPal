package com.tourpal.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import com.tourpal.ui.theme.TourPalColors as TPColors
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tourpal.R

@Composable
fun DefaultButton(s: String, onClick: () -> Unit, modifier: Modifier = Modifier, enabled: Boolean = true) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled
    ){
        Text(
            text = s,
            color = TPColors.onPrimary,
        )
    }
}


@Composable
fun GoogleSignInButton(onClick: () -> Unit) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(10.dp)) // Add shadow here
    ) {
        Image(
            painter = painterResource(id = R.drawable.google_logo),
            contentDescription = "Google Icon",
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = "Sign in with Google",
            modifier = Modifier.padding(start = 10.dp),
            color = TPColors.onPrimary
        )
    }
}


@Preview
@Composable
fun GoogleSignInButtonPreview() {
    GoogleSignInButton(onClick = {})
}

@Preview
@Composable
fun DefaultButtonPreview() {
    DefaultButton(s = "Login", onClick = {}, enabled = true)
}

