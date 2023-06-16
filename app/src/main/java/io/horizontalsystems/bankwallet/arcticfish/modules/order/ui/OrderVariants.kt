package io.horizontalsystems.bankwallet.arcticfish.modules.order.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.entities.ConfiguredToken
import io.horizontalsystems.bankwallet.arcticfish.modules.order.OrderVariants
import io.horizontalsystems.bankwallet.ui.compose.ComposeAppTheme
import io.horizontalsystems.bankwallet.ui.compose.components.*

@Composable
fun OrderVariants(
    orderVariants: OrderVariants,
    onClickAddToWallet: (ConfiguredToken) -> Unit,
    onClickCopy: (String) -> Unit,
    onClickExplorer: (String) -> Unit,
) {
    Column {
        CellSingleLineClear(borderTop = true) {
            body_leah(text = stringResource(id = orderVariants.type.titleResId))
        }

        CellUniversalLawrenceSection(
            items = orderVariants.items,
            limit = 3
        ) { orderVariant ->
            RowUniversal(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            ) {
                Image(
                    modifier = Modifier.size(32.dp),
                    painter = rememberAsyncImagePainter(
                        model = orderVariant.imgUrl,
                        error = painterResource(R.drawable.ic_platform_placeholder_32)
                    ),
                    contentDescription = "platform"
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                ) {
                    orderVariant.name?.let {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            body_leah(
                                modifier = Modifier.weight(1f, fill = false),
                                text = it,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Spacer(Modifier.height(1.dp))
                    }
                    subhead2_grey(
                        modifier = Modifier
                            .clickable(
                                enabled = orderVariant.copyValue != null,
                                onClick = {
                                    onClickCopy.invoke(orderVariant.copyValue ?: "")
                                }
                            ),
                        text = orderVariant.value,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                if (orderVariant.canAddToWallet) {
                    if (orderVariant.inWallet) {
                        ButtonSecondaryCircle(
                            icon = R.drawable.ic_in_wallet_dark_24,
                            contentDescription = stringResource(R.string.CoinPage_InWallet),
                            enabled = false,
                            tint = ComposeAppTheme.colors.grey,
                            onClick = {
//                                HudHelper.showInProcessMessage(view, R.string.Hud_Already_In_Wallet, showProgressBar = false)
                            }
                        )
                    } else {
                        ButtonSecondaryCircle(
                            icon = R.drawable.ic_add_to_wallet_2_24,
                            contentDescription = stringResource(R.string.CoinPage_AddToWallet),
                            onClick = {
                                onClickAddToWallet.invoke(orderVariant.configuredToken)
                            }
                        )
                    }

                }
                orderVariant.explorerUrl?.let { explorerUrl ->
                    ButtonSecondaryCircle(
                        modifier = Modifier.padding(start = 16.dp),
                        icon = R.drawable.ic_globe_20,
                        contentDescription = stringResource(R.string.Button_Browser),
                        onClick = {
                            onClickExplorer.invoke(explorerUrl)
                        }
                    )
                }
            }
        }
    }
}
