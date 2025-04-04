package com.tourpal.ui.screens

import TopBar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tourpal.ui.components.BasicTextInput
import com.tourpal.ui.components.DefaultButton
import com.tourpal.ui.theme.TourPalTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import java.text.SimpleDateFormat
import java.util.*
import com.tourpal.ui.components.DatePickerModal

@Composable
fun UpdateProfilePage(navController: NavHostController) {
    var username by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var birthdate by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }

    // Function to update the birthdate when the date is selected
    val onDateSelected: (Long?) -> Unit = { selectedDate ->
        selectedDate?.let {
            // Format the selected date
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            birthdate = dateFormat.format(Date(it))
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar(text="Create your Profile")
        Spacer(modifier = Modifier.height(16.dp))

        // Username input
        BasicTextInput("Username", username, { username = it })
        Spacer(modifier = Modifier.height(16.dp))

        // Birthdate input as a clickable TextField
        Text(
            text = "Birthdate: ${if (birthdate.isNotEmpty()) birthdate else "Select a date"}",
            modifier = Modifier
                .clickable { showDatePicker = true }
                .padding(16.dp)
        )

        // Show Date Picker Modal when required
        if (showDatePicker) {
            DatePickerModal(
                onDateSelected = { date ->
                    onDateSelected(date)
                    showDatePicker = false
                },
                onDismiss = { showDatePicker = false }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Description input
        BasicTextInput("Description", description, { description = it })
        Spacer(modifier = Modifier.height(16.dp))

        // OK button (action to save the profile, etc.)
        DefaultButton(s = "OK", onClick = {}, enabled = true)
    }
}

@Preview
@Composable
fun UpdateProfilePagePreview() {
    TourPalTheme {
        UpdateProfilePage(rememberNavController())
    }
}
