package com.example.zv.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseUser

@Composable
fun HomeScreen(
    user: FirebaseUser?,
    onTaskListClick: () -> Unit,
    onDeviceInfoClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onSignOut: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "TaskFlow",
            style = MaterialTheme.typography.headlineLarge,
            fontSize = 32.sp,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        
        if (user != null) {
            Text(
                text = if (user.isAnonymous) "Вы зашли как гость" else "Привет, ${user.email}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }
        
        Button(
            onClick = onTaskListClick,
            modifier = Modifier.fillMaxWidth().height(56.dp).padding(bottom = 16.dp)
        ) {
            Text("Мои задачи")
        }
        
        OutlinedButton(
            onClick = onDeviceInfoClick,
            modifier = Modifier.fillMaxWidth().height(56.dp).padding(bottom = 16.dp)
        ) {
            Text("О приложении")
        }
        
        OutlinedButton(
            onClick = onSettingsClick,
            modifier = Modifier.fillMaxWidth().height(56.dp).padding(bottom = 16.dp)
        ) {
            Text("Настройки профиля")
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        OutlinedButton(
            onClick = onSignOut,
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("Выйти")
        }
    }
}
