package battleships.esa.ffhs.ch.refactored.data.game

import battleships.esa.ffhs.ch.refactored.data.DataResult

interface GameRepository {

    suspend fun findById(gameId: Long): DataResult<Game>
    suspend fun saveGame(game: Game): DataResult<Long>
    suspend fun update(game: Game): DataResult<Int>
    suspend fun findActiveGames(): DataResult<List<Game>>
}
