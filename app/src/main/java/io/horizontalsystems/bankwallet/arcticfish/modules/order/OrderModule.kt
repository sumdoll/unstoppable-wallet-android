package io.horizontalsystems.bankwallet.arcticfish.modules.order

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.core.App

object OrderModule {

    class Factory(private val orderUid: String) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val fullCoin = App.marketKit.fullCoins(coinUids = listOf(orderUid)).first()
            val service = OrderService(fullCoin, App.marketFavoritesManager)
            return OrderViewModel(service, listOf(service), App.localStorage) as T
        }

    }

    enum class Tab(@StringRes val titleResId: Int) {
        Overview(R.string.Coin_Tab_Overview),
//        Details(R.string.Coin_Tab_Details),
        Market(R.string.Coin_Tab_Market),
//        Tweets(R.string.Coin_Tab_Tweets);
    }
}
