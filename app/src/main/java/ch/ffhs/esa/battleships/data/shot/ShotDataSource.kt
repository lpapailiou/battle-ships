package ch.ffhs.esa.battleships.data.shot

import ch.ffhs.esa.battleships.data.DataResult
import kotlinx.coroutines.flow.Flow

interface ShotDataSource {

    suspend fun findByBoard(boardUid: String): DataResult<List<Shot>>

    suspend fun insert(shot: Shot): DataResult<String>
    suspend fun observe(boardUid: String): Flow<List<Shot>>
}
