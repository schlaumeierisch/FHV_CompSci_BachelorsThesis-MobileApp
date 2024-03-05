package at.fhv.mme.bt_dementia_app.viewmodel

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import at.fhv.mme.bt_dementia_app.model.Contact
import at.fhv.mme.bt_dementia_app.repository.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(
    private val contactRepository: ContactRepository,
    application: Application
) : AndroidViewModel(application) {
    val contacts: Flow<List<Contact>> = contactRepository.getAllContacts()

    val addContactResult = MutableLiveData<AddContactResult>()
    val deleteContactResult = MutableLiveData<DeleteContactResult>()

    fun addContact(contact: Contact) {
        viewModelScope.launch {
            try {
                contactRepository.addContact(contact)
                addContactResult.postValue(AddContactResult.Success)
            } catch (e: Exception) {
                addContactResult.postValue(
                    AddContactResult.Error("Error while adding contact: ${e.message}")
                )
            }
        }
    }

    fun deleteContact(contact: Contact) {
        viewModelScope.launch {
            try {
                contactRepository.deleteContact(contact)
                deleteImageFromInternalStorage(getApplication(), contact.profileImagePath)
                deleteContactResult.postValue(DeleteContactResult.Success)
            } catch (e: Exception) {
                deleteContactResult.postValue(
                    DeleteContactResult.Error("Error while deleting contact: ${e.message}")
                )
            }
        }
    }

    // runBlocking to start coroutine and wait until it's done
    fun saveImageToInternalStorage(context: Context, bitmap: Bitmap): String = runBlocking {
        withContext(Dispatchers.IO) {
            // open a private file associated with this Context's application package for writing
            val fileName = "${UUID.randomUUID()}.jpg"

            context.openFileOutput(fileName, Context.MODE_PRIVATE).use { output ->
                // compress the bitmap, write it to the output stream and then flush and close the stream
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output)
                output.flush()
            }

            fileName
        }
    }

    private fun deleteImageFromInternalStorage(context: Context, filename: String) {
        context.deleteFile(filename)
    }
}