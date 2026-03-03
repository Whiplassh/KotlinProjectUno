package com.example.projectuno.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp


val Shapes = Shapes(
    small = RoundedCornerShape(60.dp),
    medium = RoundedCornerShape(
        topStart = 10.dp,
        topEnd = 40.dp,
        bottomStart = 40.dp,
        bottomEnd = 12.dp
    )
)