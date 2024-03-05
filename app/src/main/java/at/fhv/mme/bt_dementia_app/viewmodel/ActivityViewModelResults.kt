package at.fhv.mme.bt_dementia_app.viewmodel

sealed class AddActivityResult {
    data class Success(val activityId: Long) : AddActivityResult()
    data class Error(val message: String) : AddActivityResult()
}

sealed class UpdateActivityResult {
    object Success : UpdateActivityResult()
    data class Error(val message: String) : UpdateActivityResult()
}

sealed class DeleteActivityResult {
    data class Success(val activityId: Long) : DeleteActivityResult()
    data class Error(val message: String) : DeleteActivityResult()
}