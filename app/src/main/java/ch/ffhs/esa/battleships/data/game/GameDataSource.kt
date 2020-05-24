package ch.ffhs.esa.battleships.data.game

import ch.ffhs.esa.battleships.data.DataResult

interface GameDataSource {
    suspend fun findById(gameId: Long): DataResult<Game>

    suspend fun insert(game: Game): DataResult<Long>
    suspend fun update(game: Game): DataResult<Int>
    suspend fun findActiveGames(uid: String): DataResult<List<GameWithPlayerInfo>>
}
