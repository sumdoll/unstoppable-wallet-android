package io.horizontalsystems.bankwallet.modules.metricchart

import android.os.Parcelable
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.modules.market.ImageSource
import kotlinx.parcelize.Parcelize

@Parcelize
enum class MetricsType : Parcelable {
    TotalMarketCap, BtcDominance, Volume24h, UsdtC2C, UsdRate;

    val title: Int
        get() = when (this) {
            TotalMarketCap -> R.string.MarketGlobalMetrics_TotalMarketCap
            BtcDominance -> R.string.MarketGlobalMetrics_BtcDominance
            Volume24h -> R.string.MarketGlobalMetrics_Volume
            UsdtC2C -> R.string.MarketGlobalMetrics_UsdtC2C
            UsdRate -> R.string.MarketGlobalMetrics_UsdRate
        }

    val description: Int
        get() = when (this) {
            TotalMarketCap -> R.string.MarketGlobalMetrics_TotalMarketCapDescription
            BtcDominance -> R.string.MarketGlobalMetrics_BtcDominanceDescription
            Volume24h -> R.string.MarketGlobalMetrics_VolumeDescription
            UsdtC2C -> R.string.MarketGlobalMetrics_USDTC2CDescription
            UsdRate -> R.string.MarketGlobalMetrics_USDRateDescription
        }

    val headerIcon: ImageSource
        get() {
            val imageName = when (this) {
                TotalMarketCap,
                BtcDominance -> "total_mcap"
                Volume24h -> "total_volume"
                UsdtC2C -> "usdt_c2c"
                UsdRate -> "usd_rate"
            }

            return ImageSource.Remote("https://cdn.blocksdecoded.com/header-images/$imageName@3x.png")
        }
}
