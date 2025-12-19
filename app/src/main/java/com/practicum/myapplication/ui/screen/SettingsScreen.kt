package com.practicum.myapplication.ui.screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practicum.myapplication.R

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit = {}  // Добавьте параметр
) {
    val context = LocalContext.current
    val shareText = "Попробуй крутое приложение Playlist Maker!"
    val emailTo = stringResource(R.string.email_to)
    val emailSubject = stringResource(R.string.email_subject)
    val emailBody = stringResource(R.string.email_body)
    val termsUrl = stringResource(R.string.terms_url)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Хедер с кнопкой "Назад"
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(16.dp)
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Назад",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(id = R.string.settings_title),
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = stringResource(id = R.string.settings_subtitle),
            fontSize = 20.sp
        )

        Spacer(Modifier.height(16.dp))

        SettingSwitchItem(
            title = stringResource(id = R.string.settings_dark_theme)
        )

        Spacer(Modifier.height(16.dp))

        SettingClickItem(
            title = stringResource(id = R.string.settings_share),
            icon = Icons.Default.Share
        ) {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, shareText)
            }
            context.startActivity(Intent.createChooser(shareIntent, null))
        }

        Spacer(Modifier.height(16.dp))

        SettingClickItem(
            title = stringResource(id = R.string.settings_email),
            icon = Icons.Default.Email
        ) {
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:$emailTo")
                putExtra(Intent.EXTRA_SUBJECT, emailSubject)
                putExtra(Intent.EXTRA_TEXT, emailBody)
            }
            context.startActivity(emailIntent)
        }

        Spacer(Modifier.height(16.dp))

        SettingArrowItem(
            title = stringResource(id = R.string.settings_terms),
        ) {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(termsUrl))
            context.startActivity(browserIntent)
        }
    }
}