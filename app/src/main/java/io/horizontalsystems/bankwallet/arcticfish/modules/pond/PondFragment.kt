package io.horizontalsystems.bankwallet.arcticfish.modules.pond

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

class PondFragment : BaseFragment() {

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

                val reqUid = requireArguments().getString(ORDER_UID_KEY, uid ?: "")
                if (uid != null) {
                    activity?.intent?.data = null
                }

                PondScreen(
                    reqUid,
                    pondViewModel(reqUid),
                    findNavController()
                )
            }
        }
    }

    private fun pondViewModel(coinUid: String): PondViewModel? = try {
        val viewModel by navGraphViewModels<PondViewModel>(R.id.orderFragment) {
            PondModule.Factory(coinUid)
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
fun PondScreen(
    reqUid: String,
    viewModel: PondViewModel?,
    navController: NavController
) {
    ComposeAppTheme {
        if (viewModel != null) {
            PondDetails(viewModel, navController)
        } else {
            PondNotFound(reqUid, navController)
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PondDetails(
    viewModel: PondViewModel,
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
fun PondNotFound(reqUid: String, navController: NavController) {
    Column(modifier = Modifier.background(color = ComposeAppTheme.colors.tyler)) {
        AppBar(
            title = TranslatableString.PlainString(reqUid),
            navigationIcon = {
                HsBackButton(onClick = { navController.popBackStack() })
            }
        )

        ListEmptyView(
            text = stringResource(R.string.CoinPage_CoinNotFound, reqUid),
            icon = R.drawable.ic_not_available
        )

    }
}
