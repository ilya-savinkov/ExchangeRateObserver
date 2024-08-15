package com.intellect.logos.data.database.table

import org.jetbrains.exposed.sql.Table

object RateTable : Table() {
    val id = integer("id").autoIncrement()
    val baseAssetName = varchar("base_asset_name", 255)
    val quoteAssetName = varchar("quote_asset_name", 255)
    val rate = double("rate")
    val timestamp = long("timestamp")

    override val primaryKey = PrimaryKey(id)
}