package ch.ffhs.esa.battleships.data.board

import ch.ffhs.esa.battleships.data.DataResult
import ch.ffhs.esa.battleships.di.AppModule.LocalBoardDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BoardRepository @Inject constructor(
    @LocalBoardDataSource private val localBoardDataSource: BoardDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun findById(id: Int): DataResult<Board> {
        return withContext(ioDispatcher) {
            return@withContext localBoardDataSource.findById(id)
        }
    }

    suspend fun findByGameAndPlayer(gameId: Long, playerId: String): DataResult<Board> {
        return withContext(ioDispatcher) {
            return@withContext localBoardDataSource.findByGameAndPlayer(gameId, playerId)
        }
    }

    suspend fun saveBoard(board: Board): DataResult<Long> {
        return withContext(ioDispatcher) {
            return@withContext localBoardDataSource.insert(board)
        }
    }
}
