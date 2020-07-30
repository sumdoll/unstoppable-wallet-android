package io.horizontalsystems.bankwallet.modules.ratechart

import java.math.BigDecimal

object CoinInfoMap {
    
    val data = mapOf(
            "BTC" to CoinInfo(BigDecimal(21_000_000), "03/01/2009", "https://bitcoin.org/en"),
            "LTC" to CoinInfo(BigDecimal(84_000_000),  "13/10/2011", "https://litecoin.com"),
            "ETH" to CoinInfo( null,  "30/07/2015", "https://www.ethereum.org"),
            "BCH" to CoinInfo(BigDecimal(21_000_000), "01/08/2017", "https://www.bitcoincash.org"),
            "DASH" to CoinInfo(BigDecimal(18_900_000), "18/01/2014", "http://dash.org"),
            "BNB" to CoinInfo(BigDecimal(187_536_713), "27/06/2017", "https://www.binance.com"),
            "EOS" to CoinInfo(BigDecimal(1_035_000_004), "26/06/2017", "https://eos.io"),
            "ZRX" to CoinInfo(BigDecimal(1_000_000_000), "15/08/2017", "https://www.0xproject.com"),
            "ELF" to CoinInfo(BigDecimal(880_000_000), "18/12/2017", "http://aelf.io"),
            "AMPL" to CoinInfo(BigDecimal(38_207_563), "14/06/2019", "https://www.ampleforth.org"),
            "ANKR" to CoinInfo(BigDecimal(10_000_000_000), "21/02/2019", "https://www.ankr.com"),
            "ANT" to CoinInfo(BigDecimal(39_609_524), "05/05/2017", "https://aragon.one"),
            "BNT" to CoinInfo( null,  "13/02/2017", "https://bancor.network"),
            "BAT" to CoinInfo(BigDecimal(1_500_000_000), "31/05/2017", "https://basicattentiontoken.org"),
            "BUSD" to CoinInfo(BigDecimal(28_603_822), "10/09/2019", "https://www.paxos.com/busd"),
            "BTCB" to CoinInfo(BigDecimal(9_001), "17/06/2019", "https://bitcoin.org/en"),
            "CAS" to CoinInfo(BigDecimal(1_000_000_000), "12/10/2017", "https://cashaa.com"),
            "LINK" to CoinInfo(BigDecimal(1_000_000_000), "19/09/2017", "https://link.smartcontract.com"),
            "CVC" to CoinInfo(BigDecimal(1_000_000_000), "21/06/2017", "https://www.civic.com"),
            "COMP" to CoinInfo(BigDecimal(10_000_000), "04/03/2020", "https://compound.finance"),
            "CRPT" to CoinInfo(BigDecimal(99_785_291), "28/09/2017", "https://crypterium.io"),
            "MCO" to CoinInfo(BigDecimal(31_587_682), "18/05/2017", "https://crypto.com/en/index.html"),
            "CRO" to CoinInfo(BigDecimal(100_000_000_000), "14/11/2019", "https://www.crypto.com/en/chain"),
            "DAI" to CoinInfo(BigDecimal(127_759_558), "18/11/2019", "https://makerdao.com/en"),
            "MANA" to CoinInfo(BigDecimal(2_644_403_343), "08/08/2017", "https://decentraland.org"),
            "DGD" to CoinInfo(BigDecimal(2_000_000), "28/04/2016", "https://www.dgx.io"),
            "DGX" to CoinInfo(BigDecimal(120_600), "29/03/2018", "https://digix.global/dgx#"),
            "DNT" to CoinInfo(BigDecimal(1_000_000_000), "08/08/2017", "https://district0x.io"),
            "ENJ" to CoinInfo(BigDecimal(1_000_000_000), "24/07/2017", "https://enjincoin.io"),
            "EOSDT" to CoinInfo(BigDecimal(170_000_000), "31/05/2019", "https://eosdt.com/en"),
            "IQ" to CoinInfo(BigDecimal(10_006_128_771), "14/07/2018", "https://everipedia.org"),
            "GUSD" to CoinInfo( null, "09/09/2018", "https://gemini.com/dollar"),
            "GTO" to CoinInfo(BigDecimal(1_000_000_000), "14/12/2017", "https://gifto.io"),
            "GNT" to CoinInfo(BigDecimal(1_000_000_000), "17/11/2016", "https://golem.network"),
            "HOT" to CoinInfo(BigDecimal(177_619_433_541), "16/01/2018", "https://thehydrofoundation.com"),
            "HT" to CoinInfo(BigDecimal(500_000_000), "22/01/2018", "https://www.huobi.pro"),
            "IDXM" to CoinInfo( null, null, "https://idex.market/eth/idex"),
            "IDEX" to CoinInfo(BigDecimal(1_000_000_000), "28/09/2017", "https://auroradao.com"),
            "KCS" to CoinInfo(BigDecimal(176_863_551), "15/09/2017", "https://www.kucoin.com"),
            "KNC" to CoinInfo(BigDecimal(210_590_429), "20/09/2017", "https://kyber.network"),
            "LOOM" to CoinInfo(BigDecimal(1_000_000_000), "03/03/2018", "https://loomx.io"),
            "LRC" to CoinInfo(BigDecimal(1_374_513_897), "06/08/2017", "https://loopring.org"),
            "MKR" to CoinInfo(BigDecimal(1_000_000), "15/08/2015", null),
            "MEETONE" to CoinInfo(BigDecimal(10_000_000_000), "05/05/2018", "https://meet.one"),
            "MITH" to CoinInfo(BigDecimal(1_000_000_000), "12/03/2018", "https://mith.io"),
            "NUT" to CoinInfo(BigDecimal(10_000_000), "26/02/2019", "https://mith.io"),
            "NDX" to CoinInfo(BigDecimal(10_000_000_000), null, null),
            "NEXO" to CoinInfo(BigDecimal(1_000_000_000), "29/04/2018", "https://omg.network"),
            "OMG" to CoinInfo( null,"23/06/2017", "https://nexo.io") ,
            "ORBS" to CoinInfo(BigDecimal(10_000_000_000), "14/03/2018", "https://www.orbs.com"),
            "OXT" to CoinInfo(BigDecimal(10_000_000_000), "03/12/2019", "https://www.orchid.com"),
            "PAX" to CoinInfo(BigDecimal(249_952_065), "10/09/2018", "https://www.paxos.com/pax"),
            "PAXG" to CoinInfo(BigDecimal(2_410), "29/08/2019", "https://www.paxos.com/paxgold"),
            "PTI" to CoinInfo(BigDecimal(3_600_000_000), "13/03/2018", "https://tokensale.paytomat.com"),
            "POLY" to CoinInfo(BigDecimal(1_000_000_000), "25/12/2017", "https://www.polymath.network"),
            "PPT" to CoinInfo(BigDecimal(53_252_246), "12/04/2017", "https://populous.world"),
            "PGL" to CoinInfo(BigDecimal(220_000_000), "19/04/2017", "https://prospectors.io/en"),
            "NPXS" to CoinInfo(BigDecimal(259_810_708_833), "27/09/2017", "https://pundix.com"),
            "REN" to CoinInfo( BigDecimal(999_999_633), "31/12/2017", "https://renproject.io"),
            "RENBCH" to CoinInfo( null, null, "https://renproject.io"),
            "RENBTC" to CoinInfo( null, null, "https://renproject.io"),
            "RENZEC" to CoinInfo( null, null, "https://renproject.io"),
            "REP" to CoinInfo( null, "17/11/2014", "https://www.augur.net"),
            "R" to CoinInfo(BigDecimal(1_000_000_000), "04/08/2017", "http://revain.org"),
            "XRP" to CoinInfo(BigDecimal(99_990_932_726), "02/02/2013", "https://ripple.com/xrp"),
            "SAI" to CoinInfo( null, "18/12/2017", "https://makerdao.com/en"),
            "SNX" to CoinInfo(BigDecimal(190_075_446), "07/01/2018", "https://www.synthetix.io"),
            "EURS" to CoinInfo( null, "22/06/2018", "https://stasis.net"),
            "SNT" to CoinInfo(BigDecimal(6_804_870_174), "20/06/2017", "https://status.im"),
            "CHSB" to CoinInfo(BigDecimal(1_000_000_000), "08/09/2017", "https://swissborg.com"),
            "USDT" to CoinInfo(BigDecimal(9_479_177_442), "10/06/2014", "https://tether.to"),
            "TUSD" to CoinInfo( null, "31/03/2018", "https://www.trusttoken.com"),
            "USDC" to CoinInfo(BigDecimal(931_069_099), "17/05/2018", "https://www.centre.io/usdc"),
            "WTC" to CoinInfo(BigDecimal(70_000_000), "27/08/2017", "https://www.waltonchain.org/en"),
            "WBTC" to CoinInfo(BigDecimal(21_000_000), "27/11/2018", "https://wbtc.network"),
            "WETH" to CoinInfo(BigDecimal(1_153_917), "31/12/2018", "https://weth.io")

            )
}

data class CoinInfo(val supply: BigDecimal?, val startDate: String?, val website: String?)
