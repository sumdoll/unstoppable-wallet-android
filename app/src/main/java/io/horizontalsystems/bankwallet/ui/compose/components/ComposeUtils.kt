package io.horizontalsystems.bankwallet.ui.compose.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.core.App
import io.horizontalsystems.bankwallet.modules.market.Value
import io.horizontalsystems.bankwallet.ui.compose.ComposeAppTheme
import java.math.BigDecimal

@Composable
fun RateColor(diff: BigDecimal?) =
    if ((diff ?: BigDecimal.ZERO) >= BigDecimal.ZERO) ComposeAppTheme.colors.remus else ComposeAppTheme.colors.lucian

@Composable
fun diffColor(value: BigDecimal) =
    if (value.signum() >= 0) {
        ComposeAppTheme.colors.remus
    } else {
        ComposeAppTheme.colors.lucian
    }

@Composable
fun formatValueAsDiff(value: Value): String =
    App.numberFormatter.formatValueAsDiff(value)

@Composable
fun RateText(diff: BigDecimal?): String {
    if (diff == null) return ""
    val sign = if (diff >= BigDecimal.ZERO) "+" else "-"
    return App.numberFormatter.format(diff.abs(), 0, 2, sign, "%")
}

@Composable
fun CoinImage(
    iconUrl: String?,
    placeholder: Int? = null,
    modifier: Modifier,
    colorFilter: ColorFilter? = null
) {
    val fallback = placeholder ?: R.drawable.coin_placeholder
    when {
        iconUrl != null -> Image(
            painter = rememberAsyncImagePainter(
                model = iconUrl,
                error = painterResource(fallback)
            ),
            contentDescription = null,
            modifier = modifier,
            colorFilter = colorFilter,
            contentScale = ContentScale.FillBounds
        )
        else -> Image(
            painter = painterResource(fallback),
            contentDescription = null,
            modifier = modifier,
            colorFilter = colorFilter
        )
    }
}

@Composable
fun PayImage(
    iconPlace: Int,
    modifier: Modifier,
    colorFilter: ColorFilter? = null
) {
    var fallback = R.drawable.pay_card
    if (iconPlace == 1)
        fallback = R.drawable.pay_ali
    else if (iconPlace == 2)
        fallback = R.drawable.pay_we

    Image(
        painter = painterResource(fallback),
        contentDescription = null,
        modifier = modifier,
        colorFilter = colorFilter
    )
}

@Composable
fun NormalImage(
    img: Int,
    modifier: Modifier,
    colorFilter: ColorFilter? = null
) {
    Image(
        painter = painterResource(img),
        contentDescription = null,
        modifier = modifier,
        colorFilter = colorFilter
    )
}

@Composable
fun LevelImage(
    iconPlace: Int,
    modifier: Modifier,
    colorFilter: ColorFilter? = null
) {
    var fallback = R.drawable.coin_placeholder
    if (iconPlace == 1)
        fallback = R.drawable.coin_placeholder
    else if (iconPlace == 2)
        fallback = R.drawable.coin_placeholder
    else if (iconPlace >= 3)
        fallback = R.drawable.coin_placeholder

    Image(
        painter = painterResource(fallback),
        contentDescription = null,
        modifier = modifier,
        colorFilter = colorFilter
    )
}

@Composable
fun NftIcon(
    modifier: Modifier = Modifier,
    iconUrl: String?,
    placeholder: Int? = null,
    colorFilter: ColorFilter? = null
) {
    val fallback = placeholder ?: R.drawable.ic_platform_placeholder_24
    when {
        iconUrl != null -> Image(
            painter = rememberAsyncImagePainter(
                model = iconUrl,
                error = painterResource(fallback)
            ),
            contentDescription = null,
            modifier = modifier
                .clip(RoundedCornerShape(8.dp))
                .size(32.dp),
            colorFilter = colorFilter,
            contentScale = ContentScale.Crop
        )
        else -> Image(
            painter = painterResource(fallback),
            contentDescription = null,
            modifier = modifier.size(32.dp),
            colorFilter = colorFilter
        )
    }
}
