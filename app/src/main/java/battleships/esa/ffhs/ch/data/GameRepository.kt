package battleships.esa.ffhs.ch.data

import androidx.lifecycle.LiveData
import battleships.esa.ffhs.ch.entity.GameEntity

class GameRepository(private val gameDao: GameDao) {

    val gameList: LiveData<List<GameEntity>> = gameDao.getGames()

    suspend fun insert(game: GameEntity) {
        gameDao.insert(game)
    }
}