package io.horizontalsystems.bankwallet.arcticfish.modules.user

import io.horizontalsystems.bankwallet.core.Clearable
import io.horizontalsystems.bankwallet.core.managers.MarketFavoritesManager
import io.horizontalsystems.marketkit.models.FullCoin
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

// 获取订单阶段和状态
class UserLoginService(
) : Clearable {

    private val _doCancel = BehaviorSubject.create<Boolean>()
    val isCanceling: Observable<Boolean>
        get() = _doCancel

    private val disposables = CompositeDisposable()

    init {
        // 初始化，定时获取订单当前阶段，状态等信息并更新UI
    }

    override fun clear() {
        disposables.clear()
    }
}
