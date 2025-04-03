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
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.SelectableDates

import java.util.Calendar


@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit,
    maxDate: Long? = null,  // For absolute maximum date
    minAge: Int = 18        // For age-based minimum date
) {
    val currentDate = Calendar.getInstance()
    val minBirthDate = Calendar.getInstance().apply {
        add(Calendar.YEAR, -minAge)
    }.timeInMillis

    // Calculate the initial displayed date (18 years ago today)
    val initialDisplayDate = Calendar.getInstance().apply {
        add(Calendar.YEAR, -minAge)
    }.timeInMillis

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = null,
        initialDisplayedMonthMillis = initialDisplayDate, // This sets the initial view
        yearRange = IntRange(1900, currentDate.get(Calendar.YEAR) - minAge),
        initialDisplayMode = DisplayMode.Picker,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis <= minBirthDate
            }
            override fun isSelectableYear(year: Int): Boolean {
                return year <= (currentDate.get(Calendar.YEAR) - minAge)
            }
        }
    )

    // Additional validation when OK is clicked
//    fun validateAndSubmit() {
//        datePickerState.selectedDateMillis?.let {
//            if (maxDate != null && it > maxDate) {
//                // This shouldn't happen because of selectableDates, but just in case
//                return
//            }
//            onDateSelected(it)
//        } ?: run {
//            onDateSelected(null)
//        }
//        onDismiss()
//    }
    fun validateAndSubmit() {
        datePickerState.selectedDateMillis?.let {
            if (it > minBirthDate) return
            onDateSelected(it)
        } ?: run {
            onDateSelected(null)
        }
        onDismiss()
    }

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = { validateAndSubmit() },
                enabled = datePickerState.selectedDateMillis?.let {
                    maxDate?.let { max -> it <= max } ?: true
                } ?: true
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            showModeToggle = false
        )
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
            onDismiss = { showDatePicker = false },
            maxDate = Calendar.getInstance().timeInMillis // Close the picker if dismissed
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