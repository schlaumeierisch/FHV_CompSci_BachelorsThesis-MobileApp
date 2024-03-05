package at.fhv.mme.bt_dementia_app.view.medication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import at.fhv.mme.bt_dementia_app.R
import at.fhv.mme.bt_dementia_app.databinding.FragmentMedicationBinding
import at.fhv.mme.bt_dementia_app.model.Medication
import at.fhv.mme.bt_dementia_app.utils.DialogUtils
import at.fhv.mme.bt_dementia_app.view.adapter.MedicationListAdapter
import at.fhv.mme.bt_dementia_app.viewmodel.MedicationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MedicationFragment : Fragment() {

    private var _binding: FragmentMedicationBinding? = null
    private val binding get() = _binding!!

    private lateinit var medicationListAdapter: MedicationListAdapter

    private val viewModel: MedicationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMedicationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        // observe the medication list from the ViewModel
        viewModel.allMedication.asLiveData().observe(viewLifecycleOwner) { medication ->
            // update medication list in adapter
            medicationListAdapter.submitList(medication)
        }
    }

    private fun initViews() {
        // set page title
        binding.header.tvTitle.setText(R.string.title_medication)

        // initialize adapter & RecyclerView
        medicationListAdapter = MedicationListAdapter(
            onDelete = { medication: Medication ->
                DialogUtils.showConfirmationDialog(
                    requireContext(),
                    getString(R.string.label_confirmation_delete_title, "Medication"),
                    getString(R.string.label_confirmation_delete_text, "medication")
                ) { viewModel.deleteMedication(medication) }
            }
        )

        binding.rvMedicationList.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = medicationListAdapter
        }

        // initialize add medication button
        binding.btnAddMedication.setOnClickListener {
            findNavController().navigate(R.id.action_medicationFragment_to_addMedicationFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}