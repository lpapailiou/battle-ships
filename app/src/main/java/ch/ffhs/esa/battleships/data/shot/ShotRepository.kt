package ch.ffhs.esa.battleships.data.shot

import ch.ffhs.esa.battleships.data.DataResult
import kotlinx.coroutines.flow.Flow

interface ShotRepository {
    suspend fun findByBoard(boardUid: String, isBotGame: Boolean): DataResult<List<Shot>>

    suspend fun insert(shot: Shot, isBotGame: Boolean): DataResult<String>
    suspend fun observe(boardUid: String): Flow<List<Shot>>
}
