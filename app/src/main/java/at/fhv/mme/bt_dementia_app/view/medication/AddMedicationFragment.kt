package at.fhv.mme.bt_dementia_app.view.medication

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import at.fhv.mme.bt_dementia_app.R
import at.fhv.mme.bt_dementia_app.databinding.FragmentAddMedicationBinding
import at.fhv.mme.bt_dementia_app.model.Medication
import at.fhv.mme.bt_dementia_app.utils.DialogUtils
import at.fhv.mme.bt_dementia_app.viewmodel.ActivityViewModel
import at.fhv.mme.bt_dementia_app.viewmodel.MedicationViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

@AndroidEntryPoint
class AddMedicationFragment : Fragment() {

    private var _binding: FragmentAddMedicationBinding? = null
    private val binding get() = _binding!!

    private val medicationViewModel: MedicationViewModel by viewModels()

    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    private var medicationName: String = ""
    private var medicationDay: String = ""
    private var medicationTime: String = ""
    private var amount: String = ""
    private var unit: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddMedicationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
        // set page title
        binding.header.tvTitle.setText(R.string.title_add_medication)

        // initialize back button
        binding.header.btnBack.setOnClickListener {
            DialogUtils.showConfirmationDialog(
                requireContext(),
                getString(R.string.label_confirmation_discard_title),
                getString(R.string.label_confirmation_discard_text)
            ) { findNavController().popBackStack() }
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
            addMedication()
        }

        // initialize and set array adapter for medication day (dropdown menu)
        val medicationDayAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.medication_day,
            android.R.layout.simple_dropdown_item_1line
        )
        binding.actvMedicationDay.setAdapter(medicationDayAdapter)

        // initialize time picker
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        binding.tietMedicationTime.setOnClickListener {
            TimePickerDialog(
                requireContext(),
                { _, mHour, mMinute ->
                    binding.tietMedicationTime.setText(String.format("%02d:%02d", mHour, mMinute))
                },
                hour,
                minute,
                true
            ).show()
        }

        // initialize and set array adapter for medication day (dropdown menu)
        val medicationAmountAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.medication_unit,
            android.R.layout.simple_dropdown_item_1line
        )
        binding.actvMedicationUnit.setAdapter(medicationAmountAdapter)
    }

    private fun addMedication() {
        val medication = Medication(
            name = medicationName,
            dayOfWeek = DayOfWeek.valueOf(medicationDay.uppercase()),
            time = LocalTime.parse(medicationTime, timeFormatter),
            amount = amount.toInt(),
            unit = unit
        )

        // save medication to database
        medicationViewModel.addMedication(medication)
        findNavController().popBackStack()
    }

    private fun showStepGeneral() {
        binding.stepGeneral.visibility = View.VISIBLE
        binding.stepSummary.visibility = View.GONE
    }

    private fun showStepSummary() {
        medicationName = binding.tietMedicationName.text.toString()
        medicationDay = binding.actvMedicationDay.text.toString()
        medicationTime = binding.tietMedicationTime.text.toString()
        amount = binding.tietMedicationAmount.text.toString()
        unit = binding.actvMedicationUnit.text.toString()

        if (medicationName.isNotBlank() && medicationDay.isNotBlank() && medicationTime.isNotBlank() && amount.isNotBlank() && unit.isNotBlank()) {
            binding.tvName.text = medicationName
            binding.tvDay.text = getString(R.string.text_medication_day, medicationDay)
            binding.tvTime.text = getString(R.string.text_medication_time, medicationTime)
            binding.tvAmount.text = getString(R.string.text_medication_amount, amount, unit)

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