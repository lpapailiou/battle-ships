package ch.ffhs.esa.battleships.data.ship

import ch.ffhs.esa.battleships.data.DataResult
import ch.ffhs.esa.battleships.di.AppModule.LocalShipDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class ShipRepositoryImpl @Inject constructor(
    @LocalShipDataSource private val localShipDataSource: ShipDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ShipRepository {

    override suspend fun insert(ship: Ship): DataResult<Long> {
        return withContext(ioDispatcher) {
            return@withContext localShipDataSource.insert(ship)
        }
    }

    override suspend fun findByBoard(boardId: Long): DataResult<List<Ship>> {
        return withContext(ioDispatcher) {
            return@withContext localShipDataSource.loadByBoard(boardId)
        }
    }
}
