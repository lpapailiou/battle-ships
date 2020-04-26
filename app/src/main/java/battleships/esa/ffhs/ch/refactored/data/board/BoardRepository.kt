package battleships.esa.ffhs.ch.refactored.data.board

import battleships.esa.ffhs.ch.refactored.data.DataResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BoardRepository @Inject constructor(
    private val localBoardDataSource: BoardDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun findById(id: Int): DataResult<Board> {
        return withContext(ioDispatcher) {
            return@withContext localBoardDataSource.findById(id)
        }
    }
}
