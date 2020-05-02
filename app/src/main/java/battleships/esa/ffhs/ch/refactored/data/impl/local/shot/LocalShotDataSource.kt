package battleships.esa.ffhs.ch.refactored.data.impl.local.shot

import battleships.esa.ffhs.ch.refactored.data.DataResult
import battleships.esa.ffhs.ch.refactored.data.shot.Shot
import battleships.esa.ffhs.ch.refactored.data.shot.ShotDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalShotDataSource internal constructor(
    private val shotDao: ShotDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ShotDataSource {

    override suspend fun findByBoard(boardId: Long): DataResult<List<Shot>> =
        withContext(ioDispatcher) {
            try {
                val shots = shotDao.findByBoard(boardId)
                return@withContext DataResult.Success(shots)
            } catch (e: Exception) {
                return@withContext DataResult.Error(e)
            }

        }

    override suspend fun insert(shot: Shot): DataResult<Long> =
        withContext(ioDispatcher) {
            try {
                val shotId = shotDao.insert(shot)
                if (shotId != 0L) {
                    return@withContext DataResult.Success(shotId)
                } else {
                    return@withContext DataResult.Error(Exception("Could not save shot"))
                }
            } catch (e: Exception) {
                return@withContext DataResult.Error(e)
            }
        }
}
