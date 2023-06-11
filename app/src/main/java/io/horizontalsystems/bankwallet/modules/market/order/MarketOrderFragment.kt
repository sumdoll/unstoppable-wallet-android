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
fun OrderTitle(
    titleText: String,
    onRightCancelClick: (() -> Unit)? = null,
    leftIcon: Int,
    onBackButtonClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    var searchText by rememberSaveable { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onBackButtonClick.invoke()
            }
        ) {
            Icon(
                painter = painterResource(id = leftIcon),
                contentDescription = "back icon",
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 16.dp)
                    .size(24.dp),
                tint = ComposeAppTheme.colors.jacob
            )
        }
        subhead1_leah(
            modifier = Modifier.weight(1f),
            text = titleText,
            maxLines = 1
        )
        onRightCancelClick?.let {
            Box(
                modifier = Modifier.clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    onRightCancelClick.invoke()
                }
            ) {
                headline2_jacob(
                    text = stringResource(R.string.Market_Filters),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }

}

@Composable
fun OrderContent(
    data: MarketOrderModule.OrderData,
    onUserClick: () -> Unit
) {
//    Column(
//        modifier = Modifier.fillMaxWidth()
//    ) {
//
//        var cashAmount = "￥ ${data.cashAmount}"
//
//        MarkerFirstRow(headerIconUrl, headerIconPlaceholder, markerName, markerLevel)
//        Spacer(modifier = Modifier.height(3.dp))
//        NumberRow(stringResource(R.string.MarketOrder_Amount), limitArrays[0], limitArrays[1], stringResource(R.string.MarketOrder_Price))
//        Spacer(modifier = Modifier.height(3.dp))
//        PriceRow(stringResource(R.string.MarketOrder_Limit), limitArrays[2], limitArrays[3], price)
//        Spacer(modifier = Modifier.height(3.dp))
//        ActionRow(payArrays, type, onClickAble)
//    }
    val nick = data.userInfo.base.nick
    val level = data.userInfo.base.level
    LazyColumn {
        item {
            UserRow(data.type, nick, level, onUserClick)
        }

        item {
            val cashAmount = "￥ ${data.cashAmount}"
            ContentRow(cashAmount, data.coinAmount, data.coinPrice)
        }

        item {
            val cashAmount = "￥ ${data.cashAmount}"
            OrderInfoRow(data.orderNumber, data.orderTime)
        }

        item {
            OrderTipRow(data.type)
        }

        item {
            OrderPaymentRow(data.type, data.pays[0], )
        }

        item {
            OrderBottom(data.type, data.stage, data.state)
        }
    }
}

@Composable
fun UserRow(type: OrderType, nick: String, level: Int?, onClick: (() -> Unit)) {
    var nickTitle : String = if (type == OrderType.Buy) stringResource(R.string.Order_SellerNick)
    else stringResource(R.string.Order_BuyerNick)
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        subhead1_grey(
//            modifier = Modifier.weight(1f).padding(end = 6.dp),
            text = nickTitle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        body_leah(
            text = nick,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
//            modifier = Modifier.weight(1f)
        )

        level?.let {
            LevelImage(
                iconPlace = it,
                modifier = Modifier
                    .padding(end = 5.dp)
                    .size(20.dp)
            )
        }

        NormalImage(
            img = R.drawable.ic_arrow_right,
            modifier = Modifier
                .padding(end = 6.dp)
                .size(24.dp)
        )
    }
}

@Composable
fun ContentRow(cashStr: String, amount: Float, price: Float) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
//        modifier = Modifier.weight(1f).padding(end = 6.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Row {
                body_leah(
                    modifier = Modifier.weight(1f).padding(end = 6.dp),
                    text = stringResource(R.string.Order_Cash_Amount),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                subhead1_yellow50(
                    text = cashStr,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
//            modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.height(4.dp))
            }

            Row {
                body_leah(
//            modifier = Modifier.weight(1f).padding(end = 6.dp),
                    text = stringResource(R.string.Order_Coin_Amount),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                subhead1_yellow50(
                    text = "$amount",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
//            modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.height(4.dp))
            }

            Row {
                body_leah(
//            modifier = Modifier.weight(1f).padding(end = 6.dp),
                    text = stringResource(R.string.Order_Coin_Price),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                subhead1_yellow50(
                    text = "$price",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
//            modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.height(4.dp))
            }
        }

        Column(
            verticalArrangement = Arrangement.Center
        ) {
            PayImage(
                iconPlace = R.drawable.coin_usdt,
                modifier = Modifier
                    .padding(end = 6.dp)
                    .size(60.dp)
            )
            body_leah(
//            modifier = Modifier.weight(1f).padding(end = 6.dp),
                text = "USDT",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun OrderInfoRow(number: String, time: String) {
    val view = LocalView.current

    Column {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            body_leah(
                text = stringResource(R.string.Order_Number_ID),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
//            modifier = Modifier.weight(1f)
            )

            body_leah(
                text = number,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
//            modifier = Modifier.weight(1f)
            )

            HSpacer(16.dp)
            ButtonSecondaryCircle(
                icon = R.drawable.ic_copy_20,
                onClick = {
                    TextHelper.copyText(number)
                    HudHelper.showSuccessMessage(view, R.string.Hud_Text_Copied)
                }
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            body_leah(
                text = stringResource(R.string.Order_Take_Time),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
//            modifier = Modifier.weight(1f)
            )

            body_leah(
                text = time,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
//            modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun OrderTipRow(type: OrderType) {
    var tip : String =
        if (type == OrderType.Buy)
            stringResource(R.string.Order_Tips_MyPayway)
        else
            stringResource(R.string.Order_Tips_PayCash)

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        body_leah(
            text = tip,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
//            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun OrderPaymentRow(orderType: OrderType, payType: OrderPayment) {

    var payName = R.string.Order_Payway_Bank
    var idName = R.string.Order_Pay_BankID
    var payImg = R.string.Order_Pay_BankName
    var payTag = 0
    if (payType == OrderPayment.AliPay) {
        payName = R.string.Order_Payway_Ali
        idName = R.string.Order_Pay_AliID
        payImg = R.string.Order_Pay_Code
        payTag = 1
    } else if (payType == OrderPayment.WePay) {
        payName = R.string.Order_Payway_We
        idName = R.string.Order_Pay_WeID
        payImg = R.string.Order_Pay_Code
        payTag = 2
    }

    var realName = R.string.Order_Pay_SellerReal

    Column {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            PayImage(
                iconPlace = payTag,
                modifier = Modifier
                    .padding(end = 2.dp)
                    .size(40.dp)
            )
            headline2_leah(
                text = stringResource(payName),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            body_leah(
                text = stringResource(R.string.Order_Pay_Switch),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
//            modifier = Modifier.weight(1f)
            )

            if (orderType == OrderType.Buy) {
                body_leah(
                    text = stringResource(R.string.Order_Pay_Switch),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
//            modifier = Modifier.weight(1f)
                )

                NormalImage(
                    img = R.drawable.ic_arrow_right,
                    modifier = Modifier
                        .padding(end = 6.dp)
                        .size(24.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        CopyRow(stringResource(R.string.Order_Pay_Payee), "收款名字", false)
        Spacer(modifier = Modifier.height(6.dp))
        CopyRow(stringResource(idName), "12345678901243", false)
        Spacer(modifier = Modifier.height(6.dp))
        CopyRow(stringResource(payImg), "收款名字", true)

        if (orderType == OrderType.Buy) {
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                body_leah(
                    text = stringResource(R.string.Order_Pay_SellerReal),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
    //            modifier = Modifier.weight(1f)
                )

                body_leah(
                    text = "测试实名",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
    //            modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun CopyRow(head: String, content: String, isQr: Boolean) {
    val view = LocalView.current
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        body_leah(
            text = head,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
//            modifier = Modifier.weight(1f)
        )
        if (isQr) {
            NormalImage(
                img = R.drawable.ic_pay_qrcode,
                modifier = Modifier
                    .padding(end = 6.dp)
                    .size(24.dp)
            )
            HSpacer(16.dp)
            ButtonSecondaryCircle(
//                icon = R.drawable.ic_copy_20,
                contentDescription = "查看",
                onClick = {
                    // TODO: 显示二维码界面
                    TextHelper.copyText(content)
                    HudHelper.showSuccessMessage(view, R.string.Hud_Text_Copied)
                }
            )
        } else {
            body_leah(
                text = content,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
//            modifier = Modifier.weight(1f)
            )
            HSpacer(16.dp)
            ButtonSecondaryCircle(
                icon = R.drawable.ic_copy_20,
                onClick = {
                    TextHelper.copyText(content)
                    HudHelper.showSuccessMessage(view, R.string.Hud_Text_Copied)
                }
            )
        }
    }
}

@Composable
fun OrderBottom(orderType: OrderType, stage: OrderStage, state: OrderState) {

    var tip = R.string.Order_Tips_BuyNotice
    var actionBtn = R.string.Order_Button_ConfirmOrder
    var appealBtn = R.string.Order_Button_Appeal

    if (orderType == OrderType.Buy) {
        tip = R.string.Order_Tips_BuyNotice
        if (stage == OrderStage.NeedConfirm) {
            actionBtn = R.string.Order_Button_ConfirmOrder
        } else if (stage == OrderStage.WaitCash) {
            actionBtn = R.string.Order_Button_ConfirmPay
        } else if (stage == OrderStage.Timeout) {
            actionBtn = R.string.Order_Button_Appeal
        }
    } else if (orderType == OrderType.Sell) {
        tip = R.string.Order_Tips_SellNotice
        if (stage == OrderStage.WaitCash || stage == OrderStage.PaidCash) {
            actionBtn = R.string.Order_Button_ConfirmCash
        }
    }

    var buyAppeal : Boolean = false
    var sellAppeal : Boolean = false
    if (orderType == OrderType.Buy && stage == OrderStage.Timeout) {
        buyAppeal = true
    }
    if (orderType == OrderType.Sell && (stage == OrderStage.PaidCash
                || stage == OrderStage.Timeout)) {
        sellAppeal = true
    }

    if (state == OrderState.Appealing) {
        appealBtn = R.string.Order_Button_Appealing
    } else {
        appealBtn = R.string.Order_Button_Appealed
    }

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            body_green50(
                text = stringResource(tip),
                overflow = TextOverflow.Ellipsis,
                //            modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (buyAppeal || sellAppeal) {
                ButtonPrimaryYellow(
                    modifier = Modifier.weight(1f),
                    title = stringResource(appealBtn),
                    onClick = {
//                        navController.slideFromBottom(
//                            R.id.sendXFragment,
//                            SendFragment.prepareParams(viewItem.wallet)
                        //TODO: 申诉申请
//                        )
                    },
                    enabled = true
                )
            }

            // 付款和已收款确认按钮
            ButtonPrimaryYellow(
                modifier = Modifier.weight(1f),
                title = stringResource(actionBtn),
                onClick = {
                    //TODO: 确认操作
                },
                enabled = true
            )
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

@Preview(showBackground = true)
@Composable
fun BuyOrderPreview() {
    val coin = Coin("ether", "Ethereum", "ETH")
    ComposeAppTheme {
        MarketCoin(
            coin.code,
            coin.name,
            coin.imageUrl,
            R.drawable.coin_placeholder,
            false,
            {},
            {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SellOrderPreview() {
    val coin = Coin("ether", "Ethereum", "ETH")
    ComposeAppTheme {
        MarketCoin(
            coin.code,
            coin.name,
            coin.imageUrl,
            R.drawable.coin_placeholder,
            false,
            {},
            {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TitlePreview() {
    ComposeAppTheme {
        OrderTitle(
            titleText = stringResource(R.string.Order_TitleBuy),
            onRightCancelClick = { },
            leftIcon = R.drawable.ic_back,
            onBackButtonClick = { }
        )
    }
}
