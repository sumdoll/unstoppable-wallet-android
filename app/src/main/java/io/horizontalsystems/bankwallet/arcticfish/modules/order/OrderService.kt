package io.horizontalsystems.bankwallet.arcticfish.modules.order

import io.horizontalsystems.bankwallet.core.Clearable
import io.horizontalsystems.bankwallet.core.managers.MarketFavoritesManager
import io.horizontalsystems.marketkit.models.FullCoin
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

// 获取订单阶段和状态
class OrderService(
    val fullCoin: FullCoin,
    private val marketFavoritesManager: MarketFavoritesManager,
) : Clearable {

    private val _isFavorite = BehaviorSubject.create<Boolean>()
    val isFavorite: Observable<Boolean>
        get() = _isFavorite

    private val disposables = CompositeDisposable()

    init {
        emitIsFavorite()
    }

    override fun clear() {
        disposables.clear()
    }

    fun favorite() {
        marketFavoritesManager.add(fullCoin.coin.uid)

        emitIsFavorite()
    }

    fun unfavorite() {
        marketFavoritesManager.remove(fullCoin.coin.uid)

        emitIsFavorite()
    }

    private fun emitIsFavorite() {
        _isFavorite.onNext(marketFavoritesManager.isCoinInFavorites(fullCoin.coin.uid))
    }
}
