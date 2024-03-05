package at.fhv.mme.bt_dementia_app.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import at.fhv.mme.bt_dementia_app.model.Contact
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {
    @Query("SELECT * FROM contacts ORDER BY `name`")
    fun getAllContacts(): Flow<List<Contact>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addContact(contact: Contact)

    @Delete
    fun deleteContact(contact: Contact)
}