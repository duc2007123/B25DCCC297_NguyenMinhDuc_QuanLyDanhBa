package com.example.contactsmanagement.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.contactsmanagement.databinding.ItemContactBinding
import com.example.contactsmanagement.model.Contact

class ContactAdapter(
    private val onItemClicked: (Contact) -> Unit
) : ListAdapter<Contact, ContactAdapter.ContactViewHolder>(DiffCallback) {

    class ContactViewHolder(
        private val binding: ItemContactBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(contact: Contact) {
            binding.apply {
                contactAvatar.text = contact.initial
                contactName.text = contact.name
                contactPhone.text = contact.phone
                contactGroup.text = contact.group
                favoriteStar.isVisible = contact.isFavorite
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = getItem(position)
        holder.itemView.setOnClickListener { onItemClicked(contact) }
        holder.bind(contact)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem.phone == newItem.phone
        }

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem == newItem
        }
    }
}
