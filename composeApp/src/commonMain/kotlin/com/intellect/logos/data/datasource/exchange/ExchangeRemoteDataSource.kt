package com.intellect.logos.data.datasource.exchange

class ExchangeRemoteDataSource {

    suspend fun getRate(from: String, to: String): Result<Double> {
        println(from)
        println(to)
        TODO()
    }
}