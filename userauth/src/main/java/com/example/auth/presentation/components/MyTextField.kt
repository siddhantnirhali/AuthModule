package com.example.auth.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.auth.R


@Composable
fun MyTextField(text: String, placeholder: String, value: String, onValueChanged : (String) -> Unit, isValid: Boolean, warning: String?){
    var input by remember { mutableStateOf(value) }
    // Only show error after user has typed something
    val showError =  !isValid
    Text(text)
    Box(
        modifier = Modifier

    ){

        OutlinedTextField(modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedBorderColor = if (!isValid) Color.Red else Color.LightGray,
                focusedBorderColor = if (!isValid) Color.Red else Color.LightGray
            ),
            value = input,
            onValueChange = { input = it
                onValueChanged(input)
                },

            placeholder = { Text(text = placeholder) },
            singleLine = true)

        // Show warning if needed
        if (showError) {
            Text(
                text = warning?: "",
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )
        }
    }
}

@Composable
fun MyPasswordTextField(text: String, placeholder: String, value: String, onValueChanged : (String) -> Unit, isValid: Boolean, warning: String?){
    var input by remember { mutableStateOf(value) }
    var passwordVisible by remember { mutableStateOf(false) }
    // Only show error after user has typed something
    val showError = !isValid
    Text(text)
    Box(
        modifier = Modifier

    ){

        OutlinedTextField(modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedBorderColor = if (!isValid) Color.Red else Color.LightGray,
                focusedBorderColor = if (!isValid) Color.Red else Color.LightGray
            ),
            value = input,
            onValueChange = { input = it
                onValueChanged(input)
            },

            placeholder = { Text(text = placeholder) },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),

            trailingIcon = {
                val image = if (passwordVisible)
                    R.drawable.showpassword
                else R.drawable.hidepassword

                IconButton(modifier = Modifier.size(24.dp), onClick = { passwordVisible = !passwordVisible }) {
                    Icon(painter = painterResource(image), contentDescription = "Toggle Password Visibility")
                }
            })

        // Show warning if needed
        if (showError) {
            Text(
                text = warning?: "",
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )
        }
    }
}