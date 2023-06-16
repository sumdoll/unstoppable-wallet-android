package io.horizontalsystems.bankwallet.arcticfish.modules.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.core.BaseFragment
import io.horizontalsystems.bankwallet.arcticfish.modules.order.ui.OrderOverviewScreen
import io.horizontalsystems.bankwallet.ui.compose.ComposeAppTheme
import io.horizontalsystems.bankwallet.ui.compose.TranslatableString
import io.horizontalsystems.bankwallet.ui.compose.components.*
import io.horizontalsystems.core.helpers.HudHelper

class OrderFragment : BaseFragment() {

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
                val uid = try {
                    activity?.intent?.data?.getQueryParameter("uid")
                } catch (e: UnsupportedOperationException) {
                    null
                }

                val orderUid = requireArguments().getString(ORDER_UID_KEY, uid ?: "")
                if (uid != null) {
                    activity?.intent?.data = null
                }

                OrderScreen(
                    orderUid,
                    coinViewModel(orderUid),
                    findNavController()
                )
            }
        }
    }

    private fun coinViewModel(coinUid: String): OrderViewModel? = try {
        val viewModel by navGraphViewModels<OrderViewModel>(R.id.orderFragment) {
            OrderModule.Factory(coinUid)
        }
        viewModel
    } catch (e: Exception) {
        null
    }

    companion object {
        private const val ORDER_UID_KEY = "order_uid_key"

        fun prepareParams(coinUid: String) = bundleOf(ORDER_UID_KEY to coinUid)
    }
}

@Composable
fun OrderScreen(
    orderUid: String,
    orderViewModel: OrderViewModel?,
    navController: NavController
) {
    ComposeAppTheme {
        if (orderViewModel != null) {
            OrderDetails(orderViewModel, navController)
        } else {
            OrderNotFound(orderUid, navController)
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OrderDetails(
    viewModel: OrderViewModel,
    navController: NavController
) {
    val pagerState = rememberPagerState(initialPage = 0)
    val coroutineScope = rememberCoroutineScope()
    val view = LocalView.current

    Column(modifier = Modifier.background(color = ComposeAppTheme.colors.tyler)) {
        AppBar(
            title = TranslatableString.PlainString(viewModel.fullCoin.coin.code),
            navigationIcon = {
                HsBackButton(onClick = { navController.popBackStack() })
            },
            menuItems = buildList {
                if (viewModel.isWatchlistEnabled) {
                    if (viewModel.isFavorite) {
                        add(
                            MenuItem(
                                title = TranslatableString.ResString(R.string.CoinPage_Unfavorite),
                                icon = R.drawable.ic_filled_star_24,
                                tint = ComposeAppTheme.colors.jacob,
                                onClick = { viewModel.onUnfavoriteClick() }
                            )
                        )
                    } else {
                        add(
                            MenuItem(
                                title = TranslatableString.ResString(R.string.CoinPage_Favorite),
                                icon = R.drawable.ic_star_24,
                                onClick = { viewModel.onFavoriteClick() }
                            )
                        )
                    }
                }
            }
        )

        HorizontalPager(
            count = 1,
            state = pagerState,
            userScrollEnabled = false
        ) {
            OrderOverviewScreen(
                fullCoin = viewModel.fullCoin,
                navController = navController
            )
        }

        viewModel.successMessage?.let {
            HudHelper.showSuccessMessage(view, it)

            viewModel.onSuccessMessageShown()
        }
    }
}

@Composable
fun OrderNotFound(orderUid: String, navController: NavController) {
    Column(modifier = Modifier.background(color = ComposeAppTheme.colors.tyler)) {
        AppBar(
            title = TranslatableString.PlainString(orderUid),
            navigationIcon = {
                HsBackButton(onClick = { navController.popBackStack() })
            }
        )

        ListEmptyView(
            text = stringResource(R.string.CoinPage_CoinNotFound, orderUid),
            icon = R.drawable.ic_not_available
        )

    }
}
