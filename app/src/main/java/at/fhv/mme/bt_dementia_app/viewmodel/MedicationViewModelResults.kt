package at.fhv.mme.bt_dementia_app.viewmodel

sealed class AddMedicationResult {
    object Success : AddMedicationResult()
    data class Error(val message: String) : AddMedicationResult()
}

sealed class DeleteMedicationResult {
    object Success : DeleteMedicationResult()
    data class Error(val message: String) : DeleteMedicationResult()
}