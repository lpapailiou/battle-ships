package ch.ffhs.esa.battleships.data.source.local.board

import ch.ffhs.esa.battleships.data.DataResult
import ch.ffhs.esa.battleships.data.board.Board
import ch.ffhs.esa.battleships.data.board.BoardDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalBoardDataSource internal constructor(
    private val boardDao: BoardDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : BoardDataSource {

    override suspend fun findById(id: Int): DataResult<Board> =
        withContext(ioDispatcher) {
            try {
                val board = boardDao.findById(id)
                if (board != null) {
                    return@withContext DataResult.Success(board)
                } else {
                    return@withContext DataResult.Error(Exception("Player not found!"))
                }
            } catch (e: Exception) {
                return@withContext DataResult.Error(e)
            }
        }

    override suspend fun findByGameAndPlayer(gameId: Long, playerUID: String): DataResult<Board> =
        withContext(ioDispatcher) {
            try {
                val board = boardDao.findByGameAndPlayer(gameId, playerUID)
                if (board != null) {
                    return@withContext DataResult.Success(board)
                } else {
                    return@withContext DataResult.Error(Exception("Player not found!"))
                }
            } catch (e: Exception) {
                return@withContext DataResult.Error(e)
            }
        }

    override suspend fun insert(board: Board): DataResult<Long> =
        withContext(ioDispatcher) {
            try {
                val boardId = boardDao.insert(board)
                if (boardId != 0L) {
                    return@withContext DataResult.Success(boardId)
                } else {
                    return@withContext DataResult.Error(Exception("Could not save board!"))
                }
            } catch (e: Exception) {
                return@withContext DataResult.Error(e)
            }
        }
}
