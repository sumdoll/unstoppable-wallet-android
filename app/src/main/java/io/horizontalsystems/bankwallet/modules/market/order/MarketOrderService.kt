package io.horizontalsystems.bankwallet.modules.market.order

import io.horizontalsystems.bankwallet.core.App
import io.horizontalsystems.bankwallet.core.managers.MarketFavoritesManager
import io.horizontalsystems.bankwallet.core.managers.MarketKitWrapper
import io.horizontalsystems.bankwallet.modules.market.TimeDuration
import io.horizontalsystems.bankwallet.ui.compose.Select
import io.horizontalsystems.bankwallet.entities.Currency
import io.horizontalsystems.marketkit.models.CoinCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.rx2.await
import kotlinx.coroutines.withContext

class MarketOrderService(
    private val marketKit: MarketKitWrapper,
    private val marketFavoritesManager: MarketFavoritesManager,
    private val baseCurrency: Currency,
) {

    private var categories: List<CoinCategory> = listOf()

    private var filter = ""

    private val periodOptions = TimeDuration.values().toList()

    private var selectedPeriod = periodOptions[0]

    private val _serviceDataFlow =
        MutableSharedFlow<Result<MarketOrderModule.Data>>(
            replay = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )

    val serviceDataFlow = _serviceDataFlow.asSharedFlow()

    val favoriteDataUpdated by marketFavoritesManager::dataUpdatedAsync

    val timePeriodMenu = Select(selectedPeriod, periodOptions)

    var sortDescending = true
        private set

    suspend fun updateState() = withContext(Dispatchers.IO) {
        try {
//            if (filter.isBlank()) {
//                if (categories.isEmpty()) {
//                    categories = marketKit.coinCategoriesSingle(baseCurrency.code).await()
//                }
//                _serviceDataFlow.tryEmit(Result.success((DiscoveryItems(discoveryItems))))
//            } else {
//                _serviceDataFlow.tryEmit(Result.success((SearchResult(coinItems))))
//            }
        } catch (e: Exception) {
            _serviceDataFlow.tryEmit(Result.failure(e))
        }
    }

    fun unFavorite(coinUid: String) {
        marketFavoritesManager.remove(coinUid)
    }

    fun favorite(coinUid: String) {
        marketFavoritesManager.add(coinUid)
    }

    suspend fun setFilter(filter: String) {
        this.filter = filter
        updateState()
    }

    suspend fun setTimePeriod(timeDuration: TimeDuration) {
        selectedPeriod = timeDuration
        updateState()
    }

    suspend fun toggleSortType() {
        sortDescending = !sortDescending
        updateState()
    }

}
