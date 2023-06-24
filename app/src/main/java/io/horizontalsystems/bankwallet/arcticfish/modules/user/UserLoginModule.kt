package io.horizontalsystems.bankwallet.arcticfish.modules.user

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.core.App
import io.horizontalsystems.bankwallet.core.providers.Translator
import io.horizontalsystems.bankwallet.modules.contacts.model.Contact

object UserLoginModule {

    class Factory() : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val service = UserLoginService()
            return UserLoginViewModel(service, listOf(service), App.localStorage) as T
        }

    }


    enum class LoginOption(@StringRes val titleRes: Int) {
        Register(R.string.Restore_RecoveryPhrase),
        Login(R.string.Restore_RecoveryPhrase),
        Forgot(R.string.Restore_PrivateKey)
    }


    sealed class ValidException(override val message: String?) : Throwable() {
        object DuplicateName : ValidException(Translator.getString(R.string.User_HasExist))
        object DuplicateAddress : ValidException(Translator.getString(R.string.Valid_Mail))
    }
}
