package com.example.authmodule.auth.data.model

import androidx.room.PrimaryKey

data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userName: String,
    val email: String,
    val password: String
)
