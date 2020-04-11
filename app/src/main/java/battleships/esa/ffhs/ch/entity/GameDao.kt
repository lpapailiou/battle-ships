package battleships.esa.ffhs.ch.entity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class GameDao {
    // Dao = Data Access Object
    private val gameList = mutableListOf<GameInstance>()
    private val games = MutableLiveData<List<GameInstance>>()

    init {
        games.value = gameList
    }

    fun addGame(game: GameInstance) {
        gameList.add(0, game)
        games.value = gameList                                  // triggers observers
    }

    fun getGames() = games as LiveData<List<GameInstance>>      // do not change live data outside of this class

    fun getActiveGame(): GameInstance? {                        // not sure if good idea to return game instance here
        val activeGames = gameList.filter { game ->  game.isActive() }.toList()
        if (activeGames.isNotEmpty()) {
            return activeGames.first()
        }
        return null
    }
}