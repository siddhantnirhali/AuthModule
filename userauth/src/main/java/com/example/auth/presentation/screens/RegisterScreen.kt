package com.example.auth.presentation.screens

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.auth.presentation.components.LoadingScreen
import com.example.auth.presentation.components.MyPasswordTextField
import com.example.auth.presentation.components.MyTextField
import com.example.auth.presentation.responsive.WindowType
import com.example.auth.presentation.viewmodel.AuthStatus
import com.example.auth.presentation.viewmodel.RegistrationFormState
import com.example.auth.presentation.viewmodel.RegistrationScreenViewModel
import com.example.auth.R


@Composable
fun RegisterScreen(
    windowType: WindowType,
    viewModel: RegistrationScreenViewModel
) {
    val context = LocalContext.current // Get context
    val registrationState = viewModel.registrationState.collectAsState()
    val uiState by viewModel.uiState.collectAsState()


    // For handling Google Sign-In result
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // Pass the intent data to the ViewModel
            viewModel.handleGoogleSignInResult(result.data)
        } else {
            // User cancelled the sign-in flow
            Toast.makeText(
                context,
                "Google sign-in cancelled",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.checkUserLoggedIn()
        if (viewModel.isLoggedIn.value) {
            viewModel.onLoginSuccess()
        }
    }

    // Toast messages for auth status
    LaunchedEffect(uiState.authStatus) {
        when (uiState.authStatus) {
            AuthStatus.SUCCESS -> {
                Toast.makeText(context, "User Registration Successful", Toast.LENGTH_SHORT).show()
                // Do any navigation or success logic
                viewModel.onLoginSuccess()
                viewModel.clearAuthStatus()
            }

            AuthStatus.ERROR -> {
                Toast.makeText(
                    context,
                    uiState.errorMessage ?: "User Registration Failed",
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.clearAuthStatus()
            }

            else -> { /* No toast for IDLE or other states */
            }
        }
    }


    UserRegistrationUI(context,
        isLoading = uiState.isLoading,
        registrationState.value,
        onEmailUpdate = { viewModel.setEmail(it) },
        onUsernameUpdate = { viewModel.setUsername(it) },
        onPasswordUpdate = { viewModel.setPassword(it) },
        onConfirmPasswordUpdate = { viewModel.setConfirmPassword(it) },
        onRegisterPressed = { viewModel.register() },
        onLoginPressed = { viewModel.onLoginPressed() },
        onGoogleSignInPressed = { viewModel.signUpWithGoogle().let { launcher.launch(it) } },
        windowType)
}


@Composable
fun UserRegistrationUI(
    context: Context,
    isLoading: Boolean,
    registrationState: RegistrationFormState,
    onEmailUpdate: (String) -> Unit,
    onUsernameUpdate: (String) -> Unit,
    onPasswordUpdate: (String) -> Unit,
    onConfirmPasswordUpdate: (String) -> Unit,
    onRegisterPressed: () -> Unit,
    onLoginPressed: () -> Unit,
    onGoogleSignInPressed: () -> Unit,
    windowType: WindowType
) {
    if (isLoading) {
        LoadingScreen("Signing up...")
    }

    var containerSize by remember { mutableStateOf(IntSize.Zero) }

    // Adjust padding based on window size
    val horizontalPadding = when (windowType) {
        WindowType.Compact -> 16.dp
        WindowType.Medium -> 48.dp
        WindowType.Expanded -> 96.dp
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged { containerSize = it }
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF2567E8), Color(0xFF1CE6DA)),
                    startY = 0f,
                    endY = containerSize.height.toFloat()
                )
            )
           // .padding(horizontal = horizontalPadding)
    ) {
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
                        text = "Register", fontSize = 32.sp
                    )
                    Row {
                        Text(text = "Already have an account?")
                        Spacer(modifier = Modifier.padding(6.dp))
                        Text(text = "Login",
                            color = Color.Blue,
                            modifier = Modifier.clickable { onLoginPressed() })
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    MyTextField(
                        "Username",
                        "Username",
                        registrationState.username,
                        onValueChanged = onUsernameUpdate,
                        registrationState.isValidUsername,
                        registrationState.usernameError
                    )
                    MyTextField(
                        "Email", "Email", registrationState.email, onValueChanged = {
                            onEmailUpdate(it)
                        },
                        registrationState.isValidEmail,
                        registrationState.emailError
                    )
                    MyPasswordTextField(
                        "Password",
                        "Password",
                        registrationState.password,
                        onValueChanged = onPasswordUpdate,
                        registrationState.isValidPassword,
                        registrationState.passwordError
                    )
                    MyPasswordTextField(
                        "Confirm Password",
                        "Password",
                        registrationState.confirmPassword,
                        onValueChanged = onConfirmPasswordUpdate,
                        registrationState.isValidConfirmPassword,
                        registrationState.confirmPasswordError
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onRegisterPressed
                    ) {
                        Text(text = "Register")
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Divider(
                            modifier = Modifier.weight(1f),
                            color = Color.Gray.copy(alpha = 0.3f),
                            thickness = 1.dp
                        )
                        Text(
                            text = "Or",
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = Color.Gray
                        )
                        Divider(
                            modifier = Modifier.weight(1f),
                            color = Color.Gray.copy(alpha = 0.3f),
                            thickness = 1.dp
                        )
                    }

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        shadowElevation = 1.dp,
                        color = Color.White,
                        onClick = onGoogleSignInPressed
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 24.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.google_logo),
                                contentDescription = "Google Logo",
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Continue with Google",
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



@Preview
@Composable
fun RegisterScreenPreview() {
    val context = LocalContext.current // Get context
    UserRegistrationUI(
        context,
        false,
        RegistrationFormState(),
        onRegisterPressed = {},
        onEmailUpdate = {},
        onUsernameUpdate = {},
        onPasswordUpdate = {},
        onConfirmPasswordUpdate = {},
        onLoginPressed = {},
        onGoogleSignInPressed = {},
        windowType = WindowType.Medium
    )
}



