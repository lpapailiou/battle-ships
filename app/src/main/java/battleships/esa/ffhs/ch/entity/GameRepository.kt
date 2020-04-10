package battleships.esa.ffhs.ch.entity

class GameRepository private constructor (private val gameDao: GameDao) {       // mediator between viewModels and database

    companion object {
        @Volatile private var instance: GameRepository? = null

        fun getInstance(gameDao: GameDao) = instance ?: synchronized(this) {
            instance?: GameRepository(gameDao).also { instance = it }
        }
    }

    fun addGame(game: GameInstance) {
        gameDao.addGame(game)
    }

    fun getGames() = gameDao.getGames()

    fun getActiveGame() = gameDao.getActiveGame()

}