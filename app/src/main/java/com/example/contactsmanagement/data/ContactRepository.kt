package com.example.contactsmanagement.data

import com.example.contactsmanagement.model.Contact

object ContactRepository {
    private val _contacts = arrayListOf(
        Contact("Nguyễn Văn An", "0901234567", "Bạn bè", "Nam"),
        Contact("Trần Thị Bình", "0912345678", "Gia đình", "Nữ", isFavorite = true),
        Contact("Lê Minh Châu", "0987654321", "Công việc", "Nam")
    )
    val contacts: List<Contact>
        get() = _contacts

    val favoriteCount: Int
        get() = _contacts.count { it.isFavorite }

    fun findByPhone(phone: String): Contact? = _contacts.find { it.phone == phone }

    fun isPhoneTaken(phone: String): Boolean = _contacts.any { it.phone == phone }

    fun add(contact: Contact) {
        _contacts.add(contact)
    }

    fun setFavorite(phone: String, isFavorite: Boolean) {
        val index = _contacts.indexOfFirst { it.phone == phone }
        if (index != -1) {
            _contacts[index] = _contacts[index].copy(isFavorite = isFavorite)
        }
    }

    fun delete(phone: String) {
        _contacts.removeAll { it.phone == phone }
    }
}
