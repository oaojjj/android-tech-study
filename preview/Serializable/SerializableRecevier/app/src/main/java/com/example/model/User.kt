package com.example.model

import java.io.Serializable

data class User(var nickName: String, var age: Int, var introduce: String) : Serializable {
    companion object {
        const val serialVersionUID = 123L
    }

    override fun toString(): String {
        return "닉네임: $nickName, 나이: $age, 소개: $introduce"
    }
}