package io.horizontalsystems.bankwallet.arcticfish.modules.order

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.core.*
import io.horizontalsystems.bankwallet.core.providers.Translator
import io.horizontalsystems.bankwallet.entities.*
import io.horizontalsystems.marketkit.models.FullCoin
import io.horizontalsystems.marketkit.models.TokenType
import io.reactivex.disposables.CompositeDisposable

class OrderOverviewViewModel(
    private val service: OrderOverviewService,
    private val factory: OrderViewFactory,
    private val walletManager: IWalletManager,
    private val accountManager: IAccountManager
) : ViewModel() {

    val isRefreshingLiveData = MutableLiveData<Boolean>(false)
    val overviewLiveData = MutableLiveData<OrderOverviewViewItem>()
    val viewStateLiveData = MutableLiveData<ViewState>(ViewState.Loading)

    var orderVariants by mutableStateOf<OrderVariants?>(null)
        private set
    var successMessage by mutableStateOf<Int?>(null)
        private set

    private val disposables = CompositeDisposable()

    private var fullCoin = service.fullCoin
    private var activeAccount = accountManager.activeAccount
    private var activeWallets = walletManager.activeWallets

    init {
        service.coinOverviewObservable
            .subscribeIO { coinOverview ->
                isRefreshingLiveData.postValue(false)

                coinOverview.dataOrNull?.let {
                    overviewLiveData.postValue(factory.getOverviewViewItem(it))
                }

                coinOverview.viewState?.let {
                    viewStateLiveData.postValue(it)
                }
            }
            .let {
                disposables.add(it)
            }

        service.start()

        walletManager.activeWalletsUpdatedObservable
            .subscribeIO { wallets ->
                if (wallets.size > activeWallets.size) {
                    successMessage = R.string.Hud_Added_To_Wallet
                }

                activeWallets = wallets
                refreshTokensVariants()
            }
            .let {
                disposables.add(it)
            }

        refreshTokensVariants()
    }

    fun onSuccessMessageShown() {
        successMessage = null
    }

    private fun refreshTokensVariants() {
        orderVariants = getTokenVariants(fullCoin, activeAccount, activeWallets)
    }

    override fun onCleared() {
        service.stop()
        disposables.clear()
    }

    fun refresh() {
        isRefreshingLiveData.postValue(true)
        service.refresh()
    }

    fun retry() {
        isRefreshingLiveData.postValue(true)
        service.refresh()
    }

    private fun getTokenVariants(fullCoin: FullCoin, account: Account?, activeWallets: List<Wallet>): OrderVariants? {
        val items = mutableListOf<OrderVariant>()
        var type = OrderVariants.Type.Blockchains

        val accountTypeNotWatch = if (account != null && !account.isWatchAccount) {
            account.type
        } else {
            null
        }

        fullCoin.tokens.sortedBy { it.blockchainType.order }.forEach { token ->
            val canAddToWallet = accountTypeNotWatch != null
                && token.isSupported
                && token.blockchainType.supports(accountTypeNotWatch)

            when (val tokenType = token.type) {
                is TokenType.Eip20 -> {
                    val configuredToken = ConfiguredToken(token)
                    val inWallet =
                        canAddToWallet && activeWallets.any { it.configuredToken == configuredToken }
                    items.add(
                        OrderVariant(
                            value = tokenType.address.shorten(),
                            copyValue = tokenType.address,
                            imgUrl = token.blockchainType.imageUrl,
                            explorerUrl = token.blockchain.eip20TokenUrl(tokenType.address),
                            name = token.blockchain.name,
                            configuredToken = configuredToken,
                            canAddToWallet = canAddToWallet,
                            inWallet = inWallet
                        )
                    )
                }
                is TokenType.Bep2 -> {
                    val configuredToken = ConfiguredToken(token)
                    val inWallet =
                        canAddToWallet && activeWallets.any { it.configuredToken == configuredToken }
                    items.add(
                        OrderVariant(
                            value = tokenType.symbol,
                            copyValue = tokenType.symbol,
                            imgUrl = token.blockchainType.imageUrl,
                            explorerUrl = token.blockchain.bep2TokenUrl(tokenType.symbol),
                            name = token.blockchain.name,
                            configuredToken = configuredToken,
                            canAddToWallet = canAddToWallet,
                            inWallet = inWallet
                        )
                    )
                }
                is TokenType.Spl -> {
                    val configuredToken = ConfiguredToken(token)
                    val inWallet =
                        canAddToWallet && activeWallets.any { it.configuredToken == configuredToken }
                    items.add(
                        OrderVariant(
                            value = tokenType.address.shorten(),
                            copyValue = tokenType.address,
                            imgUrl = token.blockchainType.imageUrl,
                            explorerUrl = token.blockchain.eip20TokenUrl(tokenType.address),
                            name = token.blockchain.name,
                            configuredToken = configuredToken,
                            canAddToWallet = canAddToWallet,
                            inWallet = inWallet
                        )
                    )
                }
                TokenType.Native -> when (token.blockchainType.coinSettingType) {
                    CoinSettingType.derivation -> {
                        type = OrderVariants.Type.Bips

                        AccountType.Derivation.values().forEach { derivation ->
                            val coinSettings =
                                CoinSettings(mapOf(CoinSettingType.derivation to derivation.value))
                            val configuredToken = ConfiguredToken(token, coinSettings)
                            val inWallet =
                                canAddToWallet && activeWallets.any { it.configuredToken == configuredToken }
                            items.add(
                                OrderVariant(
                                    value = derivation.addressType,
                                    copyValue = null,
                                    imgUrl = token.blockchainType.imageUrl,
                                    explorerUrl = null,
                                    name = derivation.rawName,
                                    configuredToken = configuredToken,
                                    canAddToWallet = canAddToWallet,
                                    inWallet = inWallet,
                                )
                            )
                        }
                    }
                    CoinSettingType.bitcoinCashCoinType -> {
                        type = OrderVariants.Type.CoinTypes

                        BitcoinCashCoinType.values().forEach { bchCoinType ->
                            val coinSettings =
                                CoinSettings(mapOf(CoinSettingType.bitcoinCashCoinType to bchCoinType.value))
                            val configuredToken = ConfiguredToken(token, coinSettings)
                            val inWallet =
                                canAddToWallet && activeWallets.any { it.configuredToken == configuredToken }
                            items.add(
                                OrderVariant(
                                    value = Translator.getString(bchCoinType.title),
                                    copyValue = null,
                                    imgUrl = token.blockchainType.imageUrl,
                                    explorerUrl = null,
                                    name = bchCoinType.value,
                                    configuredToken = configuredToken,
                                    canAddToWallet = canAddToWallet,
                                    inWallet = inWallet
                                )
                            )
                        }
                    }
                    null -> {
                        val configuredToken = ConfiguredToken(token)
                        val inWallet =
                            canAddToWallet && activeWallets.any { it.configuredToken == configuredToken }
                        items.add(
                            OrderVariant(
                                value = Translator.getString(R.string.CoinPlatforms_Native),
                                copyValue = null,
                                imgUrl = token.blockchainType.imageUrl,
                                explorerUrl = null,
                                name = token.blockchain.name,
                                configuredToken = configuredToken,
                                canAddToWallet = canAddToWallet,
                                inWallet = inWallet
                            )
                        )
                    }
                }
                is TokenType.Unsupported -> Unit
            }
        }

        return when {
            items.isNotEmpty() -> OrderVariants(items, type)
            else -> null
        }
    }

}

data class OrderVariants(val items: List<OrderVariant>, val type: Type) {
    enum class Type(@StringRes val titleResId: Int) {
        Blockchains(R.string.CoinPage_Blockchains),
        Bips(R.string.CoinPage_Bips),
        CoinTypes(R.string.CoinPage_CoinTypes)
    }
}
