package io.horizontalsystems.bankwallet.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import io.horizontalsystems.bankwallet.core.storage.AccountRecord
import java.math.BigDecimal

@Entity(
    primaryKeys = ["tokenQueryId", "coinSettingsId", "accountId"],
    foreignKeys = [ForeignKey(
        entity = AccountRecord::class,
        parentColumns = ["id"],
        childColumns = ["accountId"],
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE,
        deferred = true
    )]
)
data class EnabledWalletCache(
    val tokenQueryId: String,
    val coinSettingsId: String,
    @ColumnInfo(name = "accountId", index = true)
    val accountId: String,
    val balance: BigDecimal,
    val balanceLocked: BigDecimal,
)
