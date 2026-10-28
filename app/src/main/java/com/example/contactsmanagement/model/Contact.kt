package com.example.contactsmanagement.model

import java.io.Serializable

data class Contact(
    val name: String,
    val phone: String,
    val group: String,
    val gender: String,
    val isFavorite: Boolean = false,
    val note: String = ""
) : Serializable {
    val initial: String
        get() = name.trim().substringAfterLast(' ').take(1).uppercase()
}
