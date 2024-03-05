package at.fhv.mme.bt_dementia_app.view.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import at.fhv.mme.bt_dementia_app.R
import at.fhv.mme.bt_dementia_app.databinding.ItemContactBinding
import at.fhv.mme.bt_dementia_app.model.Contact

class ContactListAdapter(
    private val onDelete: (Contact) -> Unit,
    private val onCall: (Contact) -> Unit
) : ListAdapter<Contact, ContactListAdapter.ViewHolder>(ContactDiffCallback()) {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemContactBinding.bind(itemView)

        fun databind(contact: Contact, onDelete: (Contact) -> Unit, onCall: (Contact) -> Unit) {
            binding.tvContactName.text = contact.name
            binding.tvContactRelation.text = contact.relation

            val context = binding.root.context
            try {
                context.openFileInput(contact.profileImagePath).use { stream ->
                    val bitmap = BitmapFactory.decodeStream(stream)
                    binding.ivProfileImage.setImageBitmap(bitmap)
                }
            } catch (e: Exception) {
                e.printStackTrace()

                // set default profile image
                binding.ivProfileImage.setImageResource(R.drawable.default_profile_image)
            }

            binding.ibtnDeleteContact.setOnClickListener {
                onDelete(contact)
            }

            binding.btnCallContact.setOnClickListener {
                onCall(contact)
            }
        }

        fun unbind() {
            // set onClickListeners null to prevent memory leaks
            binding.ibtnDeleteContact.setOnClickListener(null)
            binding.btnCallContact.setOnClickListener(null)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.databind(getItem(position), onDelete, onCall)
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        holder.unbind()
    }

    class ContactDiffCallback : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem == newItem
        }
    }
}