package io.horizontalsystems.bankwallet.arcticfish.modules.appeal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.core.Clearable
import io.horizontalsystems.bankwallet.core.ILocalStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx2.asFlow

class AppealViewModel(
    private val service: AppealService,
    private val clearables: List<Clearable>,
    private val localStorage: ILocalStorage,
) : ViewModel() {
    val fullCoin by service::fullCoin

    val isBuyCancelEnabled = localStorage.marketsTabEnabled
    var isFavorite by mutableStateOf<Boolean>(false)
        private set
    var successMessage by mutableStateOf<Int?>(null)
        private set

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

    fun onCancelClick() {
        service.cancel()
        successMessage = R.string.Hud_Added_To_Watchlist
    }

    fun onSuccessMessageShown() {
        successMessage = null
    }

}
