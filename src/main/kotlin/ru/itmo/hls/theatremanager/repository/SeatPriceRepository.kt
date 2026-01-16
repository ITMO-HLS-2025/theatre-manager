package ru.itmo.hls.theatremanager.repository

import io.r2dbc.spi.Row
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import ru.itmo.hls.theatremanager.entity.SeatPrice

@Repository
class SeatPriceRepository(
    private val databaseClient: DatabaseClient
) {
    suspend fun findSeatPricesByShowAndSeatIds(showId: Long, seatIds: List<Long>): List<SeatPrice> {
        if (seatIds.isEmpty()) return emptyList()

        val placeholders = seatIds.indices.joinToString(",") { ":id$it" }
        val sql = """
            select sp.seat_id, sp.show_id, sp.price
            from seat_price sp
            where sp.show_id = :showId and sp.seat_id in ($placeholders)
        """.trimIndent()

        var spec = databaseClient.sql(sql).bind("showId", showId)
        seatIds.forEachIndexed { index, id ->
            spec = spec.bind("id$index", id)
        }

        return spec.map { row, _ ->
            SeatPrice(
                seatId = row.getLong("seat_id"),
                showId = row.getLong("show_id"),
                price = row.getInt("price")
            )
        }.all().collectList().awaitSingle()
    }

    suspend fun deleteByHallId(hallId: Long): Int {
        val sql = """
            delete from seat_price
            where seat_id in (select id from seat where hall_id = :hallId)
        """.trimIndent()

        return databaseClient.sql(sql)
            .bind("hallId", hallId)
            .fetch()
            .rowsUpdated()
            .awaitSingle()
            .toInt()
    }

    suspend fun replaceByShowId(showId: Long, prices: List<SeatPrice>): Int {
        val deleteSql = "delete from seat_price where show_id = :showId"
        databaseClient.sql(deleteSql)
            .bind("showId", showId)
            .fetch()
            .rowsUpdated()
            .awaitSingle()

        if (prices.isEmpty()) return 0

        var updated = 0
        prices.forEach { price ->
            val insertSql = """
                insert into seat_price (seat_id, show_id, price)
                values (:seatId, :showId, :price)
            """.trimIndent()
            updated += databaseClient.sql(insertSql)
                .bind("seatId", price.seatId)
                .bind("showId", price.showId)
                .bind("price", price.price)
                .fetch()
                .rowsUpdated()
                .awaitSingle()
                .toInt()
        }
        return updated
    }
}

private fun Row.getLong(name: String): Long =
    (get(name) as Number?)?.toLong() ?: 0L

private fun Row.getInt(name: String): Int =
    (get(name) as Number?)?.toInt() ?: 0
