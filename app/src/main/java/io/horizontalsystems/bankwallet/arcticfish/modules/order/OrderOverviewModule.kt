package io.horizontalsystems.bankwallet.arcticfish.modules.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.horizontalsystems.bankwallet.core.App
import io.horizontalsystems.bankwallet.entities.ConfiguredToken
import io.horizontalsystems.bankwallet.modules.chart.ChartCurrencyValueFormatterSignificant
import io.horizontalsystems.bankwallet.modules.chart.ChartModule
import io.horizontalsystems.bankwallet.modules.chart.ChartViewModel
import io.horizontalsystems.bankwallet.modules.market.user.FullInfo
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

    sealed class Data {
        class MetaData(val orderData: OrderData) : Data()
    }

    data class OrderData(
        val type: OrderType, // 订单类型
        val pays: List<OrderPayment>, // 订单支持的支付类型
        val cashAmount: Float, //金额
        val coinAmount: Float, // 数量
        val coinPrice: Float, // 单价
        val orderNumber: String, // 单号
        val orderTime: String, // 时间
        val userInfo: FullInfo, // 对手信息
        val stage: OrderStage, //阶段
        val state: OrderState // 状态
    )
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

enum class OrderType{
    Buy, Sell
}

enum class OrderPayment{
    Bank, AliPay, WePay
}

enum class OrderStage{
    NeedConfirm, WaitCash, PaidCash, TakeCoin, Timeout
}

enum class OrderState{
    Canceled, Runing, Done, Appealing, AppealSuccess, AppealFail
}

enum class OrderAction{
    DealOrder, CashPaid, CashReceived, AppealReq, AppealView
}
