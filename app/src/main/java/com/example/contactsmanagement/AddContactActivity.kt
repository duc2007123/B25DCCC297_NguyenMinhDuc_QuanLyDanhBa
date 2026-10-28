package com.example.contactsmanagement

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.contactsmanagement.data.ContactRepository
import com.example.contactsmanagement.databinding.ActivityAddContactBinding
import com.example.contactsmanagement.model.Contact

class AddContactActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddContactBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddContactBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.contact_groups,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.groupSpinner.adapter = adapter
        }

        binding.saveButton.setOnClickListener { saveContact() }
    }

    private fun saveContact() {
        val name = binding.nameInput.text.toString().trim()
        val phone = binding.phoneInput.text.toString().trim()

        binding.nameLayout.error = null
        binding.phoneLayout.error = null

        when {
            name.isEmpty() -> binding.nameLayout.error = getString(R.string.error_name_empty)
            !phone.matches(PHONE_PATTERN) -> binding.phoneLayout.error = getString(R.string.error_phone_invalid)
            ContactRepository.isPhoneTaken(phone) -> binding.phoneLayout.error = getString(R.string.error_phone_taken)
            else -> {
                val contact = Contact(
                    name = name,
                    phone = phone,
                    group = binding.groupSpinner.selectedItem.toString(),
                    gender = selectedGender(),
                    isFavorite = binding.favoriteCheckbox.isChecked,
                    note = binding.noteInput.text.toString().trim()
                )
                setResult(RESULT_OK, Intent().putExtra(EXTRA_CONTACT, contact))
                finish()
            }
        }
    }

    private fun selectedGender(): String = when (binding.genderGroup.checkedRadioButtonId) {
        R.id.female_radio -> getString(R.string.female)
        R.id.other_gender_radio -> getString(R.string.other_gender)
        else -> getString(R.string.male)
    }

    companion object {
        const val EXTRA_CONTACT = "extra_contact"
        private val PHONE_PATTERN = Regex("0\\d{9}")
    }
}
