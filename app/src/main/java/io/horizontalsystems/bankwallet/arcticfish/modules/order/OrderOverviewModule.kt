package io.horizontalsystems.bankwallet.arcticfish.modules.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.horizontalsystems.bankwallet.core.App
import io.horizontalsystems.bankwallet.entities.ConfiguredToken
import io.horizontalsystems.bankwallet.modules.chart.ChartCurrencyValueFormatterSignificant
import io.horizontalsystems.bankwallet.modules.chart.ChartModule
import io.horizontalsystems.bankwallet.modules.chart.ChartViewModel
import io.horizontalsystems.marketkit.models.FullCoin
import io.horizontalsystems.marketkit.models.MarketInfoOverview

object OrderOverviewModule {

    class Factory(private val fullCoin: FullCoin) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            return when (modelClass) {
                OrderOverviewViewModel::class.java -> {
                    val currency = App.currencyManager.baseCurrency
                    val service = OrderOverviewService(
                        fullCoin,
                        App.marketKit,
                        App.currencyManager,
                        App.appConfigProvider,
                        App.languageManager
                    )

                    OrderOverviewViewModel(
                        service,
                        OrderViewFactory(currency, App.numberFormatter),
                        App.walletManager,
                        App.accountManager
                    ) as T
                }
                ChartViewModel::class.java -> {
                    val chartService = OrderOverviewChartService(App.marketKit, App.currencyManager, fullCoin.coin.uid)
                    val chartNumberFormatter = ChartCurrencyValueFormatterSignificant()
                    ChartModule.createViewModel(chartService, chartNumberFormatter) as T
                }
                else -> throw IllegalArgumentException()
            }
        }

    }
}

data class OrderOverviewItem(
    val coinCode: String,
    val marketInfoOverview: MarketInfoOverview,
    val guideUrl: String?,
)

data class OrderVariant(
    val value: String,
    val copyValue: String?,
    val imgUrl: String,
    val explorerUrl: String?,
    val name: String?,
    val configuredToken: ConfiguredToken,
    val canAddToWallet: Boolean,
    val inWallet: Boolean,
) {
}

data class OrderOverviewViewItem(
    val roi: List<RoiViewItem>,
    val categories: List<String>,
    val links: List<CoinLink>,
    val about: String,
    val marketData: MutableList<CoinDataItem>
)
