package io.horizontalsystems.bankwallet.ui.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.modules.market.MarketDataValue
import io.horizontalsystems.bankwallet.ui.compose.ComposeAppTheme

@Composable
fun MarketBoardOrder(
    markerName: String,
    headerIconUrl: String,
    headerIconPlaceholder: Int,
    markerLevel: Int? = null,
    limitArrays: IntArray,
    payArrays: IntArray,
    priceValue: Float,
    type: Int,
    onClick: (() -> Unit)? = null
) {
    SectionItemBorderedRowUniversalClear(
        onClick = onClick,
        borderBottom = true
    ) {
        PendingOrderItem(markerName, headerIconUrl, headerIconPlaceholder, markerLevel, limitArrays, payArrays, priceValue, type, onClick)
    }
}

@Composable
fun MarketPendingOrder(
    markerName: String,
    headerIconUrl: String,
    headerIconPlaceholder: Int,
    markerLevel: Int? = null,
    limitArrays: IntArray,
    payArrays: IntArray,
    priceValue: Float,
    type: Int,
    onClick: (() -> Unit)? = null
) {
    RowUniversal(
        modifier = Modifier
            .background(ComposeAppTheme.colors.tyler)
            .padding(horizontal = 16.dp),
        onClick = onClick
    ) {
        PendingOrderItem(markerName, headerIconUrl, headerIconPlaceholder, markerLevel, limitArrays, payArrays, priceValue, type, onClick)
    }
}

@Composable
fun PendingOrderItem(
    markerName: String,
    headerIconUrl: String,
    headerIconPlaceholder: Int,
    markerLevel: Int? = null,
    limitArrays: IntArray,
    payArrays: IntArray,
    priceValue: Float,
    type: Int,
    onClick: (() -> Unit)? = null
) {
    val onClickAble = onClick ?: {
//        try {
//            navController.slideFromBottom(
//                R.id.receiveFragment,
//                bundleOf(ReceiveFragment.WALLET_KEY to viewModel.getWalletForReceive(viewItem))
//            )
//        } catch (e: BackupRequiredError) {
//            val text = Translator.getString(
//                R.string.ManageAccount_BackupRequired_Description,
//                e.account.name,
//                e.coinTitle
//            )
//            navController.slideFromBottom(
//                R.id.backupRequiredDialog,
//                BackupRequiredDialog.prepareParams(e.account, text)
//            )
//        }
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        assert(limitArrays.size >= 4)
        var price = "￥ $priceValue"

        MarkerFirstRow(headerIconUrl, headerIconPlaceholder, markerName, markerLevel)
        Spacer(modifier = Modifier.height(3.dp))
        NumberRow(stringResource(R.string.MarketOrder_Amount), limitArrays[0], limitArrays[1], stringResource(R.string.MarketOrder_Price))
        Spacer(modifier = Modifier.height(3.dp))
        PriceRow(stringResource(R.string.MarketOrder_Limit), limitArrays[2], limitArrays[3], price)
        Spacer(modifier = Modifier.height(3.dp))
        ActionRow(payArrays, type, onClickAble)
    }
}

@Composable
fun MarkerFirstRow(headerUrl: String, iconPlaceholder: Int, title: String, level: Int?) {
    Row(
        verticalAlignment =  Alignment.Top,
    ) {
        CoinImage(
            iconUrl = headerUrl,
            placeholder = iconPlaceholder,
            modifier = Modifier
                .padding(end = 5.dp)
                .size(32.dp)
        )
        body_leah(
//            modifier = Modifier.weight(1f).padding(end = 16.dp),
            text = title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        level?.let {
            LevelImage(
                iconPlace = it,
                modifier = Modifier
                    .padding(end = 5.dp)
                    .size(20.dp)
            )
        }
    }
}

@Composable
fun NumberRow(title: String, minVol: Int, maxVol: Int, priceTile: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        subhead1_grey(
//            modifier = Modifier.weight(1f).padding(end = 6.dp),
            text = title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        subhead2_grey(
            text = " $minVol - $maxVol USDT",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
//            modifier = Modifier.weight(1f)
        )

        body_leah(
            modifier = Modifier.weight(1f).padding(end = 10.dp),
            textAlign = TextAlign.Right,
            text = priceTile,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun PriceRow(title: String, minVol: Int, maxVol: Int, price: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        subhead1_grey(
            text = title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        subhead2_grey(
            text = " $minVol - $maxVol CNY",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        body_leah(
            modifier = Modifier.weight(1f).padding(end = 10.dp),
            textAlign = TextAlign.Right,
            text = price,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun ActionRow(payIcons: IntArray, type: Int, onClick: (() -> Unit)) {
    val onClickReceive = {
//        try {
//            navController.slideFromBottom(
//                R.id.receiveFragment,
//                bundleOf(ReceiveFragment.WALLET_KEY to viewModel.getWalletForReceive(viewItem))
//            )
//        } catch (e: BackupRequiredError) {
//            val text = Translator.getString(
//                R.string.ManageAccount_BackupRequired_Description,
//                e.account.name,
//                e.coinTitle
//            )
//            navController.slideFromBottom(
//                R.id.backupRequiredDialog,
//                BackupRequiredDialog.prepareParams(e.account, text)
//            )
//        }
    }
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        payIcons.forEach() {
            PayImage(
                iconPlace = it,
                modifier = Modifier
                    .padding(end = 6.dp)
                    .size(24.dp)
            )
        }
        Spacer(modifier = Modifier.width(100.dp))

        if (type < 0) {
            // unable
            body_leah(
                modifier = Modifier.weight(1f).padding(end = 16.dp),
                text = stringResource (R.string.MarketOrder_Unable),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        } else if (type > 0) {
            // sale
            ButtonPrimaryGreen(
                modifier = Modifier.weight(0.1f),
                title = stringResource (R.string.MarketOrder_Sale),
                onClick = onClick,
            )
        } else {
            // buy
            ButtonPrimaryYellow(
                modifier = Modifier.weight(0.1f),
                title = stringResource(R.string.MarketOrder_Buy),
                onClick = onClick,
//                {
//                    navController.slideFromBottom(
//                        R.id.sendXFragment,
//                        SendFragment.prepareParams("das")
//                    )
//                },
            )
        }
    }
}

@Composable
fun MarketCoinFirstRow(title: String, rate: String?) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        body_leah(
            modifier = Modifier.weight(1f).padding(end = 16.dp),
            text = title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        rate?.let {
            body_leah(
                text = rate,
                maxLines = 1,
            )
        }
    }
}

@Composable
fun MarketCoinSecondRow(
    subtitle: String,
    marketDataValue: MarketDataValue?,
    label: String?
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        label?.let {
            Badge(
                modifier = Modifier.padding(end = 8.dp),
                text = it
            )
        }
        subhead2_grey(
            text = subtitle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        marketDataValue?.let {
            Spacer(Modifier.width(8.dp))
            MarketDataValueComponent(marketDataValue)
        }
    }
}

@Composable
fun MarketDataValueComponent(marketDataValue: MarketDataValue) {
    when (marketDataValue) {
        is MarketDataValue.MarketCap -> {
            Row {
                subhead2_grey(
                    text = marketDataValue.value,
                    maxLines = 1,
                )
            }
        }
        is MarketDataValue.Volume -> {
            Row {
                subhead2_grey(
                    text = marketDataValue.value,
                    maxLines = 1,
                )
            }
        }
        is MarketDataValue.Diff -> {
            Text(
                text = RateText(marketDataValue.value),
                color = RateColor(marketDataValue.value),
                style = ComposeAppTheme.typography.subhead2,
                maxLines = 1,
            )
        }
        is MarketDataValue.DiffNew -> {
            Text(
                text = formatValueAsDiff(marketDataValue.value),
                color = diffColor(marketDataValue.value.raw()),
                style = ComposeAppTheme.typography.subhead2,
                maxLines = 1,
            )
        }
    }
}

@Preview
@Composable
fun PreviewMarketCoin(){
//    markerName: String,
//    headerIconUrl: String,
//    headerIconPlaceholder: Int,
//    markerLevel: Int? = null,
//    limitArrays: Array<Int>,
//    payArrays: Array<Int>,
//    priceValue: Float,
//    type: Int,
    val limits = intArrayOf(1234, 45678, 2000, 10000)
    var pays = intArrayOf(0, 1, 2)
    ComposeAppTheme {
        MarketPendingOrder(
            markerName = "Marker's Name",
            headerIconUrl = "eth.png",
            headerIconPlaceholder = R.drawable.logo_ethereum_24,
            markerLevel = 1,
            limitArrays = limits,
            payArrays = pays,
            priceValue = 7.16f,
            type = 1
        )
    }
}
