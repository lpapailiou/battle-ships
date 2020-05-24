package ch.ffhs.esa.battleships.data.game

import ch.ffhs.esa.battleships.data.DataResult

interface GameRepository {

    suspend fun findById(gameId: Long): DataResult<Game>
    suspend fun saveGame(game: Game): DataResult<Long>
    suspend fun update(game: Game): DataResult<Int>
    suspend fun findActiveGames(uid: String): DataResult<List<GameWithPlayerInfo>>
}
