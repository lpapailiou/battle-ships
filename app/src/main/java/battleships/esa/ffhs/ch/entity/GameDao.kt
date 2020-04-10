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
        gameList.add(game)
        games.value = gameList                          // triggers observers
    }

    fun getGames() = games as LiveData<List<GameInstance>>      // do not change live data outside of this class

    fun getActiveGame(): LiveData<GameInstance>? {
        val activeGames = gameList.filter { game ->  game.isActive() }.toList()
        if (!activeGames.isEmpty()) {
            return activeGames.first() as LiveData<GameInstance>
        }
        return null
    }
}