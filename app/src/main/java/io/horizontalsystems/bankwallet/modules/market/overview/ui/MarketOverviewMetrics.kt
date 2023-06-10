package io.horizontalsystems.bankwallet.modules.market.overview.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.core.App
import io.horizontalsystems.bankwallet.modules.market.overview.MarketOverviewModule
import io.horizontalsystems.bankwallet.ui.compose.ComposeAppTheme
import io.horizontalsystems.bankwallet.ui.compose.components.caption_grey
import io.horizontalsystems.bankwallet.ui.extensions.MetricData
import java.math.BigDecimal

@Composable
fun MetricChartsView(marketMetrics: MarketOverviewModule.MarketMetrics, navController: NavController) {
    Column(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 12.dp)
    ) {
        Row {
            ChartView(marketMetrics.usdtPrice, navController)
            Spacer(Modifier.width(8.dp))
            ChartView(marketMetrics.usdPrice, navController)
        }
    }
}

@Composable
private fun RowScope.ChartView(metricsData: MetricData, navController: NavController) {
    Card(
        modifier = Modifier
            .height(105.dp)
            .weight(1f)
            .clip(RoundedCornerShape(12.dp)),

        shape = RoundedCornerShape(12.dp),
        elevation = 0.dp,
        backgroundColor = ComposeAppTheme.colors.lawrence
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            caption_grey(text = stringResource(metricsData.type.title))
            Spacer(modifier = Modifier.height(10.dp))
            if (metricsData.value != null) {
                Text(
                    text = "￥7.21", //metricsData.value,
                    style = ComposeAppTheme.typography.headline1,
                    color = ComposeAppTheme.colors.bran,
                )
            } else {
                Text(
                    text = stringResource(R.string.NotAvailable),
                    style = ComposeAppTheme.typography.headline1,
                    color = ComposeAppTheme.colors.grey50,
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(verticalAlignment = Alignment.Bottom) {
                if (metricsData.diff != null) {
                    val sign = if (metricsData.diff >= BigDecimal.ZERO) "+" else "-"
                    Text(
                        text = App.numberFormatter.format(metricsData.diff.abs(), 0, 2, sign, "%"),
                        style = ComposeAppTheme.typography.subhead1,
                        color = if (metricsData.diff >= BigDecimal.ZERO) ComposeAppTheme.colors.remus else ComposeAppTheme.colors.lucian,
                    )
                } else {
                    Text(
                        text = "----",
                        style = ComposeAppTheme.typography.subhead1,
                        color = ComposeAppTheme.colors.grey50,
                    )
                }
//                Spacer(modifier = Modifier.width(12.dp))
//                metricsData.chartData?.let { chartData ->
//                    AndroidView(
//                        modifier = Modifier
//                            .weight(1f)
//                            .padding(top = 3.dp, bottom = 6.dp)
//                            .height(24.dp),
//                        factory = {
//                            ChartMinimal(it)
//                        },
//                        update = { view ->
//                            view.doOnLayout {
//                                view.setData(chartData)
//                            }
//                        }
//                    )
//                }
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}
