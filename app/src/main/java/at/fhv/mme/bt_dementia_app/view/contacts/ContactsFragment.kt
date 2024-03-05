package at.fhv.mme.bt_dementia_app.view.contacts

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import at.fhv.mme.bt_dementia_app.R
import at.fhv.mme.bt_dementia_app.databinding.FragmentContactsBinding
import at.fhv.mme.bt_dementia_app.model.Contact
import at.fhv.mme.bt_dementia_app.utils.DialogUtils
import at.fhv.mme.bt_dementia_app.view.adapter.ContactListAdapter
import at.fhv.mme.bt_dementia_app.viewmodel.ContactViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactsFragment : Fragment() {

    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!

    private lateinit var contactListAdapter: ContactListAdapter

    private val viewModel: ContactViewModel by viewModels()

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactsBinding.inflate(inflater, container, false)

        // initialize permission request launcher
        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (!isGranted) {
                    // Show a message that permission is necessary
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.toast_call_contact_permission_required),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        // observe the contact list from the ViewModel
        viewModel.contacts.asLiveData().observe(viewLifecycleOwner) { contacts ->
            // update contact list in adapter
            contactListAdapter.submitList(contacts)
        }
    }

    private fun initViews() {
        // set page title
        binding.header.tvTitle.setText(R.string.title_contacts)

        // initialize adapter & RecyclerView
        contactListAdapter = ContactListAdapter(
            onDelete = { contact: Contact ->
                DialogUtils.showConfirmationDialog(
                    requireContext(),
                    getString(R.string.label_confirmation_delete_title, "Contact"),
                    getString(R.string.label_confirmation_delete_text, "contact")
                ) { viewModel.deleteContact(contact) }
            },
            onCall = { contact: Contact ->
                // check permission before calling
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.CALL_PHONE
                    )
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    // request for permission
                    requestPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
                } else {
                    callContact(requireContext(), contact.phoneNumber)
                }
            }
        )

        binding.rvContactList.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = contactListAdapter
        }

        // initialize add contact button
        binding.btnAddContact.setOnClickListener {
            findNavController().navigate(R.id.action_contactsFragment_to_addContactFragment)
        }
    }

    private fun callContact(context: Context, phoneNumber: String) {
        val intent = Intent(Intent.ACTION_CALL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.toast_call_contact_failure),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}