package io.horizontalsystems.bankwallet.modules.market.overview

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.horizontalsystems.bankwallet.core.App
import io.horizontalsystems.bankwallet.modules.market.MarketModule
import io.horizontalsystems.bankwallet.modules.market.MarketViewItem
import io.horizontalsystems.bankwallet.modules.market.TimeDuration
import io.horizontalsystems.bankwallet.modules.market.TopMarket
import io.horizontalsystems.bankwallet.modules.market.search.MarketSearchModule.DiscoveryItem.Category
import io.horizontalsystems.bankwallet.modules.market.toporders.MarketTopMoversRepository
import io.horizontalsystems.bankwallet.ui.compose.Select
import io.horizontalsystems.bankwallet.ui.extensions.MetricData
import java.math.BigDecimal

object MarketOverviewModule {

    class Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val topMarketsRepository = MarketTopMoversRepository(App.marketKit)
            val service = MarketOverviewService(
                topMarketsRepository,
                App.marketKit,
                App.backgroundManager,
                App.currencyManager
            )
            return MarketOverviewViewModel(service, App.currencyManager) as T
        }
    }

    @Immutable
    data class ViewItem(
        val marketMetrics: MarketMetrics,
        val boards: List<Board>,
    )

    data class MarketMetrics(
        val usdtPrice: MetricData,
        val usdPrice: MetricData,
    )

    data class MarketMetricsPoint(
        val value: BigDecimal,
        val timestamp: Long
    )

    data class Board(
        val boardHeader: BoardHeader,
        val marketViewItems: List<MarketViewItem>,
        val type: MarketModule.ListType
    )

    data class BoardHeader(
        val title: Int,
        val iconRes: Int,
        val topMarketSelect: Select<TopMarket>
    )

    data class TopNftCollectionsBoard(
        val title: Int,
        val iconRes: Int,
        val timeDurationSelect: Select<TimeDuration>,
    )

    data class TopSectorsBoard(
        val title: Int,
        val iconRes: Int,
        val items: List<Category>
    )

}
