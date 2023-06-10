package io.horizontalsystems.bankwallet.modules.market.overview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.core.App
import io.horizontalsystems.bankwallet.core.managers.CurrencyManager
import io.horizontalsystems.bankwallet.core.subscribeIO
import io.horizontalsystems.bankwallet.entities.CoinValue
import io.horizontalsystems.bankwallet.entities.Currency
import io.horizontalsystems.bankwallet.entities.ViewState
import io.horizontalsystems.bankwallet.modules.market.*
import io.horizontalsystems.bankwallet.modules.market.MarketModule.ListType
import io.horizontalsystems.bankwallet.modules.market.overview.MarketOverviewModule.Board
import io.horizontalsystems.bankwallet.modules.market.overview.MarketOverviewModule.BoardHeader
import io.horizontalsystems.bankwallet.modules.market.overview.MarketOverviewModule.MarketMetrics
import io.horizontalsystems.bankwallet.modules.market.overview.MarketOverviewModule.MarketMetricsPoint
import io.horizontalsystems.bankwallet.modules.metricchart.MetricsType
import io.horizontalsystems.bankwallet.ui.compose.Select
import io.horizontalsystems.bankwallet.ui.extensions.MetricData
import io.horizontalsystems.chartview.ChartData
import io.horizontalsystems.chartview.models.ChartPoint
import io.horizontalsystems.marketkit.models.*
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.math.BigDecimal

class MarketOverviewViewModel(
    private val service: MarketOverviewService,
    private val currencyManager: CurrencyManager
) : ViewModel() {

    private val disposables = CompositeDisposable()

    val viewStateLiveData = MutableLiveData<ViewState>(ViewState.Loading)
    val viewItem = MutableLiveData<MarketOverviewModule.ViewItem>()
    val isRefreshingLiveData = MutableLiveData<Boolean>()

    val topNftCollectionsParams: Pair<SortingField, TimeDuration>
        get() = Pair(topNftsSortingField, topNftsTimeDuration)

    var gainersTopMarket: TopMarket = TopMarket.Top100
        private set
    var losersTopMarket: TopMarket = TopMarket.Top100
        private set
    var topNftsTimeDuration: TimeDuration = TimeDuration.SevenDay
        private set
    val topNftsSortingField: SortingField = SortingField.TopSales
    var topPlatformsTimeDuration: TimeDuration = TimeDuration.OneDay
        private set

    var topMovers: TopMovers? = null
    var marketOverview: MarketOverview? = null

    val baseCurrency: Currency
        get() = currencyManager.baseCurrency

    private fun syncViewItems() {
        val topMovers = topMovers ?: return
        val marketOverview = marketOverview ?: return

        val viewItem = getViewItem(
            topMovers,
            marketOverview
        )
        this.viewItem.postValue(viewItem)
        viewStateLiveData.postValue(ViewState.Success)
    }


    init {
        Observable
            .combineLatest(
                service.topMoversObservable,
                service.marketOverviewObservable
            ) { t1, t2 ->
                Pair(t1, t2)
            }
            .subscribeIO { overviewItems ->
                val error = listOfNotNull(
                    overviewItems.first.exceptionOrNull(),
                    overviewItems.second.exceptionOrNull(),
                ).firstOrNull()

                if (error != null) {
                    viewStateLiveData.postValue(ViewState.Error(error))
                } else {
                    topMovers = overviewItems.first.getOrNull()
                    marketOverview = overviewItems.second.getOrNull()

                    if (
                        topMovers != null
                        && marketOverview != null
                    ) {
                        syncViewItems()
                    }
                }
            }.let {
                disposables.add(it)
            }

        service.start()
    }

    private fun getViewItem(
        topMovers: TopMovers,
        marketOverview: MarketOverview
    ): MarketOverviewModule.ViewItem {
        val topGainersBoard = getBoard(ListType.TopGainers, topMovers)
        val topLosersBoard = getBoard(ListType.TopLosers, topMovers)

        return MarketOverviewModule.ViewItem(
            getMarketMetrics(marketOverview.globalMarketPoints, baseCurrency),
            listOf(topGainersBoard, topLosersBoard)
        )
    }

    private fun getBoard(type: ListType, topMovers: TopMovers): Board {
        val topMarket: TopMarket

        val marketInfoList = when (type) {
            ListType.TopGainers -> {
                topMarket = gainersTopMarket

                when (gainersTopMarket) {
                    TopMarket.Top100 -> topMovers.gainers100
                    TopMarket.Top200 -> topMovers.gainers200
                    TopMarket.Top300 -> topMovers.gainers300
                }
            }

            ListType.TopLosers -> {
                topMarket = losersTopMarket

                when (losersTopMarket) {
                    TopMarket.Top100 -> topMovers.losers100
                    TopMarket.Top200 -> topMovers.losers200
                    TopMarket.Top300 -> topMovers.losers300
                }
            }
        }

        val marketItems = marketInfoList.map { MarketItem.createFromCoinMarket(it, baseCurrency) }
        val topList = marketItems.map { MarketViewItem.create(it, type.marketField) }

        val boardHeader = BoardHeader(
            getSectionTitle(type),
            getSectionIcon(type),
            Select(topMarket, service.topMarketOptions)
        )
        return Board(boardHeader, topList, type)
    }

    private fun getMarketMetrics(globalMarketPoints: List<GlobalMarketPoint>, baseCurrency: Currency): MarketMetrics {
        var defiMarketCap: BigDecimal? = null
        var defiMarketCapDiff: BigDecimal? = null
        var tvl: BigDecimal? = null
        var tvlDiff: BigDecimal? = null

        if (globalMarketPoints.isNotEmpty()) {
            val startingPoint = globalMarketPoints.first()
            val endingPoint = globalMarketPoints.last()

            defiMarketCap = endingPoint.defiMarketCap
            defiMarketCapDiff = diff(startingPoint.defiMarketCap, defiMarketCap)

            tvl = endingPoint.tvl
            tvlDiff = diff(startingPoint.tvl, tvl)
        }

        val defiMarketCapPoints = globalMarketPoints.map { MarketMetricsPoint(it.defiMarketCap, it.timestamp) }
        val defiTvlPoints = globalMarketPoints.map { MarketMetricsPoint(it.tvl, it.timestamp) }

        return MarketMetrics(
            usdtPrice = MetricData(
                defiMarketCap?.let { formatFiatShortened(it, baseCurrency.symbol) },
                defiMarketCapDiff,
                getChartData(defiMarketCapPoints),
                MetricsType.UsdtC2C
            ),
            usdPrice = MetricData(
                tvl?.let { formatFiatShortened(it, baseCurrency.symbol) },
                tvlDiff,
                getChartData(defiTvlPoints),
                MetricsType.UsdRate
            )
        )
    }

    private fun getChartData(marketMetricsPoints: List<MarketMetricsPoint>): ChartData? {
        if (marketMetricsPoints.isEmpty()) return null

        val points = marketMetricsPoints.map { ChartPoint(it.value.toFloat(), it.timestamp) }
        return ChartData(points, true, false)
    }

    private fun formatFiatShortened(value: BigDecimal, symbol: String): String {
        return App.numberFormatter.formatFiatShort(value, symbol, 2)
    }

    private fun getSectionTitle(type: ListType): Int {
        return when (type) {
            ListType.TopGainers -> R.string.RateList_TopSales
            ListType.TopLosers -> R.string.RateList_TopBuys
        }
    }

    private fun getSectionIcon(type: ListType): Int {
        return when (type) {
            ListType.TopGainers -> R.drawable.ic_circle_up_20
            ListType.TopLosers -> R.drawable.ic_circle_down_20
        }
    }

    private fun refreshWithMinLoadingSpinnerPeriod() {
        service.refresh()
        viewModelScope.launch {
            isRefreshingLiveData.postValue(true)
            delay(1000)
            isRefreshingLiveData.postValue(false)
        }
    }

    private fun diff(sourceValue: BigDecimal, targetValue: BigDecimal): BigDecimal =
        if (sourceValue.compareTo(BigDecimal.ZERO) != 0)
            ((targetValue - sourceValue) * BigDecimal(100)) / sourceValue
        else BigDecimal.ZERO

    fun onSelectTopMarket(topMarket: TopMarket, listType: ListType) {
        when (listType) {
            ListType.TopGainers -> {
                gainersTopMarket = topMarket
                syncViewItems()
            }
            ListType.TopLosers -> {
                losersTopMarket = topMarket
                syncViewItems()
            }
        }
    }

    fun onSelectTopNftsTimeDuration(timeDuration: TimeDuration) {
        topNftsTimeDuration = timeDuration
        syncViewItems()
    }

    fun onSelectTopPlatformsTimeDuration(timeDuration: TimeDuration) {
        topPlatformsTimeDuration = timeDuration
        syncViewItems()
    }

    fun onErrorClick() {
        refreshWithMinLoadingSpinnerPeriod()
    }

    fun refresh() {
        refreshWithMinLoadingSpinnerPeriod()
    }

    fun getTopCoinsParams(listType: ListType): Triple<SortingField, TopMarket, MarketField> {
        return when (listType) {
            ListType.TopGainers -> {
                Triple(SortingField.TopSales, gainersTopMarket, MarketField.PriceDiff)
            }
            ListType.TopLosers -> {
                Triple(SortingField.TopBuys, losersTopMarket, MarketField.PriceDiff)
            }
        }
    }

    override fun onCleared() {
        service.stop()
        disposables.clear()
    }
}

val NftPrice.coinValue: CoinValue
    get() = CoinValue(token, value)
