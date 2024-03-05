package at.fhv.mme.bt_dementia_app.view.calendar

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
import at.fhv.mme.bt_dementia_app.databinding.FragmentCalendarBinding
import at.fhv.mme.bt_dementia_app.model.Activity
import at.fhv.mme.bt_dementia_app.utils.AlarmUtils
import at.fhv.mme.bt_dementia_app.utils.DialogUtils
import at.fhv.mme.bt_dementia_app.view.adapter.ActivityListAdapter
import at.fhv.mme.bt_dementia_app.viewmodel.ActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    private lateinit var activityListAdapter: ActivityListAdapter

    private val activityViewModel: ActivityViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activityViewModel.setNewDate(LocalDate.now())
        initViews()

        // observe date from the ViewModel
        activityViewModel.date.asLiveData().observe(viewLifecycleOwner) { date ->
            binding.tvCurrentDate.text = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        }

        // observe the activity list from the ViewModel
        activityViewModel.activities.asLiveData().observe(viewLifecycleOwner) { activities ->
            // update contact list in adapter
            activityListAdapter.submitList(activities)
        }
    }

    private fun initViews() {
        // set page title & date
        binding.header.tvTitle.setText(R.string.title_calendar)

        // initialize adapter & RecyclerView
        activityListAdapter = ActivityListAdapter(
            onSetDone = { activity: Activity, isDone: Boolean ->
                if (isDone) {
                    activityViewModel.setActivityDone(activity)
                }
            },
            onDelete = { activity: Activity ->
                DialogUtils.showConfirmationDialog(
                    requireContext(),
                    getString(R.string.label_confirmation_delete_title, "Activity"),
                    getString(R.string.label_confirmation_delete_text, "activity")
                ) {
                    activityViewModel.deleteActivity(activity)

                    // cancel alarm for deleted activity
                    AlarmUtils.cancelAlarm(activity, requireActivity())
                }
            }
        )

        binding.rvActivityList.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = activityListAdapter
        }

        // initialize previous/next day buttons
        binding.ibtnPreviousDay.setOnClickListener {
            activityViewModel.setNewDate(activityViewModel.date.value!!.minusDays(1))
        }
        binding.ibtnNextDay.setOnClickListener {
            activityViewModel.setNewDate(activityViewModel.date.value!!.plusDays(1))
        }

        // initialize add activity button
        binding.btnAddActivity.setOnClickListener {
            findNavController().navigate(R.id.action_calendarFragment_to_addActivityFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}