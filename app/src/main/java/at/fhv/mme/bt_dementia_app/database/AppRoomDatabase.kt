package at.fhv.mme.bt_dementia_app.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import at.fhv.mme.bt_dementia_app.database.dao.ActivityDao
import at.fhv.mme.bt_dementia_app.database.dao.ContactDao
import at.fhv.mme.bt_dementia_app.database.dao.MedicationDao
import at.fhv.mme.bt_dementia_app.model.Activity
import at.fhv.mme.bt_dementia_app.model.Contact
import at.fhv.mme.bt_dementia_app.model.Medication
import at.fhv.mme.bt_dementia_app.utils.Converters

@Database(
    entities = [Activity::class, Contact::class, Medication::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppRoomDatabase : RoomDatabase() {
    abstract fun activityDao(): ActivityDao
    abstract fun contactDao(): ContactDao
    abstract fun medicationDao(): MedicationDao
}