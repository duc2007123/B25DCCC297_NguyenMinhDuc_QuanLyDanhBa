package com.example.contactsmanagement

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.contactsmanagement.data.ContactRepository
import com.example.contactsmanagement.databinding.ActivityContactDetailBinding
import com.example.contactsmanagement.model.Contact

class ContactDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactDetailBinding
    private lateinit var contact: Contact

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityContactDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val phone = intent.getStringExtra(EXTRA_PHONE).orEmpty()
        val selectedContact = ContactRepository.findByPhone(phone)
        if (selectedContact == null) {
            finish()
            return
        }
        contact = selectedContact
        bindContact()

        binding.favoriteCheckbox.setOnCheckedChangeListener { _, isChecked ->
            ContactRepository.setFavorite(contact.phone, isChecked)
        }
        binding.callButton.setOnClickListener { dial() }
        binding.backButton.setOnClickListener { finish() }
        binding.deleteButton.setOnClickListener { deleteContact() }
    }

    private fun bindContact() {
        binding.apply {
            detailAvatar.text = contact.initial
            detailName.text = contact.name
            detailPhone.text = contact.phone
            detailGroupGender.text = getString(R.string.group_gender, contact.group, contact.gender)
            detailNote.text = contact.note.ifBlank { getString(R.string.no_note) }
            favoriteCheckbox.isChecked = contact.isFavorite
        }
    }

    private fun dial() {
        val intent = Intent(Intent.ACTION_DIAL, "tel:${contact.phone}".toUri())
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, R.string.no_dialer, Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteContact() {
        ContactRepository.delete(contact.phone)
        Toast.makeText(this, getString(R.string.contact_deleted, contact.name), Toast.LENGTH_SHORT).show()
        finish()
    }

    companion object {
        const val EXTRA_PHONE = "extra_phone"
    }
}
