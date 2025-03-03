package io.horizontalsystems.bankwallet.arcticfish.modules.order.ui

import android.content.Context
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.arcticfish.modules.order.*
import io.horizontalsystems.bankwallet.core.slideFromBottom
import io.horizontalsystems.bankwallet.core.slideFromRight
import io.horizontalsystems.bankwallet.entities.ViewState
import io.horizontalsystems.bankwallet.modules.coin.CoinLink
import io.horizontalsystems.bankwallet.modules.enablecoin.restoresettings.RestoreSettingsViewModel
import io.horizontalsystems.bankwallet.modules.enablecoin.restoresettings.ZCashConfig
import io.horizontalsystems.bankwallet.modules.managewallets.ManageWalletsModule
import io.horizontalsystems.bankwallet.modules.managewallets.ManageWalletsViewModel
import io.horizontalsystems.bankwallet.modules.markdown.MarkdownFragment
import io.horizontalsystems.bankwallet.modules.zcashconfigure.ZcashConfigure
import io.horizontalsystems.bankwallet.ui.compose.ComposeAppTheme
import io.horizontalsystems.bankwallet.ui.compose.HSSwipeRefresh
import io.horizontalsystems.bankwallet.ui.compose.components.ListErrorView
import io.horizontalsystems.bankwallet.ui.compose.components.subhead2_grey
import io.horizontalsystems.bankwallet.ui.helpers.LinkHelper
import io.horizontalsystems.core.getNavigationResult
import io.horizontalsystems.core.helpers.HudHelper
import io.horizontalsystems.marketkit.models.FullCoin
import io.horizontalsystems.marketkit.models.LinkType

@Composable
fun OrderOverviewScreen(
    fullCoin: FullCoin,
    navController: NavController
) {
    val vmFactory by lazy { OrderOverviewModule.Factory(fullCoin) }
    val viewModel = viewModel<OrderOverviewViewModel>(factory = vmFactory)

    val refreshing by viewModel.isRefreshingLiveData.observeAsState(false)
    val overview by viewModel.overviewLiveData.observeAsState()
    val viewState by viewModel.viewStateLiveData.observeAsState()

    val view = LocalView.current
    val context = LocalContext.current

    viewModel.successMessage?.let {
        HudHelper.showSuccessMessage(view, it)

        viewModel.onSuccessMessageShown()
    }

    val vmFactory1 = remember { ManageWalletsModule.Factory() }
    val manageWalletsViewModel = viewModel<ManageWalletsViewModel>(factory = vmFactory1)
    val restoreSettingsViewModel = viewModel<RestoreSettingsViewModel>(factory = vmFactory1)

    if (restoreSettingsViewModel.openZcashConfigure != null) {
        restoreSettingsViewModel.zcashConfigureOpened()

        navController.getNavigationResult(ZcashConfigure.resultBundleKey) { bundle ->
            val requestResult = bundle.getInt(ZcashConfigure.requestResultKey)

            if (requestResult == ZcashConfigure.RESULT_OK) {
                val zcashConfig = bundle.getParcelable<ZCashConfig>(ZcashConfigure.zcashConfigKey)
                zcashConfig?.let { config ->
                    restoreSettingsViewModel.onEnter(config)
                }
            } else {
                restoreSettingsViewModel.onCancelEnterBirthdayHeight()
            }
        }

        navController.slideFromBottom(R.id.zcashConfigure)
    }


    HSSwipeRefresh(
        refreshing = refreshing,
        onRefresh = {
            viewModel.refresh()
        },
        content = {
            Crossfade(viewState) { viewState ->
                when (viewState) {
                    ViewState.Loading -> {
                        Loading()
                    }
                    ViewState.Success -> {
                        overview?.let { overview ->
//                            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
//                                CoinScreenTitle(
//                                    fullCoin.coin.name,
//                                    fullCoin.coin.marketCapRank,
//                                    fullCoin.coin.imageUrl,
//                                    fullCoin.iconPlaceholder
//                                )
//
//                                viewModel.orderVariants?.let { orderVariants ->
//                                    Spacer(modifier = Modifier.height(24.dp))
//                                    OrderVariants(
//                                        orderVariants = orderVariants,
//                                        onClickAddToWallet = {
//                                            manageWalletsViewModel.enable(it)
//                                        },
//                                        onClickCopy = {
//                                            TextHelper.copyText(it)
//                                            HudHelper.showSuccessMessage(view, R.string.Hud_Text_Copied)
//                                        },
//                                        onClickExplorer = {
//                                            LinkHelper.openLinkInAppBrowser(context, it)
//                                        },
//                                    )
//                                }
//
//                                if (overview.categories.isNotEmpty()) {
//                                    Spacer(modifier = Modifier.height(24.dp))
//                                    Categories(overview.categories)
//                                }
//
//                                if (overview.about.isNotBlank()) {
//                                    Spacer(modifier = Modifier.height(24.dp))
//                                    About(overview.about)
//                                }
//
//                                Spacer(modifier = Modifier.height(32.dp))
//                                CellFooter(text = stringResource(id = R.string.Market_PoweredByApi))
//                            }
                            Column {
                                OrderStatusTitle(
                                    type = OrderType.Buy,
                                    stage = OrderStage.NeedConfirm,
                                    state = OrderState.Runing
                                )

                                TradeOrderContent(
                                    nick = "币圈小能手",
                                    level = 1,
                                    cashAmount = 1234567.00f,
                                    coinAmount = 100000.00f,
                                    price = 7.18f,
                                    number = "202308091233456",
                                    time = "2023-08-09 18:12:23",
                                    type = OrderType.Buy,
                                    payment = OrderPayment.Bank,
                                    stage = OrderStage.PaidCash,
                                    state = OrderState.Runing,
                                    {}
                                )
                            }
                        }

                    }
                    is ViewState.Error -> {
                        ListErrorView(stringResource(id = R.string.BalanceSyncError_Title)) {
                            viewModel.retry()
                        }
                    }
                    null -> {}
                }
            }
        },
    )
}

private fun onClick(coinLink: CoinLink, context: Context, navController: NavController) {
    val absoluteUrl = getAbsoluteUrl(coinLink)

    when (coinLink.linkType) {
        LinkType.Guide -> {
            val arguments = bundleOf(
                MarkdownFragment.markdownUrlKey to absoluteUrl,
                MarkdownFragment.handleRelativeUrlKey to true
            )
            navController.slideFromRight(
                R.id.markdownFragment,
                arguments
            )
        }
        else -> LinkHelper.openLinkInAppBrowser(context, absoluteUrl)
    }
}

private fun getAbsoluteUrl(coinLink: CoinLink) = when (coinLink.linkType) {
    LinkType.Twitter -> "https://twitter.com/${coinLink.url}"
    LinkType.Telegram -> "https://t.me/${coinLink.url}"
    else -> coinLink.url
}

@Preview
@Composable
fun LoadingPreview() {
    ComposeAppTheme {
        Loading()
    }
}

@Composable
fun Error(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        subhead2_grey(text = message)
    }
}

@Composable
fun Loading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(24.dp),
            color = ComposeAppTheme.colors.grey,
            strokeWidth = 2.dp
        )
    }
}
