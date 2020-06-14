package ch.ffhs.esa.battleships.data.ship

import ch.ffhs.esa.battleships.data.DataResult
import ch.ffhs.esa.battleships.di.AppModule.LocalShipDataSource
import ch.ffhs.esa.battleships.di.AppModule.RemoteShipDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class ShipRepositoryImpl @Inject constructor(
    @LocalShipDataSource private val localShipDataSource: ShipDataSource,
    @RemoteShipDataSource private val remoteShipDataSource: ShipDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ShipRepository {

    override suspend fun insert(
        ship: Ship,
        isBotGame: Boolean
    ): DataResult<String> {
        return withContext(ioDispatcher) {
            if (ship.uid.isEmpty()) {
                ship.uid = "%s_%d_%d".format(ship.boardUid, ship.x, ship.y)
            }

            if (!isBotGame) {
                val result = remoteShipDataSource.insert(ship)
                if (result is DataResult.Error) {
                    throw result.exception
                }
            }

            return@withContext localShipDataSource.insert(ship)
        }
    }

    override suspend fun findByBoard(boardUid: String, isBotGame: Boolean): DataResult<List<Ship>> {
        if (!isBotGame) {
            val remoteResult = remoteShipDataSource.loadByBoard(boardUid)

            if (remoteResult is DataResult.Error) {
                return remoteResult
            }

            remoteResult as DataResult.Success
            remoteResult.data.forEach { localShipDataSource.insert(it) }
        }

        return localShipDataSource.loadByBoard(boardUid)
    }
}
