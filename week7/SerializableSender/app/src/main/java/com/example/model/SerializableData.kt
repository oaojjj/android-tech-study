package com.example.model

import java.io.Serializable

data class SerializableData(var item: String) : Serializable {
    companion object {
        const val serialVersionUID = 123L
    }

    override fun toString(): String {
        return "아이템 $item"
    }
}