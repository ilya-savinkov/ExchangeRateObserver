package com.intellect.logos.data.datasource.asset

import ServerConstant
import com.intellect.logos.data.database.table.AssetTable
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.annotation.Single
import response.AssetResponse

@Single
class AssetLocalDataSource {

    fun getAssets(page: Long, pageSize: Int): List<AssetResponse> {
        return transaction {
            AssetTable
                .selectAll()
                .limit(pageSize)
                .offset((page - 1) * pageSize)
                .map {
                    AssetResponse(
                        name = it[AssetTable.name],
                        description = it[AssetTable.description],
                        icon = getIconUrl(it[AssetTable.name])
                    )
                }
        }
    }

    fun getAsset(name: String): AssetResponse? {
        return transaction {
            AssetTable
                .selectAll()
                .where { AssetTable.name eq name }
                .map {
                    AssetResponse(
                        name = it[AssetTable.name],
                        description = it[AssetTable.description],
                        icon = getIconUrl(it[AssetTable.name])
                    )
                }
                .firstOrNull()
        }
    }

    private fun getIconUrl(name: String): String {
        return "${ServerConstant.URL}/image/forex/$name.svg"
    }
}