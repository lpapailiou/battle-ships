package battleships.esa.ffhs.ch.refactored.data.impl.local.ship

import battleships.esa.ffhs.ch.refactored.data.DataResult
import battleships.esa.ffhs.ch.refactored.data.ship.Ship
import battleships.esa.ffhs.ch.refactored.data.ship.ShipDataSource
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
}
