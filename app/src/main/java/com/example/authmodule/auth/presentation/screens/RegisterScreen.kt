package com.example.authmodule.auth.presentation.screens

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.authmodule.R
import com.example.authmodule.auth.presentation.components.LoadingScreen
import com.example.authmodule.auth.presentation.components.MyPasswordTextField
import com.example.authmodule.auth.presentation.components.MyTextField
import com.example.authmodule.auth.presentation.viewmodel.AuthStatus
import com.example.authmodule.auth.presentation.viewmodel.RegistrationFormState
import com.example.authmodule.auth.presentation.viewmodel.RegistrationScreenViewModel



@Composable
fun RegisterScreen(
    viewModel: RegistrationScreenViewModel
) {
    val context = LocalContext.current // Get context
    val registrationState = viewModel.registrationState.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    Log.d("ComposeCheck", "Recomposition isLoading: ${uiState.isLoading}")

    LaunchedEffect(uiState.authStatus) {
        if(uiState.authStatus == AuthStatus.SUCCESS){
            Toast.makeText(context, "User Register Successfully", Toast.LENGTH_SHORT).show()
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
        onLoginPressed = { viewModel.onLoginPressed() })


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
    onLoginPressed: () -> Unit
) {

    if (isLoading) {
        LoadingScreen("Signing up...")
    }

    var email by remember { mutableStateOf(registrationState.email) }
    var username by remember { mutableStateOf(registrationState.username) }
    var password by remember { mutableStateOf(registrationState.password) }
    var confirmPassword by remember { mutableStateOf(registrationState.confirmPassword) }

    var isChecked by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }
    val emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$".toRegex()
    var isPasswordVisible by remember { mutableStateOf(false) }
    var userLoggedIn by remember { mutableStateOf(false) }
    var containerSize by remember { mutableStateOf(IntSize.Zero) }

    val coroutineScope = rememberCoroutineScope()

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
                    MyTextField("Username",
                        "Username",
                        username,
                        onValueChanged = { onUsernameUpdate(it) })
                    MyTextField("Email", "Email", email, onValueChanged = { email ->
                        onEmailUpdate(email)
                        Log.d("MyTextField", email)
                    })
                    MyPasswordTextField("Password",
                        "Password",
                        password,
                        onValueChanged = { onPasswordUpdate(it) })
                    MyPasswordTextField("Confirm Password",
                        "Password",
                        confirmPassword,
                        onValueChanged = { onConfirmPasswordUpdate(it) })

                }
                Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onRegisterPressed() }) {
                        Text(text = "Register")
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(), // Adjust padding as needed
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
                        Surface(modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            shadowElevation = 1.dp,
                            color = Color.White,
                            onClick = {}) {
                            Row(
                                modifier = Modifier.padding(
                                    vertical = 10.dp,
                                    horizontal = 24.dp
                                ),
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
                        Surface(modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            shadowElevation = 1.dp,
                            color = Color.White,
                            onClick = {}) {
                            Row(
                                modifier = Modifier.padding(
                                    vertical = 10.dp,
                                    horizontal = 24.dp
                                ),
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
fun RegisterScreenPreview() {
    val context = LocalContext.current // Get context
    UserRegistrationUI(context,
        false,
        RegistrationFormState(),
        onRegisterPressed = {},
        onEmailUpdate = {},
        onUsernameUpdate = {},
        onPasswordUpdate = {},
        onConfirmPasswordUpdate = {},
        onLoginPressed = {})
}



