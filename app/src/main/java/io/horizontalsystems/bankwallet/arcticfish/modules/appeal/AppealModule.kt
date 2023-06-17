package io.horizontalsystems.bankwallet.arcticfish.modules.appeal

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.core.App

object AppealModule {

    class Factory(private val orderUid: String) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val fullCoin = App.marketKit.fullCoins(coinUids = listOf(orderUid)).first()
            val service = AppealService(fullCoin, App.marketFavoritesManager)
            return AppealViewModel(service, listOf(service), App.localStorage) as T
        }

    }
}


enum class AppealStage{
    // 提交、辩诉、审查、完成
    Submit, Plea, Scrutiny, Done
}
