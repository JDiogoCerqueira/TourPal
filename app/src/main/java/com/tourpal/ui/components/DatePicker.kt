@file:OptIn(ExperimentalMaterial3Api::class)

package com.tourpal.ui.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
fun DatePickerPreview() {
    var showDatePicker by remember { mutableStateOf(true) } // You can toggle this for showing/hiding the picker
    var selectedDate by remember { mutableStateOf<Long?>(null) }

    // The modal will show if showDatePicker is true
    if (showDatePicker) {
        DatePickerModal(
            onDateSelected = { date ->
                selectedDate = date
                showDatePicker = false // Close the picker after date is selected
            },
            onDismiss = { showDatePicker = false } // Close the picker if dismissed
        )
    }

    // Display the selected date or a default message if no date is selected
    Text(
        text = "Selected Date: ${selectedDate?.let { SimpleDateFormat("dd/MM/yyyy").format(Date(it)) } ?: "None"}",
        style = MaterialTheme.typography.bodyLarge // Updated style for Material3
    )
}

@Preview(showBackground = true)
@Composable
fun DatePickerPreviewScreen() {
    MaterialTheme {
        DatePickerPreview()
    }
}