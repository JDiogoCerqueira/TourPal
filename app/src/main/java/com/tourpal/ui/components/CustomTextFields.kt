package com.tourpal.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tourpal.R
import com.tourpal.ui.theme.TourPalTheme
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon

@Composable
fun BasicTextInput(
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder) },
        maxLines = 1,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun PasswordInput(
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var passwordVisible by remember { mutableStateOf(false)}

    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder) },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        colors = TextFieldDefaults.colors(
        ),
        maxLines = 1,
        shape = RoundedCornerShape(8.dp),
        trailingIcon = {
            val icon = if (passwordVisible) {
                painterResource(id = R.drawable.ic_visibility)
            } else {
                painterResource(id = R.drawable.ic_visibility_off)
            }
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(painter = icon, contentDescription = if (passwordVisible) "Hide password" else "Show password")
            }
        },
        modifier = modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
fun BasicTextInputPreview() {
    TourPalTheme {
        BasicTextInput(
            placeholder = "Username",
            value = "",
            onValueChange = {}
            )
    }
}
@Preview
@Composable
fun PasswordInputPreview() {
    TourPalTheme {
        PasswordInput(
            placeholder = "Password",
            value = "",
            onValueChange = {}
        )
    }
}