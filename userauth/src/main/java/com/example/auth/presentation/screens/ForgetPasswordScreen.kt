package com.example.auth.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.auth.presentation.components.LoadingScreen
import com.example.auth.presentation.components.MyTextField
import com.example.auth.presentation.responsive.WindowType
import com.example.auth.presentation.viewmodel.LoginViewModel

@Composable
fun ForgetPasswordScreen(windowType: WindowType, viewModel: LoginViewModel) {

    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val loginFormState by viewModel.loginState.collectAsState()

    LaunchedEffect(uiState) {
        if (uiState.isEmailVerified) {
            Toast.makeText(context, "Email verified", Toast.LENGTH_SHORT).show()

        }
    }
    ForgetPasswordScreenUI(windowType
            ,isLoading = uiState.isLoading,
        isEmailverified = uiState.isEmailVerified,
        onEmailUpdate = { email -> viewModel.setEmail(email) },
        onPasswordUpdate = { password -> viewModel.setPassword(password) },
        onConfirmPasswordUpdate = {},
        onSignupPressed = { viewModel.onSignupPressed() },
        onVerifyEmailPressed = { viewModel.verifyEmail() },
        onChangePasswordPressed = { viewModel.forgetPasswordRequest() })
}

@Composable
fun ForgetPasswordScreenUI(
    windowType: WindowType,
    isLoading: Boolean,
    isEmailverified: Boolean,
    onEmailUpdate: (String) -> Unit,
    onPasswordUpdate: (String) -> Unit,
    onConfirmPasswordUpdate: (String) -> Unit,
    onSignupPressed: () -> Unit,
    onVerifyEmailPressed: () -> Unit,
    onChangePasswordPressed: () -> Unit
) {

    var containerSize by remember { mutableStateOf(IntSize.Zero) }


    if (isLoading) LoadingScreen("Logging in...")
    Box(modifier = Modifier
        .fillMaxSize() // Fills the entire screen
        .onSizeChanged { size ->
            containerSize = size
        }
        .background(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFF2567E8), Color(0xFF1CE6DA)),
                startY = 0f,
                endY = containerSize.height.toFloat()
            )
        )) {
        // Responsive width modifier
        val maxWidth = when (windowType) {
            WindowType.Compact -> Modifier.fillMaxWidth()
            WindowType.Medium -> Modifier.width(400.dp)
            WindowType.Expanded -> Modifier.width(500.dp)
        }

        Box(
            modifier = Modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .align(Alignment.Center)
                .then(maxWidth)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Forget Password", fontSize = 32.sp
                    )
                    Row {
                        Text(text = "Don't have an account?")
                        Spacer(modifier = Modifier.padding(6.dp))
                        Text(
                            text = "Sign Up",
                            color = Color.Blue,
                            modifier = Modifier.clickable { onSignupPressed() })
                    }
                }
                if (isEmailverified) {

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(text = "Email is verified. ")
                        Text(text = "Reset password email has been sent successfully.")
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        MyTextField("Email", "Email", "", onValueChanged = { onEmailUpdate(it) }, true, "")
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                        Button(modifier = Modifier.fillMaxWidth(),
                            onClick = { onVerifyEmailPressed() }) {
                            Text(text = "Verify")
                        }

                    }
                }
            }

        }
    }
}

@Preview
@Composable
fun ForgetPasswordScreenPreview() {
    ForgetPasswordScreenUI(windowType = WindowType.Medium,false,
        false,
        onEmailUpdate = {},
        onPasswordUpdate = {},
        onConfirmPasswordUpdate = {},
        onSignupPressed = {},
        onVerifyEmailPressed = {},
        onChangePasswordPressed = {})
}



