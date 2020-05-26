package ch.ffhs.esa.battleships.data.shot

import ch.ffhs.esa.battleships.data.DataResult

interface ShotRepository {
    suspend fun findByBoard(boardUid: String): DataResult<List<Shot>>

    suspend fun insert(shot: Shot): DataResult<String>
}
