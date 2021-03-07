package com.example.mybankapplication

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Entities(
    @PrimaryKey val uid: String,
    @ColumnInfo(name = "account_name") val account_name: String?,
    @ColumnInfo(name = "iban") val iban: String?,
    @ColumnInfo(name = "currency") val currency: String?,
    @ColumnInfo(name = "amount") val amount: String?

)

