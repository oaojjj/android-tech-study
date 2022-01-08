package com.example.recyclerviewexample.item

import android.graphics.Color
import kotlin.random.Random

data class ShapeItem(
    var color: Int = Color.argb(
        255,
        Random.nextInt(256),
        Random.nextInt(256),
        Random.nextInt(256)
    ),
    var type: Type = when (Random.nextInt(Type.values().size)) {
        0 -> Type.CIRCLE
        1 -> Type.TRIANGLE
        2 -> Type.SQUARE
        3 -> Type.STAR
        else -> Type.CIRCLE
    },
    var count: Int
) {
    enum class Type {
        CIRCLE, SQUARE, TRIANGLE, STAR;
    }
}
