package at.fhv.mme.bt_dementia_app.viewmodel

sealed class AddContactResult {
    object Success : AddContactResult()
    data class Error(val message: String) : AddContactResult()
}

sealed class DeleteContactResult {
    object Success : DeleteContactResult()
    data class Error(val message: String) : DeleteContactResult()
}