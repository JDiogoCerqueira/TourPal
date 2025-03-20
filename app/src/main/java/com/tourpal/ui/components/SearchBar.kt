package com.tourpal.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.tourpal.ui.theme.*
import androidx.compose.material.icons.filled.Search
import com.tourpal.R

@Composable
fun SearchBar(
    placeholder: String,
    value: String = "",
    trailingIcon: @Composable () -> Unit, onValueChange: (String) -> Unit = {}) {
    TextField(value = value, onValueChange = onValueChange, placeholder = {
        Text(placeholder, color = Gray)
    }, leadingIcon = {
        Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = Gray)
    }, trailingIcon = trailingIcon, colors = TextFieldDefaults.colors(
        focusedContainerColor = White,
        unfocusedContainerColor = White,
        disabledContainerColor = White,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent
    ), textStyle = TextStyle(color = Black, fontSize = 14.sp), modifier = Modifier
        .fillMaxWidth()
        .height(56.dp)
        .clip(RoundedCornerShape(8.dp))
    )
}


@Composable
fun LocationIcon() {
    Icon(
        painter = painterResource(id = R.drawable.outline_location_on_24),
        contentDescription = "Nearby",
        tint = Gray,
    )
}

@Composable
fun MicrophoneIcon() {
    Icon(
        painter = painterResource(id = R.drawable.baseline_mic_24),
        contentDescription = "Voice Search",
        tint = Gray
    )
}

@Preview
@Composable
fun Icons() {
    MicrophoneIcon()
    LocationIcon()
}

@Preview
@Composable
fun SearchBarPreview() {
    val textState = remember { mutableStateOf("") }

    SearchBar(
        placeholder = "Search...",
        value = textState.value,
        onValueChange = {
            textState.value = it
        }, trailingIcon = {
            MicrophoneIcon()
        }
    )
}

