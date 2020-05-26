package ch.ffhs.esa.battleships.data.board

import ch.ffhs.esa.battleships.data.DataResult

interface BoardDataSource {
    suspend fun findByUid(uid: String): DataResult<Board>

    suspend fun findByGameAndPlayer(gameUid: String, playerUid: String): DataResult<Board>

    suspend fun insert(board: Board): DataResult<String>
}
