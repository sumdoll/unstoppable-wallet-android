package io.horizontalsystems.bankwallet.arcticfish.modules.pond

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.core.App

object PondModule {

    class Factory(private val orderUid: String) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val fullCoin = App.marketKit.fullCoins(coinUids = listOf(orderUid)).first()
            val service = PondService(fullCoin, App.marketFavoritesManager)
            return PondViewModel(service, listOf(service), App.localStorage) as T
        }

    }
}


enum class PondStage{
    // 已提交、通过、失败
    Submitted, Passed, Resubmit
}
