package com.example.contactsmanagement

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.example.contactsmanagement.data.ContactRepository
import com.example.contactsmanagement.databinding.ActivityMainBinding
import com.example.contactsmanagement.model.Contact
import com.example.contactsmanagement.ui.ContactAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val adapter = ContactAdapter { contact ->
        val intent = Intent(this, ContactDetailActivity::class.java)
        intent.putExtra(ContactDetailActivity.EXTRA_PHONE, contact.phone)
        startActivity(intent)
    }

    private val addContactLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val data = result.data
            if (result.resultCode != RESULT_OK || data == null) return@registerForActivityResult

            val contact = IntentCompat.getSerializableExtra(data, AddContactActivity.EXTRA_CONTACT, Contact::class.java)
            if (contact != null) {
                ContactRepository.add(contact)
                updateContacts(scrollToBottom = true)
                Toast.makeText(this, getString(R.string.contact_added, contact.name), Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.contactList.adapter = adapter
        binding.addButton.setOnClickListener {
            addContactLauncher.launch(Intent(this, AddContactActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        updateContacts()
    }

    private fun updateContacts(scrollToBottom: Boolean = false) {
        val contacts = ContactRepository.contacts.toList()
        adapter.submitList(contacts) {
            if (scrollToBottom) binding.contactList.scrollToPosition(contacts.lastIndex)
        }
        binding.statistics.text = getString(
            R.string.statistics,
            contacts.size,
            ContactRepository.favoriteCount
        )
        binding.emptyText.isVisible = contacts.isEmpty()
    }
}
