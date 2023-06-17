package io.horizontalsystems.bankwallet.arcticfish.modules.appeal

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
import com.google.accompanist.pager.rememberPagerState
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.arcticfish.modules.appeal.ui.SubmitAppeal
import io.horizontalsystems.bankwallet.core.BaseFragment
import io.horizontalsystems.bankwallet.ui.compose.ComposeAppTheme
import io.horizontalsystems.bankwallet.ui.compose.TranslatableString
import io.horizontalsystems.bankwallet.ui.compose.components.*

class AppealFragment : BaseFragment() {

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

                AppealScreen(
                    orderUid,
                    appealViewModel(orderUid),
                    findNavController()
                )
            }
        }
    }

    private fun appealViewModel(coinUid: String): AppealViewModel? = try {
        val viewModel by navGraphViewModels<AppealViewModel>(R.id.orderFragment) {
            AppealModule.Factory(coinUid)
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
fun AppealScreen(
    orderUid: String,
    viewModel: AppealViewModel?,
    navController: NavController
) {
    ComposeAppTheme {
        if (viewModel != null) {
            AppealDetails(viewModel, navController)
        } else {
            AppealNotFound(orderUid, navController)
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AppealDetails(
    viewModel: AppealViewModel,
    navController: NavController
) {
    val pagerState = rememberPagerState(initialPage = 0)
    val coroutineScope = rememberCoroutineScope()
    val view = LocalView.current

    SubmitAppeal(navController = navController)

//    Column(modifier = Modifier.background(color = ComposeAppTheme.colors.tyler)) {
//        AppBar(
//            title = TranslatableString.PlainString(viewModel.fullCoin.coin.code),
//            navigationIcon = {
//                HsBackButton(onClick = { navController.popBackStack() })
//            },
//            menuItems = buildList {
//                if (viewModel.isBuyCancelEnabled) {
//                    add(
//                        MenuItem(
//                            title = TranslatableString.ResString(R.string.Order_Cancel),
//                            tint = ComposeAppTheme.colors.jacob,
//                            onClick = {
//                                // TODO: 显示取消确定框，确定之后直接back
////                                navController.popBackStack()
//                                viewModel.onCancelClick()
//                            }
//                        )
//                    )
//                }
//            }
//        )
//
//        HorizontalPager(
//            count = 1,
//            state = pagerState,
//            userScrollEnabled = false
//        ) {
//            OrderOverviewScreen(
//                fullCoin = viewModel.fullCoin,
//                navController = navController
//            )
//        }
//
//        viewModel.successMessage?.let {
//            HudHelper.showSuccessMessage(view, it)
//
//            viewModel.onSuccessMessageShown()
//        }
//    }
}

@Composable
fun AppealNotFound(orderUid: String, navController: NavController) {
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
