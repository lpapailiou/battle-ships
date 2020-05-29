package ch.ffhs.esa.battleships.data.source.local.shot

import ch.ffhs.esa.battleships.data.DataResult
import ch.ffhs.esa.battleships.data.shot.Shot
import ch.ffhs.esa.battleships.data.shot.ShotDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class LocalShotDataSource internal constructor(
    private val shotDao: ShotDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ShotDataSource {

    override suspend fun findByBoard(boardUid: String): DataResult<List<Shot>> =
        withContext(ioDispatcher) {
            try {
                val shots = shotDao.findByBoard(boardUid)
                return@withContext DataResult.Success(shots)
            } catch (e: Exception) {
                return@withContext DataResult.Error(e)
            }
        }

    override suspend fun insert(shot: Shot): DataResult<String> =
        withContext(ioDispatcher) {
            try {
                shotDao.insert(shot)
                return@withContext DataResult.Success("Success")
            } catch (e: Exception) {
                return@withContext DataResult.Error(e)
            }
        }

    override suspend fun observe(boardUid: String): Flow<List<Shot>> {
        TODO("Local observation not implemented")
    }
}
