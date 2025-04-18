package com.example.auth.presentation.responsive

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass

fun getWindowType(windowSizeClass: WindowWidthSizeClass): WindowType {
    return when (windowSizeClass) {
        WindowWidthSizeClass.Compact -> WindowType.Compact
        WindowWidthSizeClass.Medium -> WindowType.Medium
        WindowWidthSizeClass.Expanded -> WindowType.Expanded
        else -> WindowType.Compact // Default fallback
    }
}