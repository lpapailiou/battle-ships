package battleships.esa.ffhs.ch.refactored.data.impl.local.board

import battleships.esa.ffhs.ch.refactored.data.DataResult
import battleships.esa.ffhs.ch.refactored.data.board.Board
import battleships.esa.ffhs.ch.refactored.data.board.BoardDataSource
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
                val player = boardDao.findById(id)
                if (player != null) {
                    return@withContext DataResult.Success(player)
                } else {
                    return@withContext DataResult.Error(Exception("Player not found!"))
                }
            } catch (e: Exception) {
                return@withContext DataResult.Error(e)
            }
        }
}
