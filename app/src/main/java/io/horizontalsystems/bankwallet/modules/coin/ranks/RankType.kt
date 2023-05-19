package io.horizontalsystems.bankwallet.modules.coin.ranks

import android.os.Parcelable
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.modules.market.ImageSource
import kotlinx.parcelize.Parcelize

@Parcelize
enum class RankType(val title: Int, val description: Int, val headerIconName: String): Parcelable {
    CexVolumeRank(R.string.CoinAnalytics_CexVolumeRank, R.string.CoinAnalytics_CexVolumeRank_Description, "cex_volume" ),
    DexVolumeRank(R.string.CoinAnalytics_DexVolumeRank, R.string.CoinAnalytics_DexVolumeRank_Description, "dex_volume"),
    DexLiquidityRank(R.string.CoinAnalytics_DexLiquidityRank, R.string.CoinAnalytics_DexLiquidityRank_Description, "dex_liquidity"),
    AddressesRank(R.string.CoinAnalytics_ActiveAddressesRank, R.string.CoinAnalytics_ActiveAddressesRank_Description, "active_addresses"),
    TransactionCountRank(R.string.CoinAnalytics_TransactionCountRank, R.string.CoinAnalytics_TransactionCountRank, "trx_count"),
    RevenueRank(R.string.CoinAnalytics_ProjectRevenueRank, R.string.CoinAnalytics_ProjectRevenueRank_Description, "revenue");

    val headerIcon: ImageSource
        get() = ImageSource.Remote("https://cdn.blocksdecoded.com/header-images/$headerIconName@3x.png")
}