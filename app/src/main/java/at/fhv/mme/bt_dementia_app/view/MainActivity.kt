package at.fhv.mme.bt_dementia_app.view

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import at.fhv.mme.bt_dementia_app.R
import at.fhv.mme.bt_dementia_app.databinding.ActivityMainBinding
import at.fhv.mme.bt_dementia_app.model.Activity
import at.fhv.mme.bt_dementia_app.utils.AlarmUtils
import at.fhv.mme.bt_dementia_app.utils.MediaPlayerUtils
import at.fhv.mme.bt_dementia_app.viewmodel.ActivityViewModel
import at.fhv.mme.bt_dementia_app.viewmodel.MedicationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    private val activityViewModel: ActivityViewModel by viewModels()
    private val medicationViewModel: MedicationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        navController = navHostFragment.navController

        binding.includeMain.bottomNavigation.setupWithNavController(navController)

        // remove bottom navigation bar from selected pages
        navController.addOnDestinationChangedListener { _, nd: NavDestination, _ ->
            if (nd.id == R.id.calendarFragment || nd.id == R.id.medicationFragment || nd.id == R.id.contactsFragment) {
                binding.includeMain.bottomNavigation.visibility = View.VISIBLE
            } else {
                binding.includeMain.bottomNavigation.visibility = View.GONE
            }
        }

        // create activities from medication for the next 3 days if not already created
        for (i in 0 until 3) {
            val date = LocalDate.now().plusDays(i.toLong())

            lifecycleScope.launch {
                val activities = activityViewModel.getAllActivitiesByDate(date).first()
                val medication = medicationViewModel.getAllMedicationByDay(date.dayOfWeek).first()

                for (med in medication) {
                    val isMedicationAlreadyCreated = activities.any { activity ->
                        activity.name == med.name && activity.time == med.time && activity.amount == med.amount && activity.unit == med.unit
                    }

                    if (!isMedicationAlreadyCreated) {
                        val activity = Activity(
                            name = med.name,
                            date = date,
                            time = med.time,
                            reminderTime = 0,
                            reminderAudioPath = "high_life_richard_smithson",
                            additionalInfo = "${med.amount} ${med.unit}",
                            amount = med.amount,
                            unit = med.unit
                        )
                        val activityId = activityViewModel.addActivity(activity)
                        if (activityId != -1L && !LocalDateTime.of(activity.date, activity.time)
                                .isAfter(LocalDateTime.now())
                        ) {
                            AlarmUtils.setupAlarm(
                                activityId,
                                activity,
                                applicationContext
                            )
                        }
                    }
                }
            }
        }

        // create activity from newly created medication
        medicationViewModel.addedMedication.observe(this) { medication ->
            for (i in 1..3) {
                val date = LocalDate.now().plusDays(i.toLong())
                if (date.dayOfWeek == medication.dayOfWeek) {
                    val activity = Activity(
                        name = medication.name,
                        date = date,
                        time = medication.time,
                        reminderTime = 0,
                        reminderAudioPath = "high_life_richard_smithson",
                        additionalInfo = "${medication.amount} ${medication.unit}",
                        amount = medication.amount,
                        unit = medication.unit
                    )
                    val activityId = activityViewModel.addActivity(activity)
                    if (activityId != -1L && !LocalDateTime.of(activity.date, activity.time)
                            .isAfter(LocalDateTime.now())
                    ) {
                        AlarmUtils.setupAlarm(
                            activityId,
                            activity,
                            applicationContext
                        )
                    }
                    break
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        MediaPlayerUtils.stopMediaPlayer()
    }
}