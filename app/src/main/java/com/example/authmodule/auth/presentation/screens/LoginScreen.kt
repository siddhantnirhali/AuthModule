package com.example.authmodule.auth.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.authmodule.R
import com.example.authmodule.auth.presentation.components.LoadingScreen
import com.example.authmodule.auth.presentation.components.MyPasswordTextField
import com.example.authmodule.auth.presentation.components.MyTextField
import com.example.authmodule.auth.presentation.viewmodel.AuthStatus
import com.example.authmodule.auth.presentation.viewmodel.LoginViewModel

@Composable
fun LoginScreen(viewModel: LoginViewModel) {

    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val loginFormState by viewModel.loginState.collectAsState()

    LaunchedEffect(uiState.authStatus) {
        if (uiState.authStatus == AuthStatus.SUCCESS) {
            Toast.makeText(context, "User Logged in Successfully", Toast.LENGTH_SHORT).show()

        }
    }
    LoginScreenUI(isLoading = uiState.isLoading,
        onLoginPressed = { viewModel.login() },
        onEmailUpdate = { email -> viewModel.setEmail(email) },
        onPasswordUpdate = { password -> viewModel.setPassword(password) },
        onSignupPressed = { viewModel.onSignupPressed() },
        onForgetPasswordPressed = {viewModel.forgetPasswordRequest()})
}

@Composable
fun LoginScreenUI(
    isLoading: Boolean,
    onLoginPressed: () -> Unit,
    onEmailUpdate: (String) -> Unit,
    onPasswordUpdate: (String) -> Unit,
    onSignupPressed: () -> Unit,
    onForgetPasswordPressed: () -> Unit
) {
    val context = LocalContext.current // Get context
    var isChecked by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }
    val emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$".toRegex()
    var isPasswordVisible by remember { mutableStateOf(false) }
    var userLoggedIn by remember { mutableStateOf(false) }
    var containerSize by remember { mutableStateOf(IntSize.Zero) }

    val coroutineScope = rememberCoroutineScope()

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
        Box(
            modifier = Modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .align(Alignment.Center)
                .fillMaxWidth(),

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
                        text = "Login", fontSize = 32.sp
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
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    MyTextField("Email", "Email", "", onValueChanged = { onEmailUpdate(it) })
                    MyPasswordTextField(
                        "Password",
                        "Password",
                        "",
                        onValueChanged = { onPasswordUpdate(it) })
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Checkbox(checked = false, onCheckedChange = {})
                            Text(text = "Remember me")
                        }
                        //Spacer(modifier = Modifier.padding(24.dp))
                        Text(text = "Forget Password?",
                            color = Color.Blue,
                            modifier = Modifier.clickable { onForgetPasswordPressed()})
                    }
                }
                Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                    Button(modifier = Modifier.fillMaxWidth(),
                        onClick = { onLoginPressed() }) {
                        Text(text = "Log In")
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(), // Adjust padding as needed
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Left line
                        Divider(
                            modifier = Modifier.weight(1f),
                            color = Color.Gray.copy(alpha = 0.3f),
                            thickness = 1.dp
                        )

                        // "Or" text
                        Text(
                            text = "Or",
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = Color.Gray
                        )

                        // Right line
                        Divider(
                            modifier = Modifier.weight(1f),
                            color = Color.Gray.copy(alpha = 0.3f),
                            thickness = 1.dp
                        )
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(15.dp)) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            shadowElevation = 1.dp,
                            color = Color.White,
                            onClick = {}
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(vertical = 10.dp, horizontal = 24.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                // Google Icon
                                Image(
                                    painter = painterResource(id = R.drawable.google_logo), // Replace with your Google logo drawable
                                    contentDescription = "Google Logo",
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp)) // Space between icon and text

                                // Text
                                Text(
                                    text = "Continue with Google",
                                    color = Color.Black,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            shadowElevation = 1.dp,
                            color = Color.White,
                            onClick = {}
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(vertical = 10.dp, horizontal = 24.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                // Google Icon
                                Image(
                                    painter = painterResource(id = R.drawable.facebook_logo), // Replace with your Google logo drawable
                                    contentDescription = "Google Logo",
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp)) // Space between icon and text

                                // Text
                                Text(
                                    text = "Continue with Facebook",
                                    color = Color.Black,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }

            }

        }
    }
}


@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreenUI(false,
        onLoginPressed = {},
        onEmailUpdate = {},
        onPasswordUpdate = {},
        onSignupPressed = {},
        onForgetPasswordPressed = {})
}



