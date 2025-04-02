package com.tourpal.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tourpal.R
import com.tourpal.ui.theme.TourPalTheme

@Composable
fun BasicTextInput(
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder) },
        maxLines = 1,
        shape = RoundedCornerShape(8.dp),
        keyboardOptions = keyboardOptions,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun PasswordInput(
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Password
    )
) {
    var passwordVisible by remember { mutableStateOf(false) }

    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder) },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        colors = TextFieldDefaults.colors(),
        maxLines = 1,
        shape = RoundedCornerShape(8.dp),
        keyboardOptions = keyboardOptions,
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
            onValueChange = {},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
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
            onValueChange = {},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
    }
}