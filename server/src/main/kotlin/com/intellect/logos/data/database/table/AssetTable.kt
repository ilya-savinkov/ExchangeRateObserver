package com.intellect.logos.data.database.table

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object AssetTable : Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    val name: Column<String> = varchar("name", 255)
    val description: Column<String> = text("description")

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}