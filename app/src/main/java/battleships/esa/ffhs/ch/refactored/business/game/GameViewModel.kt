package battleships.esa.ffhs.ch.refactored.business.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import battleships.esa.ffhs.ch.refactored.data.DataResult
import battleships.esa.ffhs.ch.refactored.data.board.Board
import battleships.esa.ffhs.ch.refactored.data.board.BoardRepository
import battleships.esa.ffhs.ch.refactored.data.game.Game
import battleships.esa.ffhs.ch.refactored.data.game.GameRepository
import kotlinx.coroutines.launch
import javax.inject.Inject


class GameViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val boardRepository: BoardRepository
) : ViewModel() {

    private val _game = MutableLiveData<Game>()
    val game: LiveData<Game> = _game

    private val _activeBoard = MutableLiveData<Board>()
    val activeBoard: LiveData<Board> = _activeBoard

    private val _inactiveBoard = MutableLiveData<Board>()
    val inactiveBoard: LiveData<Board> = _inactiveBoard

    fun start(gameId: Long, currentPlayerId: Long, enemyPlayerId: Long) {
        if (_game.value != null) {
            return
        }
        loadGame(gameId)
        loadActiveBoard(gameId, currentPlayerId)
        loadInactiveBoard(gameId, enemyPlayerId)
    }

    private fun loadGame(gameId: Long) {
        viewModelScope.launch {
            val result = gameRepository.findById(gameId)
            if (result is DataResult.Success) {
                _game.value = result.data
            }
        }
    }

    private fun loadActiveBoard(gameId: Long, currentPlayerId: Long) {
        viewModelScope.launch {
            val result = boardRepository.findByGameAndPlayer(gameId, currentPlayerId)
            if (result is DataResult.Success) {
                _activeBoard.value = result.data
            }
        }
    }


    private fun loadInactiveBoard(gameId: Long, currentPlayerId: Long) {
        viewModelScope.launch {
            val result = boardRepository.findByGameAndPlayer(gameId, currentPlayerId)
            if (result is DataResult.Success) {
                _inactiveBoard.value = result.data
            }
        }
    }

}
