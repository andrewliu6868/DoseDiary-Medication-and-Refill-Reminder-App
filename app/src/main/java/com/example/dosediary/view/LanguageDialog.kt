package com.example.dosediary.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.dosediary.R

@Composable
fun LanguageDialog(
    selectedLanguage: String,
    onLanguageSelected: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    val languages = mapOf(
        "en" to stringResource(R.string.language_english),
        "zh" to stringResource(R.string.language_chinese),
        "de" to stringResource(R.string.language_german),
        "es" to stringResource(R.string.language_spanish),
        "fr" to stringResource(R.string.language_french),
        "hi" to stringResource(R.string.language_hindi),
        "it" to stringResource(R.string.language_italian),
        "ja" to stringResource(R.string.language_japanese),
        "ko" to stringResource(R.string.language_korean),
        "ru" to stringResource(R.string.language_russian)
    )

    var tempSelectedLanguage by remember { mutableStateOf(selectedLanguage) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = stringResource(R.string.select_language))
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                languages.forEach { (code, language) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                tempSelectedLanguage = code
                            }
                            .background(
                                if (tempSelectedLanguage == code) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                                else Color.Transparent
                            )
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = language)
                        if (tempSelectedLanguage == code) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onLanguageSelected(tempSelectedLanguage)
                onDismissRequest()
            }) {
                Text(stringResource(R.string.ok))
            }
        }
    )
}
