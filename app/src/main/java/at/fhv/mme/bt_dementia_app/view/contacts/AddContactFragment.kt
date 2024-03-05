package at.fhv.mme.bt_dementia_app.view.contacts

import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import at.fhv.mme.bt_dementia_app.R
import at.fhv.mme.bt_dementia_app.databinding.FragmentAddContactBinding
import at.fhv.mme.bt_dementia_app.model.Contact
import at.fhv.mme.bt_dementia_app.utils.DialogUtils
import at.fhv.mme.bt_dementia_app.viewmodel.ContactViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddContactFragment : Fragment() {

    private var _binding: FragmentAddContactBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ContactViewModel by viewModels()

    private var contactName: String = ""
    private var relation: String = ""
    private var phoneNumber: String = ""
    private var selectedImageUri: Uri? = null

    // activity result launcher to choose image
    private val pickImageResultLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                binding.tvProfileImageInfo.visibility = View.VISIBLE
                binding.ivProfileImage.setImageURI(uri)
                selectedImageUri = uri
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.toast_no_image_chosen),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddContactBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
        // set page title
        binding.header.tvTitle.setText(R.string.title_add_contact)

        // initialize back button
        binding.header.btnBack.setOnClickListener {
            DialogUtils.showConfirmationDialog(
                requireContext(),
                getString(R.string.label_confirmation_discard_title),
                getString(R.string.label_confirmation_discard_text)
            ) { findNavController().popBackStack() }
        }

        // initialize upload image button
        binding.btnSelectImage.setOnClickListener {
            pickImageResultLauncher.launch("image/*")
        }

        // initialize next step button
        binding.btnNextStep.setOnClickListener {
            showStepSummary()
        }

        // initialize step back button
        binding.btnStepBack.setOnClickListener {
            showStepGeneral()
        }

        // initialize create button
        binding.btnCreate.setOnClickListener {
            addContact()
        }
    }

    private fun addContact() {
        selectedImageUri?.let { uri ->
            val source = ImageDecoder.createSource(requireActivity().contentResolver, uri)
            val bitmap = ImageDecoder.decodeBitmap(source)
            // val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)
            val fileName = viewModel.saveImageToInternalStorage(requireContext(), bitmap)

            val contact = Contact(
                name = contactName,
                relation = relation,
                phoneNumber = phoneNumber,
                profileImagePath = fileName
            )

            // save contact to database
            viewModel.addContact(contact)
            findNavController().popBackStack()
        }
    }

    private fun showStepGeneral() {
        binding.stepGeneral.visibility = View.VISIBLE
        binding.stepSummary.visibility = View.GONE
    }

    private fun showStepSummary() {
        contactName = binding.tietContactName.text.toString()
        relation = binding.tietRelation.text.toString()
        phoneNumber = binding.tietPhoneNumber.text.toString()

        if (contactName.isNotBlank() && relation.isNotBlank() && phoneNumber.isNotBlank() && selectedImageUri != null) {
            binding.tvNameAndRelation.text =
                getString(R.string.text_contact_name_relation, contactName, relation)
            binding.tvPhoneNumber.text = phoneNumber

            binding.stepGeneral.visibility = View.GONE
            binding.stepSummary.visibility = View.VISIBLE
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.toast_provide_information),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}