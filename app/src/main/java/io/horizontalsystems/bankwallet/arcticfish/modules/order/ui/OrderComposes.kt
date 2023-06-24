package io.horizontalsystems.bankwallet.arcticfish.modules.order.ui

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.arcticfish.modules.order.*
import io.horizontalsystems.bankwallet.core.providers.Translator
import io.horizontalsystems.bankwallet.ui.compose.ComposeAppTheme
import io.horizontalsystems.bankwallet.ui.compose.components.*
import io.horizontalsystems.bankwallet.ui.helpers.TextHelper
import io.horizontalsystems.core.helpers.HudHelper

@Composable
fun OrderStatusTitle(
    type: OrderType,
    stage: OrderStage,
    state: OrderState
) {
    //两行
    stateRow(type, state)
    VSpacer(2.dp)

    stageRow(type, stage)
    VSpacer(2.dp)
}

@Composable
fun stateRow(
    type: OrderType,
    state: OrderState
) {
    var reasonStr: String? = stringResource(R.string.Order_Tips_TimeNotify)

    var icon: Int? = null
    var stateStr: String? = null

    if (state == OrderState.Runing) {
        icon = R.drawable.ic_time_countdown
        stateStr = "14:16"
    } else if (state == OrderState.Appealing) {
        icon = R.drawable.ic_appealing
        stateStr = stringResource(R.string.Order_AppealDoing)
    } else if (state == OrderState.AppealSuccess) {
        icon = R.drawable.ic_smile
        stateStr = stringResource(R.string.Order_AppealSuccess)
    } else if (state == OrderState.AppealFail) {
        icon = R.drawable.ic_frown
        stateStr = stringResource(R.string.Order_AppealFailed)
    } else if (state ==  OrderState.Canceled) {
        stateStr = stringResource(R.string.Order_StatusCancel)
    } else if (state ==  OrderState.Done) {
        stateStr = stringResource(R.string.Order_StatusDone)
    }
    Row {
        Column (
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            icon?.let {
                NormalImage(
                    img = icon,
                    modifier = Modifier
                        .padding(start = 10.dp, end = 5.dp)
                        .size(40.dp)
                )
            }
        }
        Column {
            stateStr?.let {
                headline1_issykBlue(
                    text = stateStr,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            reasonStr?.let {
                subhead1_leah(
                    text = reasonStr,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
fun stageRow(
    type: OrderType,
    stage: OrderStage
) {
    var fullStage : String = if (type == OrderType.Buy) {
        stringResource(R.string.Order_SellerNick)
    } else {
        stringResource(R.string.Order_BuyerNick)
    }

    var stageStr: String? = null

    if (stage == OrderStage.NeedConfirm) {
        stageStr = stringResource(R.string.Order_BuyerStage_WaitPay)
    } else if (stage == OrderStage.PaidCash) {

    } else if (stage == OrderStage.TakeCoin) {

    } else if (stage == OrderStage.WaitCash) {

    } else if (stage == OrderStage.Timeout) {

    }
    stageStr?.let {
        LightRowRed {
            headline1_jacob(
                text = stageStr,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

//                R.drawable.ic_arrow_right
            NormalImage(
                img = R.drawable.ic_arrow_right,
                modifier = Modifier
                    .padding(start = 3.dp, end = 3.dp)
                    .size(12.dp)
            )

            subhead2_leah(
                text = stringResource(R.string.Order_BuyerStage_HavePay),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            NormalImage(
                img = R.drawable.ic_arrow_right,
                modifier = Modifier
                    .padding(start = 3.dp, end = 3.dp)
                    .size(12.dp)
            )

            subhead2_leah(
                text = stringResource(R.string.Order_BuyerStage_WaitCoin),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
fun OrderContent(
    data: OrderOverviewModule.OrderData,
    onUserClick: () -> Unit
) {
    val nick = data.userInfo.base.nick
    val level = data.userInfo.base.level
    LazyColumn {
        item {
            OrderUserRow(data.type, nick, level, onUserClick)
            Spacer(modifier = Modifier.height(10.dp))
        }

        item {
            val cashAmount = "￥ ${data.cashAmount}"
            OrderAmountRow(cashAmount, data.coinAmount, data.coinPrice)
            Spacer(modifier = Modifier.height(10.dp))
        }

        item {
            OrderInfoRow(data.orderNumber, data.orderTime)
            Spacer(modifier = Modifier.height(10.dp))
        }

        item {
            OrderTipRow(data.type)
            Spacer(modifier = Modifier.height(10.dp))
        }

        item {
            OrderPaymentRow(data.type, data.pays[0], )
            Spacer(modifier = Modifier.height(10.dp))
        }

        item {
            OrderBottom(data.type, data.stage, data.state)
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun TradeOrderContent(
    nick: String,
    level: Int,
    cashAmount: Float,
    coinAmount: Float,
    price: Float,
    number: String,
    time: String,
    type: OrderType,
    payment: OrderPayment,
    stage: OrderStage,
    state: OrderState,
    onUserClick: () -> Unit
) {
    LazyColumn {
        item {
            OrderUserRow(type, nick, level, onUserClick)
            Spacer(modifier = Modifier.height(10.dp))
        }

        item {
            val cashAmount = "￥ $cashAmount"
            OrderAmountRow(cashAmount, coinAmount, price)
            Spacer(modifier = Modifier.height(10.dp))
        }

        item {
            OrderInfoRow(number, time)
            Spacer(modifier = Modifier.height(10.dp))
        }

        item {
            OrderTipRow(type)
            Spacer(modifier = Modifier.height(10.dp))
        }

        item {
            OrderPaymentRow(type, payment)
            Spacer(modifier = Modifier.height(10.dp))
        }

        item {
            OrderBottom(type, stage, state)
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun ViewOrderState(
    nick: String,
    level: Int,
    cashAmount: Float,
    coinAmount: Float,
    price: Float,
    number: String,
    time: String,
    type: OrderType,
    payment: OrderPayment,
    onUserClick: () -> Unit
) {
    Column {
        OrderUserRow(type, nick, level, onUserClick)

        val cashAmount = "￥ $cashAmount"
        OrderAmountRow(cashAmount, coinAmount, price)

        OrderInfoRow(number, time)

        OrderPaymentRow(type, payment)
    }
}

@Composable
fun OrderUserRow(type: OrderType, nick: String, level: Int?, onClick: (() -> Unit)) {
    var nickTitle : String = if (type == OrderType.Buy) stringResource(R.string.Order_SellerNick)
    else stringResource(R.string.Order_BuyerNick)
    CellUniversalLawrenceSection(
        listOf {
            RowUniversal(
                verticalAlignment = Alignment.CenterVertically
            ) {
                subhead1_grey(
                    modifier = Modifier.weight(1f).padding(start = 10.dp),
                    text = nickTitle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                ButtonSecondaryWithIcon(
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .height(28.dp),
                    title = nick,
                    iconRight = painterResource(R.drawable.ic_arrow_right),
                    onClick = onClick
                )
            }
        }
    )
}

@Composable
fun OrderAmountRow(cashStr: String, amount: Float, price: Float) {
    CellUniversalLawrenceSection(
        listOf {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f).padding(end = 4.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    OrderAmountInfoCell(cashStr, amount, price)
                }
                Column(
                    modifier = Modifier.padding(end = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OrderTokenInfoCell()
                }
            }
        }
    )
}

@Composable
fun OrderAmountInfoCell(cashStr: String, amount: Float, price: Float) {
    CellUniversalLawrenceSection(
        listOf({
            RowUniversal(
                verticalAlignment = Alignment.CenterVertically
            ) {
                body_leah(
                    text = stringResource(R.string.Order_Cash_Amount),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(end = 10.dp)
                )

                headline2_yellow50(
                    text = cashStr,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(end = 10.dp)
                )
            }
        }, {
            RowUniversal(
                verticalAlignment = Alignment.CenterVertically
            ) {
                body_leah(
                    text = stringResource(R.string.Order_Coin_Amount),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(end = 10.dp)
                )

                subhead1_leah(
                    text = "$amount",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(end = 10.dp)
                )
            }
        }, {
            RowUniversal(
                verticalAlignment = Alignment.CenterVertically
            ) {
                body_leah(
                    text = stringResource(R.string.Order_Coin_Price),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(end = 10.dp)
                )

                subhead1_leah(
                    text = "$price",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(end = 10.dp)
                )
            }
        })
    )
}

@Composable
fun OrderTokenInfoCell() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NormalImage(
            img = R.drawable.coin_usdt,
            modifier = Modifier
                .padding(end = 6.dp)
                .size(60.dp)
        )
        body_leah(
            text = "USDT",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun OrderInfoRow(number: String, time: String) {
    val view = LocalView.current

    Column {
        CellUniversalLawrenceSection(
            listOf ({
                RowUniversal(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HSpacer(10.dp)
                    body_leah(
                        text = stringResource(R.string.Order_Number_ID),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    HSpacer(20.dp)

                    body_leah(
                        text = number,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )

                    HSpacer(8.dp)
                    ButtonSecondaryCircle(
                        icon = R.drawable.ic_copy_20,
                        onClick = {
                            TextHelper.copyText(number)
                            HudHelper.showSuccessMessage(view, R.string.Hud_Text_Copied)
                        }
                    )
                }
            }, {
                RowUniversal(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HSpacer(10.dp)
                    body_leah(
                        text = stringResource(R.string.Order_Take_Time),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    HSpacer(20.dp)

                    body_leah(
                        text = time,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            })
        )
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
            modifier = Modifier.weight(1f)
                .padding(start = 20.dp)
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OrderPaymentRow(orderType: OrderType, payType: OrderPayment) {
    val interactionSource = remember { MutableInteractionSource() }
    var showPays by remember { mutableStateOf(false) }

    val modalBottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    val onClickSwitch: (() -> Unit) = {
        showPays = true
    }

    if (showPays) {
        SelectPayDialog(
            onSuccess = {
                showPays = false
            },
            onError = {
                showPays = false
            }
        )
    }
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
        CellUniversalLawrenceSection(
            listOf ({
                RowUniversal() {
                    PayImage(
                        iconPlace = payTag,
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .size(32.dp)
                    )
                    body_leah(
                        modifier = Modifier
                            .padding(start = 5.dp)
                            .weight(1f),
                        text = stringResource(payName)
                    )
                    ButtonSecondaryWithIcon(
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .height(28.dp),
                        title = stringResource(R.string.Order_Pay_Switch),
                        iconRight = painterResource(R.drawable.ic_down_arrow_20),
                        onClick = onClickSwitch
                    )
                }
            }, {
                CopyRow(stringResource(R.string.Order_Pay_Payee), "收款名字", false)
            }, {
                CopyRow(stringResource(idName), "12345678901243", false)
            }, {
                CopyRow(stringResource(payImg), "收款名字", true)
            })
        )

        // 买单存在“卖家实名”
        if (orderType == OrderType.Buy) {
            Spacer(modifier = Modifier.height(6.dp))
            CellUniversalLawrenceSection(
                listOf {
                    RowUniversal(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        body_leah(
                            text = stringResource(R.string.Order_Pay_SellerReal),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                                .padding(start = 10.dp)
                        )

                        body_leah(
                            text = "测试实名",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(end = 10.dp)
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun CopyRow(head: String, content: String, isQr: Boolean) {
    val view = LocalView.current
    RowUniversal(
        verticalAlignment = Alignment.CenterVertically
    ) {
        HSpacer(10.dp)
        body_leah(
            text = head,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        if (isQr) {
            NormalImage(
                img = R.drawable.ic_pay_qrcode,
                modifier = Modifier.size(24.dp)
            )
            HSpacer(2.dp)
            ButtonSecondaryCircle(
                contentDescription = "查看",
                onClick = {
                    // TODO: 显示二维码界面
                    TextHelper.copyText(content)
                    HudHelper.showSuccessMessage(view, R.string.Hud_Text_Copied)
                }
            )
            HSpacer(10.dp)
        } else {
            body_leah(
                text = content,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            HSpacer(10.dp)
            ButtonSecondaryCircle(
                icon = R.drawable.ic_copy_20,
                onClick = {
                    TextHelper.copyText(content)
                    HudHelper.showSuccessMessage(view, R.string.Hud_Text_Copied)
                }
            )
            HSpacer(10.dp)
        }
    }
}

@Composable
fun OrderBottom(orderType: OrderType, stage: OrderStage, state: OrderState) {

    var tip = stringResource(R.string.Order_Tips_BuyNotice)
    var actionBtn = R.string.Order_Button_ConfirmOrder
    var appealBtn = R.string.Order_Button_Appeal

    if (orderType == OrderType.Buy) {
        if (stage == OrderStage.NeedConfirm) {
            actionBtn = R.string.Order_Button_ConfirmOrder
        } else if (stage == OrderStage.WaitCash) {
            actionBtn = R.string.Order_Button_ConfirmPay
        } else if (stage == OrderStage.Timeout) {
            actionBtn = R.string.Order_Button_Appeal
        }
        tip = Translator.getString(R.string.Order_Tips_BuyNotice,
            stringResource(R.string.Order_Button_ConfirmPay))
    } else if (orderType == OrderType.Sell) {
        if (stage == OrderStage.WaitCash || stage == OrderStage.PaidCash) {
            actionBtn = R.string.Order_Button_ConfirmCash
        }
        tip = stringResource(R.string.Order_Tips_SellNotice)
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
            TextImportantWarning(
                modifier = Modifier.padding(horizontal = 6.dp),
                text = tip
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (buyAppeal || sellAppeal) {
                HSpacer(20.dp)
                ButtonPrimaryYellow(
                    modifier = Modifier.weight(0.4f)
                        .width(120.dp),
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

            HSpacer(20.dp)

            // 付款和已收款确认按钮
            ButtonPrimaryYellow(
                modifier = Modifier.weight(0.4f)
                    .width(120.dp),
                title = stringResource(actionBtn),
                onClick = {
                    //TODO: 确认操作
                },
                enabled = true
            )
            HSpacer(20.dp)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun BuyOrderPreview() {
    ComposeAppTheme {
        Column {
            OrderStatusTitle(
                type = OrderType.Buy,
                stage = OrderStage.NeedConfirm,
                state = OrderState.Runing
            )

            TradeOrderContent(
                nick = "币圈小能手",
                level = 1,
                cashAmount = 1234.00f,
                coinAmount = 100.00f,
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

@Preview(showBackground = true)
@Composable
fun SellOrderPreview() {
    ComposeAppTheme {
        Column {
            OrderStatusTitle(
                type = OrderType.Sell,
                stage = OrderStage.NeedConfirm,
                state = OrderState.Runing
            )

            TradeOrderContent(
                nick = "币圈小能手",
                level = 1,
                cashAmount = 1234.00f,
                coinAmount = 100.00f,
                price = 7.18f,
                number = "202308091233456",
                time = "2023-08-09 18:12:23",
                type = OrderType.Sell,
                payment = OrderPayment.AliPay,
                stage = OrderStage.PaidCash,
                state = OrderState.Runing,
                {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OrderCheckPreview() {
    ComposeAppTheme {
        Column{
            OrderStatusTitle(
                type = OrderType.Sell,
                stage = OrderStage.TakeCoin,
                state = OrderState.Done
            )
            ViewOrderState(
                nick = "币圈小能手",
                level = 1,
                cashAmount = 1234.00f,
                coinAmount = 100.00f,
                price = 7.18f,
                number = "202308091233456",
                time = "2023-08-09 18:12:23",
                type = OrderType.Buy,
                payment = OrderPayment.WePay,
                {}
            )
        }
    }
}
