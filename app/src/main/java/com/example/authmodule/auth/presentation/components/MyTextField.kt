package com.example.authmodule.auth.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults.colors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lint.kotlin.metadata.Visibility


@Composable
fun MyTextField(text: String, placeholder: String, value: String, onValueChanged : (String) -> Unit){
    var input by remember { mutableStateOf(value) }
    Text(text)
    Box(
        modifier = Modifier

    ){

        OutlinedTextField(modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = colors(
                focusedContainerColor = Color.White,
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color.Gray
            ),
            value = input,
            onValueChange = { input = it
                onValueChanged(input)
                },

            placeholder = { Text(text = placeholder) },
            singleLine = true)
    }
}

@Composable
fun MyPasswordTextField(text: String, placeholder: String, value: String, onValueChanged : (String) -> Unit){
    var input by remember { mutableStateOf(value) }
    var passwordVisible by remember { mutableStateOf(false) }
    Text(text)
    Box(
        modifier = Modifier

    ){

        OutlinedTextField(modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = colors(
                focusedContainerColor = Color.White,
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color.Gray
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
                    Icons.Default.Check
                else Icons.Default.Close

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = "Toggle Password Visibility")
                }
            })
    }
}