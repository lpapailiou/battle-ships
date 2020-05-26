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

    override suspend fun insert(ship: Ship): DataResult<String> =
        withContext(ioDispatcher) {
            try {
                shipDao.insert(ship)
                return@withContext DataResult.Success("Success")
            } catch (e: Exception) {
                return@withContext DataResult.Error(e)
            }
        }

    override suspend fun loadByBoard(boardUid: String): DataResult<List<Ship>> =
        withContext(ioDispatcher) {
            try {
                val ships = shipDao.loadByBoard(boardUid)
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
