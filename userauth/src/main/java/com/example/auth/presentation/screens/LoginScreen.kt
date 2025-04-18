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
import com.example.auth.presentation.components.MyPasswordTextField
import com.example.auth.presentation.components.MyTextField
import com.example.auth.presentation.responsive.WindowType
import com.example.auth.presentation.viewmodel.AuthStatus
import com.example.auth.presentation.viewmodel.LoginFormState
import com.example.auth.presentation.viewmodel.LoginViewModel

@Composable
fun LoginScreen(windowType: WindowType, viewModel: LoginViewModel) {

    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val loginFormState by viewModel.loginState.collectAsState()

    // Toast messages for auth status
    LaunchedEffect(uiState.authStatus) {
        when (uiState.authStatus) {
            AuthStatus.SUCCESS -> {
                Toast.makeText(context, "User Login Successful", Toast.LENGTH_SHORT).show()
                // Do any navigation or success logic
                viewModel.onLoginSuccess()
                viewModel.clearAuthStatus()
            }

            AuthStatus.ERROR -> {
                Toast.makeText(
                    context,
                    uiState.errorMessage ?: "User Login Failed",
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.clearAuthStatus()
            }

            else -> { /* No toast for IDLE or other states */
            }
        }


    }
    LoginScreenUI(windowType, loginFormState, isLoading = uiState.isLoading,
        onLoginPressed = { viewModel.login() },
        onEmailUpdate = { email -> viewModel.setEmail(email) },
        onPasswordUpdate = { password -> viewModel.setPassword(password) },
        onSignupPressed = { viewModel.onSignupPressed() },
        onForgetPasswordPressed = { viewModel.forgetPasswordRequest() })
}

@Composable
fun LoginScreenUI(
    windowType: WindowType,
    loginFormState: LoginFormState,
    isLoading: Boolean,
    onLoginPressed: () -> Unit,
    onEmailUpdate: (String) -> Unit,
    onPasswordUpdate: (String) -> Unit,
    onSignupPressed: () -> Unit,
    onForgetPasswordPressed: () -> Unit
) {
    var containerSize by remember { mutableStateOf(IntSize.Zero) }

    if (isLoading) LoadingScreen("Logging in...")

    Box(modifier = Modifier
        .fillMaxSize()
        .onSizeChanged { size -> containerSize = size }
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
                // Header
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(text = "Login", fontSize = 32.sp)
                    Row {
                        Text(text = "Don't have an account?")
                        Spacer(modifier = Modifier.padding(6.dp))
                        Text(
                            text = "Sign Up",
                            color = Color.Blue,
                            modifier = Modifier.clickable { onSignupPressed() }
                        )
                    }
                }

                // Fields
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    MyTextField(
                        "Email",
                        "Email",
                        value = loginFormState.email,
                        onValueChanged = onEmailUpdate,
                        isValid = loginFormState.isValidEmail,
                        loginFormState.emailError
                    )
                    MyPasswordTextField(
                        "Password",
                        "Password",
                        value = loginFormState.password,
                        onValueChanged = onPasswordUpdate,
                        isValid = loginFormState.isValidPassword,
                        loginFormState.passwordError
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Forget Password?",
                            color = Color.Blue,
                            modifier = Modifier.clickable { onForgetPasswordPressed() }
                        )
                    }
                }

                // Login Button and Divider
                Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onLoginPressed
                    ) {
                        Text(text = "Log In")
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
//                        Divider(
//                            modifier = Modifier.weight(1f),
//                            color = Color.Gray.copy(alpha = 0.3f),
//                            thickness = 1.dp
//                        )
//                        Text(
//                            text = "Or",
//                            modifier = Modifier.padding(horizontal = 16.dp),
//                            color = Color.Gray
//                        )
//                        Divider(
//                            modifier = Modifier.weight(1f),
//                            color = Color.Gray.copy(alpha = 0.3f),
//                            thickness = 1.dp
//                        )
                    }

//                    // Google Button
//                    Surface(
//                        modifier = Modifier.fillMaxWidth(),
//                        shape = RoundedCornerShape(8.dp),
//                        shadowElevation = 1.dp,
//                        color = Color.White,
//                        onClick = {} // Handle Google sign-in here
//                    ) {
//                        Row(
//                            modifier = Modifier
//                                .padding(vertical = 10.dp, horizontal = 24.dp),
//                            verticalAlignment = Alignment.CenterVertically,
//                            horizontalArrangement = Arrangement.Center
//                        ) {
//                            Image(
//                                painter = painterResource(id = R.drawable.google_logo),
//                                contentDescription = "Google Logo",
//                                modifier = Modifier.size(24.dp)
//                            )
//                            Spacer(modifier = Modifier.width(12.dp))
//                            Text(
//                                text = "Continue with Google",
//                                color = Color.Black,
//                                style = MaterialTheme.typography.bodySmall
//                            )
//                        }
//                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreenUI(windowType = WindowType.Medium,
        LoginFormState(), false,
        onLoginPressed = {},
        onEmailUpdate = {},
        onPasswordUpdate = {},
        onSignupPressed = {},
        onForgetPasswordPressed = {},
    )
}



