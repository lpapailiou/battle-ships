package ch.ffhs.esa.battleships.data.source.local.ship

import ch.ffhs.esa.battleships.data.DataResult
import ch.ffhs.esa.battleships.data.ship.Ship
import ch.ffhs.esa.battleships.data.ship.ShipDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class LocalShipDataSource @Inject constructor(
    private val shipDao: ShipDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ShipDataSource {

    override suspend fun insert(ship: Ship): DataResult<Long> =
        withContext(ioDispatcher) {
            try {
                val shipId = shipDao.insert(ship)
                if (shipId != 0L) {
                    return@withContext DataResult.Success(shipId)
                } else {
                    return@withContext DataResult.Error(Exception("Could not insert ship!"))
                }
            } catch (e: Exception) {
                return@withContext DataResult.Error(e)
            }
        }

    override suspend fun loadByBoard(boardId: Long): DataResult<List<Ship>> =
        withContext(ioDispatcher) {
            try {
                val ships = shipDao.loadByBoard(boardId)
                if (ships.isNotEmpty()) {
                    return@withContext DataResult.Success(ships)
                } else {
                    return@withContext DataResult.Error(Exception("No Ships found for Board"))
                }
            } catch (e: Exception) {
                return@withContext DataResult.Error(e)
            }
        }
}
