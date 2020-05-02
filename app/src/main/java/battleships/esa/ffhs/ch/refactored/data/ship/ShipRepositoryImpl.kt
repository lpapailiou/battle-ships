package battleships.esa.ffhs.ch.refactored.data.ship

import battleships.esa.ffhs.ch.refactored.data.DataResult
import battleships.esa.ffhs.ch.refactored.di.AppModule.LocalShipDataSource
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
}
