package io.horizontalsystems.bankwallet.widgets

import io.horizontalsystems.bankwallet.core.App
import io.horizontalsystems.bankwallet.core.imageUrl
import io.horizontalsystems.bankwallet.core.managers.CurrencyManager
import io.horizontalsystems.bankwallet.core.managers.MarketFavoritesManager
import io.horizontalsystems.bankwallet.core.managers.MarketKitWrapper
import io.horizontalsystems.bankwallet.modules.market.*
import io.horizontalsystems.bankwallet.modules.market.favorites.MarketFavoritesMenuService
import kotlinx.coroutines.rx2.await
import java.math.BigDecimal

class MarketWidgetRepository(
    private val marketKit: MarketKitWrapper,
    private val favoritesManager: MarketFavoritesManager,
    private val favoritesMenuService: MarketFavoritesMenuService,
    private val currencyManager: CurrencyManager
) {
    companion object {
        private const val topGainers = 100
        private const val itemsLimit = 5
    }

    private val currency
        get() = currencyManager.baseCurrency

    suspend fun getMarketItems(marketWidgetType: MarketWidgetType): List<MarketWidgetItem> =
        when (marketWidgetType) {
            MarketWidgetType.Watchlist -> {
                getWatchlist()
            }
            MarketWidgetType.TopGainers -> {
                getTopGainers()
            }
        }

    private suspend fun getTopGainers(): List<MarketWidgetItem> {
        val marketItems = marketKit.marketInfosSingle(topGainers, currency.code)
            .await()
            .map { MarketItem.createFromCoinMarket(it, currency) }

        val sortedMarketItems = marketItems
            .subList(0, Integer.min(marketItems.size, topGainers))
            .sort(SortingField.TopSales)
            .subList(0, Integer.min(marketItems.size, itemsLimit))

        return sortedMarketItems.map { marketWidgetItem(it, MarketField.PriceDiff) }
    }

    private suspend fun getWatchlist(): List<MarketWidgetItem> {
        val favoriteCoins = favoritesManager.getAll()
        var marketItems = listOf<MarketItem>()

        if (favoriteCoins.isNotEmpty()) {
            val favoriteCoinUids = favoriteCoins.map { it.coinUid }
            marketItems = marketKit.marketInfosSingle(favoriteCoinUids, currency.code)
                .await()
                .map { marketInfo ->
                    MarketItem.createFromCoinMarket(marketInfo, currency)
                }
                .sort(favoritesMenuService.sortingField)
        }

        return marketItems.map { marketWidgetItem(it, favoritesMenuService.marketField) }
    }

    private fun marketWidgetItem(
        marketItem: MarketItem,
        marketField: MarketField,
    ): MarketWidgetItem {
        var marketCap: String? = null
        var volume: String? = null
        var diff: BigDecimal? = null

        when (marketField) {
            MarketField.Volume -> {
                volume = App.numberFormatter.formatFiatShort(marketItem.volume.value, marketItem.volume.currency.symbol, 2)
            }
            MarketField.PriceDiff -> {
                diff = marketItem.diff
            }
        }

        return MarketWidgetItem(
            uid = marketItem.fullCoin.coin.uid,
            title = marketItem.fullCoin.coin.name,
            subtitle = marketItem.fullCoin.coin.code,
            label = marketItem.fullCoin.coin.marketCapRank?.toString() ?: "",
            value = App.numberFormatter.formatFiatFull(
                marketItem.rate.value,
                marketItem.rate.currency.symbol
            ),
            marketCap = marketCap,
            volume = volume,
            diff = diff,
            blockchainTypeUid = null,
            imageRemoteUrl = marketItem.fullCoin.coin.imageUrl
        )
    }

}
