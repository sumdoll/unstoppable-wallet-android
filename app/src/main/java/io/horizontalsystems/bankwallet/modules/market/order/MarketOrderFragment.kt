package io.horizontalsystems.bankwallet.modules.market.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.core.*
import io.horizontalsystems.bankwallet.entities.ViewState
import io.horizontalsystems.bankwallet.modules.coin.overview.ui.Loading
import io.horizontalsystems.bankwallet.modules.market.MarketDataValue
import io.horizontalsystems.bankwallet.modules.market.TimeDuration
import io.horizontalsystems.bankwallet.ui.compose.ComposeAppTheme
import io.horizontalsystems.bankwallet.ui.compose.Select
import io.horizontalsystems.bankwallet.ui.compose.components.*
import io.horizontalsystems.bankwallet.ui.helpers.TextHelper
import io.horizontalsystems.core.findNavController
import io.horizontalsystems.core.helpers.HudHelper
import io.horizontalsystems.marketkit.models.Coin

class MarketOrderFragment : BaseFragment() {

    private val viewModel by viewModels<MarketOrderViewModel> { MarketOrderModule.Factory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner)
            )

            setContent {
                MarketOrderScreen(
                    viewModel,
                    findNavController(),
                )
            }
        }
    }

}

@Composable
fun MarketOrderScreen(
    viewModel: MarketOrderViewModel,
    navController: NavController,
) {

    val viewState = viewModel.viewState
    val errorMessage = viewModel.errorMessage
//    val itemsData = viewModel.itemsData
    val orderType = 1 // : Int = itemsData.

    var title : String = if (orderType == 1) stringResource(R.string.Order_TitleBuy)
                else stringResource(R.string.Order_TitleSale)
    ComposeAppTheme {
        Column(modifier = Modifier.background(color = ComposeAppTheme.colors.tyler)) {
            OrderTitle(
                titleText = title,
                onRightCancelClick = {
                    //TODO: 取消订单对话框
                    navController.slideFromRight(R.id.marketAdvancedSearchFragment)
                },
                leftIcon = R.drawable.ic_back,
                onBackButtonClick = { navController.popBackStack() }
            )
            Crossfade(viewState) { viewState ->
                when (viewState) {
                    ViewState.Loading -> {
                        Loading()
                    }
                    is ViewState.Error -> {
                        ListErrorView(stringResource(R.string.SyncError), viewModel::refresh)
                    }
                    ViewState.Success -> {
                        when (val itemsData = viewModel.itemsData) {
                            is MarketOrderModule.Data.MetaData -> {
                                OrderContent(
                                    data = itemsData.orderData,
                                    onUserClick = {
                                        //TODO: 对手信息框
                                        navController.slideFromRight(R.id.marketAdvancedSearchFragment)
                                    }
                                )
                            }
                            null -> {}
                        }
                    }
                }
            }
        }

        errorMessage?.let {
            SnackbarError(it.getString())
            viewModel.errorShown()
        }
    }
}

@Composable
private fun MarketCoin(
    coinCode: String,
    coinName: String,
    coinIconUrl: String,
    coinIconPlaceholder: Int,
    favorited: Boolean,
    onFavoriteClick: () -> Unit,
    onClick: () -> Unit,
    coinRate: String? = null,
    marketDataValue: MarketDataValue? = null,
) {

    SectionItemBorderedRowUniversalClear(
        borderBottom = true,
        onClick = onClick
    ) {
        CoinImage(
            iconUrl = coinIconUrl,
            placeholder = coinIconPlaceholder,
            modifier = Modifier
                .padding(end = 16.dp)
                .size(32.dp)
        )
        Column(
            modifier = Modifier.weight(1f)
        ) {
            MarketCoinFirstRow(coinCode, coinRate)
            Spacer(modifier = Modifier.height(3.dp))
            MarketCoinSecondRow(coinName, marketDataValue, null)
        }

        HsIconButton(onClick = onFavoriteClick) {
            Icon(
                painter = painterResource(if (favorited) R.drawable.ic_star_filled_20 else R.drawable.ic_star_20),
                contentDescription = "coin icon",
                tint = if (favorited) ComposeAppTheme.colors.jacob else ComposeAppTheme.colors.grey
            )
        }
    }
}
