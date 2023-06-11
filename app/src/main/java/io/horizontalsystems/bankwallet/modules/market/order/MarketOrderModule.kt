package io.horizontalsystems.bankwallet.modules.market.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.horizontalsystems.bankwallet.core.App
import io.horizontalsystems.bankwallet.modules.market.user.FullInfo
import io.horizontalsystems.marketkit.models.CoinCategory
import io.horizontalsystems.marketkit.models.FullCoin
import java.math.BigDecimal
import javax.annotation.concurrent.Immutable

object MarketOrderModule {

    class Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val service = MarketOrderService(
                App.marketKit,
                App.marketFavoritesManager,
                App.currencyManager.baseCurrency
            )
            return MarketOrderViewModel(service) as T
        }
    }

    sealed class Data {
        class MetaData(val orderData: OrderData) : Data()
    }

//    sealed class DiscoveryItem {
//        object TopCoins : DiscoveryItem()
//
//        class Category(
//            val coinCategory: CoinCategory,
//            val marketData: CategoryMarketData? = null
//        ) : DiscoveryItem()
//    }
//
//    @Immutable
//    class CoinItem(val fullCoin: FullCoin, val favourited: Boolean)
//
//    data class CategoryMarketData(
//        val marketCap: String? = null,
//        val diff: BigDecimal? = null
//    )

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
    Canceled, Done, Appealing, AppealSuccess, AppealFail
}

enum class OrderAction{
    DealOrder, CashPaid, CashReceived, AppealReq, AppealView
}
