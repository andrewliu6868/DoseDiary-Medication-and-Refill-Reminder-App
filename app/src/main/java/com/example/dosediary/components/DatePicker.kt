package com.example.dosediary.components

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.tooling.preview.Preview
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun DatePicker(date: MutableState<Date>, placeholder: String) {
    //Use remember: Ensures that these objects are only created once and survive recompositions
    val dateFormat = remember { SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()) }
    val calendar = Calendar.getInstance()

    val context = LocalContext.current

    val datePickerDialog = remember {
        //Create a Date Picker pointing to the date provided, listen on change
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                date.value = calendar.time
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }


    OutlinedTextField(
        value = dateFormat.format(date.value),
        onValueChange = { },
        label = { Text(placeholder) },
        readOnly = true,
        modifier = Modifier.fillMaxWidth(),
        interactionSource = remember { MutableInteractionSource() }
            .also { interactionSource ->
                LaunchedEffect(interactionSource) {
                    interactionSource.interactions.collect {
                        if (it is PressInteraction.Release) {
                            datePickerDialog.show()
                        }
                    }
                }
            }
    )
}

@Preview(showBackground = true)
@Composable
fun DatePickerPreview() {
    val currentDate = remember { mutableStateOf(Date()) }
    DatePicker(date = currentDate, "Select Date")
}