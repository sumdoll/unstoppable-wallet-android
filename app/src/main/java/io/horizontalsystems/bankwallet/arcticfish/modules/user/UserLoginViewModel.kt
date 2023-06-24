package io.horizontalsystems.bankwallet.arcticfish.modules.user

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.core.Clearable
import io.horizontalsystems.bankwallet.core.ILocalStorage
import io.horizontalsystems.bankwallet.modules.contacts.ContactsModule
import io.horizontalsystems.bankwallet.modules.contacts.viewmodel.ContactViewModel
import io.horizontalsystems.bankwallet.modules.restoreaccount.restoremenu.RestoreMenuModule
import io.horizontalsystems.bankwallet.modules.restoreaccount.restoremenu.RestoreMenuViewModel
import io.horizontalsystems.bankwallet.ui.compose.TranslatableString
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx2.asFlow

class UserLoginViewModel(
    private val service: UserLoginService,
    private val clearables: List<Clearable>,
    private val localStorage: ILocalStorage,
) : ViewModel() {

    var uiState by mutableStateOf(uiState())
        private set

    private var userName = ""
    private var loginPassword = ""
    private var error: UserLoginModule.ValidException? = null
//    val fullCoin by service::fullCoin

    val isBuyCancelEnabled = localStorage.marketsTabEnabled
    var isFavorite by mutableStateOf<Boolean>(false)
        private set
    var successMessage by mutableStateOf<Int?>(null)
        private set

    val choiceOptions = UserLoginModule.LoginOption.values().toList()

    var choiceOption by mutableStateOf(UserLoginModule.LoginOption.Login)
        private set

    fun onLoginOptionSelected(option: UserLoginModule.LoginOption) {
        choiceOption = option
    }

    init {
        viewModelScope.launch {
            val isCancelFlow: Flow<Boolean> = service.isCanceling.asFlow()
            isCancelFlow.collect {
                isFavorite = it
            }
        }
    }

    override fun onCleared() {
        clearables.forEach(Clearable::clear)
    }

    fun onLoginClick() {
//        service.cancel()
        successMessage = R.string.Hud_Added_To_Watchlist
    }

    fun onRegisterClick() {
//        service.cancel()
        successMessage = R.string.Hud_Added_To_Watchlist
    }

    fun onForgotClick() {
//        service.cancel()
        successMessage = R.string.Hud_Added_To_Watchlist
    }

    fun onSuccessMessageShown() {
        successMessage = null
    }

    fun onNameChange(name: String) {
        userName = name
//
//        error = try {
//            repository.validateContactName(contactUid = contact.uid, name = name)
//            null
//        } catch (ex: ContactsModule.ContactValidationException.DuplicateContactName) {
//            ex
//        }

        emitUiState()
    }

    fun onPasswordChange(password: String) {
        loginPassword = password
//        val editedContact = contact.copy(
//            name = uiState.contactName,
//            addresses = uiState.addressViewItems.map { it.contactAddress }
//        )
//        repository.save(editedContact)
//
//        closeAfterSave = true

        emitUiState()
    }

    private fun isLoginEnabled(): Boolean {
        return userName.isNotEmpty() && userName.isNotBlank()
    }

    private fun emitUiState() {
        uiState = uiState()
    }

    private fun uiState() = UserLoginViewModel.UiState(
        loginName = userName,
        loginPassword = userName,
        enableLogin = isLoginEnabled(),
        error = error
    )

    data class UiState(
        val loginName: String,
        val loginPassword: String,
        val enableLogin: Boolean,
        val error: UserLoginModule.ValidException?
    )
}
