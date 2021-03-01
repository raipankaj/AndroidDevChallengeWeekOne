package com.example.androiddevchallenge

import androidx.compose.ui.graphics.Color

fun getRandomColor(): Color {
    return Color((0..255).random(), (0..255).random(), (0..255).random())
}