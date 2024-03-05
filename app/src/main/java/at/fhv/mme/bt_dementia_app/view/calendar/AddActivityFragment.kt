package at.fhv.mme.bt_dementia_app.view.calendar

import android.app.DatePickerDialog
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
import at.fhv.mme.bt_dementia_app.databinding.FragmentAddActivityBinding
import at.fhv.mme.bt_dementia_app.model.Activity
import at.fhv.mme.bt_dementia_app.utils.AlarmUtils
import at.fhv.mme.bt_dementia_app.utils.DialogUtils
import at.fhv.mme.bt_dementia_app.utils.MediaPlayerUtils
import at.fhv.mme.bt_dementia_app.viewmodel.ActivityViewModel
import at.fhv.mme.bt_dementia_app.viewmodel.AddActivityResult
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

@AndroidEntryPoint
class AddActivityFragment : Fragment() {

    private var _binding: FragmentAddActivityBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ActivityViewModel by viewModels()

    private val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    private var activityName: String = ""
    private var activityDate: String = ""
    private var activityTime: String = ""
    private var reminderTime: String = ""
    private var reminderAudio: String = ""
    private var selectedAudioIndex: Int = -1
    private var selectedAudioResource: Int = -1
    private var additionalReminderInfo: String = ""

    private val audioResources = intArrayOf(
        R.raw.high_life_richard_smithson,
        R.raw.mood_of_summer_abbynoise,
        R.raw.paradise_island_hartzmann
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddActivityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
        // set page title
        binding.header.tvTitle.setText(R.string.title_add_activity)

        // initialize back button
        binding.header.btnBack.setOnClickListener {
            DialogUtils.showConfirmationDialog(
                requireContext(),
                getString(R.string.label_confirmation_discard_title),
                getString(R.string.label_confirmation_discard_text)
            ) { findNavController().popBackStack() }
        }

        // turn media player off if song is over
        MediaPlayerUtils.setOnCompletionListener {
            binding.ibtnPlayReminderAudio.setImageResource(R.drawable.icon_play_arrow_24dp)
        }

        // initialize play image button
        binding.ibtnPlayReminderAudio.setOnClickListener {
            if (MediaPlayerUtils.isPlaying()) {
                MediaPlayerUtils.stopMediaPlayer()
                binding.ibtnPlayReminderAudio.setImageResource(R.drawable.icon_play_arrow_24dp)
            } else {
                if (selectedAudioResource > 0) {
                    MediaPlayerUtils.startMediaPlayer(requireContext(), selectedAudioResource)
                    binding.ibtnPlayReminderAudio.setImageResource(R.drawable.icon_pause_24dp)
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.toast_select_audio_first),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        // initialize next step buttons
        binding.btnNextStep1.setOnClickListener {
            showStepReminder()
        }
        binding.btnNextStep2.setOnClickListener {
            showStepSummary()
        }

        // initialize step back buttons
        binding.btnStepBack1.setOnClickListener {
            showStepGeneral()
        }
        binding.btnStepBack2.setOnClickListener {
            showStepReminder()
        }

        // initialize create button
        binding.btnCreate.setOnClickListener {
            addActivity()
        }

        // initialize date picker
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        binding.tietActivityDate.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                { _, mYear, mMonth, mDay ->
                    binding.tietActivityDate.setText(
                        String.format("%02d/%02d/%04d", mDay, mMonth + 1, mYear)
                    )
                },
                year,
                month,
                day
            ).show()
        }

        // initialize time picker
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        binding.tietActivityTime.setOnClickListener {
            TimePickerDialog(
                requireContext(),
                { _, mHour, mMinute ->
                    binding.tietActivityTime.setText(String.format("%02d:%02d", mHour, mMinute))
                },
                hour,
                minute,
                true
            ).show()
        }

        // initialize and set array adapter for reminder time (dropdown menu)
        val reminderTimeAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.reminder_times,
            android.R.layout.simple_dropdown_item_1line
        )
        binding.actvReminderTime.setAdapter(reminderTimeAdapter)

        // initialize and set array adapter for reminder audio (dropdown menu)
        val reminderAudioAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.raw_files_names,
            android.R.layout.simple_dropdown_item_1line
        )
        binding.actvReminderAudio.setAdapter(reminderAudioAdapter)

        binding.actvReminderAudio.setOnItemClickListener { parent, _, position, _ ->
            selectedAudioIndex = position
            reminderAudio = parent.getItemAtPosition(selectedAudioIndex).toString()
            selectedAudioResource = audioResources[selectedAudioIndex]
        }
    }

    private fun addActivity() {
        val activity = Activity(
            name = activityName,
            date = LocalDate.parse(activityDate, dateFormatter),
            time = LocalTime.parse(activityTime, timeFormatter),
            reminderTime = reminderTime.replace("\\D".toRegex(), "").toInt(),
            reminderAudioPath = requireContext().resources.getResourceEntryName(audioResources[selectedAudioIndex]),
            additionalInfo = additionalReminderInfo
        )

        // save activity to database
        viewModel.addActivity(activity)
        viewModel.addActivityResult.observe(viewLifecycleOwner) { result ->
            if (result is AddActivityResult.Success) {
                AlarmUtils.setupAlarm(result.activityId, activity, requireActivity())
                findNavController().popBackStack()
            }
        }
    }

    private fun showStepGeneral() {
        binding.stepGeneral.visibility = View.VISIBLE
        binding.stepReminder.visibility = View.GONE
        binding.stepSummary.visibility = View.GONE
    }

    private fun showStepReminder() {
        activityName = binding.tietActivityName.text.toString()
        activityDate = binding.tietActivityDate.text.toString()
        activityTime = binding.tietActivityTime.text.toString()

        if (activityName.isNotBlank() && activityDate.isNotBlank() && activityTime.isNotBlank()) {
            binding.stepGeneral.visibility = View.GONE
            binding.stepReminder.visibility = View.VISIBLE
            binding.stepSummary.visibility = View.GONE
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.toast_provide_information),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun showStepSummary() {
        reminderTime = binding.actvReminderTime.text.toString()
        reminderAudio = binding.actvReminderAudio.text.toString()
        additionalReminderInfo = binding.tietAdditionalReminderInfo.text.toString()

        if (reminderTime.isNotBlank() && reminderAudio.isNotBlank()) {
            binding.tvActivity.text = activityName
            binding.tvDateAndTime.text =
                getString(R.string.text_activity_date_time, activityDate, activityTime)
            binding.tvReminderTime.text = getString(R.string.text_reminder_time, reminderTime)
            binding.tvReminderAudio.text = reminderAudio
            binding.tvAdditionalInfo.text = additionalReminderInfo

            binding.stepGeneral.visibility = View.GONE
            binding.stepReminder.visibility = View.GONE
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
        MediaPlayerUtils.stopMediaPlayer()

        _binding = null
    }
}