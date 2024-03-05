package at.fhv.mme.bt_dementia_app.repository

import at.fhv.mme.bt_dementia_app.database.dao.MedicationDao
import at.fhv.mme.bt_dementia_app.model.Medication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.time.DayOfWeek
import javax.inject.Inject

class MedicationRepository @Inject constructor(private val medicationDao: MedicationDao) {
    fun getAllMedication(): Flow<List<Medication>> = medicationDao.getAllMedication()

    fun getAllMedicationByDay(day: DayOfWeek): Flow<List<Medication>> =
        medicationDao.getAllMedicationByDay(day)

    suspend fun addMedication(medication: Medication) {
        withContext(Dispatchers.IO) {
            medicationDao.addMedication(medication)
        }
    }

    suspend fun deleteMedication(medication: Medication) {
        withContext(Dispatchers.IO) {
            medicationDao.deleteMedication(medication)
        }
    }
}