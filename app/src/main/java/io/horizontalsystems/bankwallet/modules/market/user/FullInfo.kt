package io.horizontalsystems.bankwallet.modules.market.user

data class FullInfo(
val base: baseData,
val trade: tradeData,
val payments: List<PaymentData>
) {

    override fun toString(): String {
        return "FullInfo [ \n${base.toString()}, \n${trade.toString()}, \n" +
                "${payments.toString()} \n]"
    }

}

data class baseData(
    val uuid: String,
    val nick: String,
    val level: Int,
)

data class tradeData(
    val total: Int,
    val done: Int,
)

data class PaymentData(
    val type: Int,
    val account: String,
    val number: String,
    val cardImg: String, // 开户行名 或 支付二维码图片路径
    val realName: String,
)
