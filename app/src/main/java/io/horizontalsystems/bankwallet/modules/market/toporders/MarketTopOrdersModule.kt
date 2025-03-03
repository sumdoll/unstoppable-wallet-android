package io.horizontalsystems.bankwallet.modules.market.toporders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.horizontalsystems.bankwallet.core.App
import io.horizontalsystems.bankwallet.modules.market.MarketField
import io.horizontalsystems.bankwallet.modules.market.SortingField
import io.horizontalsystems.bankwallet.modules.market.TopMarket
import io.horizontalsystems.bankwallet.ui.compose.Select

object MarketTopOrdersModule {

    class Factory(
        private val topMarket: TopMarket? = null,
        private val sortingField: SortingField? = null,
        private val marketField: MarketField? = null
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val topMarketsRepository = MarketTopMoversRepository(App.marketKit)
            val service = MarketTopOrdersService(
                topMarketsRepository,
                App.currencyManager,
                App.marketFavoritesManager,
                topMarket ?: defaultTopMarket,
                sortingField ?: defaultSortingField
            )
            return MarketTopOrdersViewModel(
                service,
                marketField ?: defaultMarketField
            ) as T
        }

        companion object {
            val defaultSortingField = SortingField.TopSales
            val defaultTopMarket = TopMarket.Top100
            val defaultMarketField = MarketField.PriceDiff
        }
    }

    data class Menu(
        val sortingFieldSelect: Select<SortingField>,
        val topMarketSelect: Select<TopMarket>?,
        val marketFieldSelect: Select<MarketField>
    )

}

sealed class SelectorDialogState() {
    object Closed : SelectorDialogState()
    class Opened(val select: Select<SortingField>) : SelectorDialogState()
}
