package com.captain.testbarchart

/**
 * @autor captain
 * @version 1.0.0
 * @single create by 2023年-01月
 */
data class BarModel2(
    val avgPrice: Double,
    val closePrice: Double,
    val contractId: String,
    val dateTimeStamp: String,
    val highPrice: Double,
    val lowPrice: Double,
    val openPrice: Double,
    val position: Int,
    val preKlineEntity: PreKlineEntity?,
    val settlePrice: Double,
    val time: Double,
    val totalQty: String,
    val tradeDate: String,
    val validity: Boolean,
    val volume: String,

    var isDrawTime: Boolean
)

data class PreKlineEntity(
    val avgPrice: Double,
    val closePrice: Double,
    val contractId: String,
    val dateTimeStamp: String,
    val highPrice: Double,
    val lowPrice: Double,
    val openPrice: Double,
    val position: Int,
    val settlePrice: Double,
    val time: Int,
    val totalQty: Int,
    val tradeDate: Int,
    val validity: Boolean,
    val volume: Int
)

data class a(val  a: Int)