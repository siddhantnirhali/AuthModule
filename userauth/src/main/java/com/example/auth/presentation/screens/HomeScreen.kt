package com.example.auth.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.auth.data.model.User
import com.example.auth.presentation.responsive.WindowType
import com.example.auth.presentation.viewmodel.HomeScreenViewModel

@Composable
fun HomeScreen(windowType: WindowType, viewModel: HomeScreenViewModel) {


    val user by viewModel.user.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onScreenEntered()
    }
    HomeScreenUI(user, onSignOut = { viewModel.signOut() })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenUI(
    user: User, onSignOut: () -> Unit
) {
    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Welcome, ${user.userName}", color = Color.White) },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.error
            ))
    }, content = { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "You are logged in!",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Button(
                onClick = onSignOut, colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ), shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.ExitToApp, contentDescription = "Sign Out")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Sign Out")
            }
        }
    })
}

@Preview
@Composable
fun HomeScreenUIPreview() {
    HomeScreenUI(
        User(
        userName = "User", email = "User@email.com"
    ), onSignOut = {})
}
