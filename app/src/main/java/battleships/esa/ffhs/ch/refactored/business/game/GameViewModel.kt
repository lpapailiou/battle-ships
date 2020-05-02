package battleships.esa.ffhs.ch.refactored.business.game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import battleships.esa.ffhs.ch.old.model.BOARD_SIZE
import battleships.esa.ffhs.ch.old.model.GameState
import battleships.esa.ffhs.ch.refactored.business.board.BoardModel
import battleships.esa.ffhs.ch.refactored.business.board.Cell
import battleships.esa.ffhs.ch.refactored.business.ship.DirectionLogic
import battleships.esa.ffhs.ch.refactored.business.ship.ShipModel
import battleships.esa.ffhs.ch.refactored.business.shot.ShotModel
import battleships.esa.ffhs.ch.refactored.data.DataResult
import battleships.esa.ffhs.ch.refactored.data.board.BoardRepository
import battleships.esa.ffhs.ch.refactored.data.game.Game
import battleships.esa.ffhs.ch.refactored.data.game.GameRepository
import battleships.esa.ffhs.ch.refactored.data.player.Player
import battleships.esa.ffhs.ch.refactored.data.player.PlayerRepository
import battleships.esa.ffhs.ch.refactored.data.ship.ShipRepository
import battleships.esa.ffhs.ch.refactored.data.shot.Shot
import battleships.esa.ffhs.ch.refactored.data.shot.ShotRepository
import battleships.esa.ffhs.ch.refactored.event.Event
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random


class GameViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val playerRepository: PlayerRepository,
    private val boardRepository: BoardRepository,
    private val shipRepository: ShipRepository,
    private val shotRepository: ShotRepository,
    private val directionLogic: DirectionLogic
) : ViewModel() {

    private val _game = MutableLiveData<Game>()
    val game: LiveData<Game> = _game

    private val _activeBoard = MutableLiveData<BoardModel>()
    val activeBoard: LiveData<BoardModel> = _activeBoard

    private val _inactiveBoard = MutableLiveData<BoardModel>()
    val inactiveBoard: LiveData<BoardModel> = _inactiveBoard

    private val _gameOverEvent = MutableLiveData<Event<Unit>>()
    val gameOverEvent: LiveData<Event<Unit>> = _gameOverEvent

    private lateinit var player: Player

    private lateinit var enemyPlayer: Player

    private lateinit var ownBoard: BoardModel

    private lateinit var enemyBoard: BoardModel

    fun start(gameId: Long, currentPlayerId: Long, enemyPlayerId: Long) {
        if (_game.value != null) {
            return
        }
        loadGame(gameId)
        loadPlayer(currentPlayerId)
        loadEnemyPlayer(enemyPlayerId)
        loadOwnBoard(gameId, currentPlayerId)
        loadEnemyBoard(gameId, enemyPlayerId)
    }

    private fun loadGame(gameId: Long) {
        viewModelScope.launch {
            val result = gameRepository.findById(gameId)
            if (result is DataResult.Success) {
                _game.value = result.data
            }
        }
    }

    private fun loadPlayer(currentPlayerId: Long) {
        viewModelScope.launch {
            val result = playerRepository.findById(currentPlayerId)
            if (result is DataResult.Success) {
                player = result.data
            }
        }
    }

    private fun loadEnemyPlayer(enemyPlayerId: Long) {
        viewModelScope.launch {
            val result = playerRepository.findById(enemyPlayerId)
            if (result is DataResult.Success) {
                enemyPlayer = result.data
            }
        }
    }

    private fun loadEnemyBoard(gameId: Long, enemyPlayerId: Long) {
        viewModelScope.launch {
            val result = boardRepository.findByGameAndPlayer(gameId, enemyPlayerId)
            if (result is DataResult.Success) {
                val boardModel = BoardModel(
                    result.data.id,
                    result.data.gameId,
                    result.data.playerId
                )
                enemyBoard = boardModel
                loadShips(boardModel)
                loadShots(boardModel)
                _activeBoard.value = boardModel
            }
        }
    }

    private fun loadOwnBoard(gameId: Long, currentPlayerId: Long) {
        viewModelScope.launch {
            val result = boardRepository.findByGameAndPlayer(gameId, currentPlayerId)
            if (result is DataResult.Success) {
                val boardModel = BoardModel(
                    result.data.id,
                    result.data.gameId,
                    result.data.playerId
                )
                ownBoard = boardModel
                loadShips(boardModel)
                loadShots(boardModel)
                _inactiveBoard.value = boardModel
            }
        }
    }

    private fun loadShips(boardModel: BoardModel) = viewModelScope.launch {
        val result = shipRepository.findByBoard(boardModel.id)
        if (result is DataResult.Success) {
            boardModel.ships.value = result.data.map {
                ShipModel(
                    it.x,
                    it.y,
                    it.size,
                    it.direction,
                    it.boardId,
                    boardModel.playerId == player.id,
                    directionLogic
                )
            }.toMutableList()
            _activeBoard.value = _activeBoard.value
            _inactiveBoard.value = _inactiveBoard.value
        }
    }

    private fun loadShots(boardModel: BoardModel) = viewModelScope.launch {
        val result = shotRepository.findByBoard(boardModel.id)
        if (result is DataResult.Success) {
            val allShipCells = boardModel.ships.value!!.flatMap { it.getShipCells() }
            boardModel.shots.value = result.data.map { shot ->
                ShotModel(
                    0,
                    shot.x,
                    shot.y,
                    shot.boardId,
                    allShipCells.contains(Cell(shot.x, shot.y))
                )
            }.toMutableList()
            Log.e("", boardModel.playerId.toString())
            _activeBoard.value = _activeBoard.value
            _inactiveBoard.value = _inactiveBoard.value
        }
    }

    fun shootAt(target: Cell) {
        if (_game.value!!.state == GameState.ENDED) {
            return
        }

        if (_game.value!!.playerAtTurnId != player.id) {
            return
        }

        if (enemyBoard.shots.value!!.any { it.x == target.x && it.y == target.y }) {
            return
        }


        createShot(target.x, target.y, enemyBoard)
        checkIfGameIsOver(enemyBoard)
        setEnemyPlayerAtTurn()

        makeAiMove()
    }

    private fun makeAiMove() {
        placeRandomShot()
        checkIfGameIsOver(ownBoard)
        setHumanPlayerAtTurn()
    }

    private fun setEnemyPlayerAtTurn() = viewModelScope.launch {
        val game = game.value
        game!!.playerAtTurnId = enemyPlayer.id
        gameRepository.saveGame(game)
    }

    private fun createShot(x: Int, y: Int, board: BoardModel) = viewModelScope.launch {
        val shot = Shot(x, y, board.id)
        val result = shotRepository.insert(shot)

        if (result is DataResult.Success) {
            val shotModel = ShotModel(
                result.data,
                shot.x,
                shot.y,
                shot.boardId,
                board.ships.value!!.flatMap { it.getShipCells() }.contains(Cell(x, y))
            )
            board.shots.value!!.add(shotModel)
            board.shots.value = board.shots.value

            _activeBoard.value = _activeBoard.value
            _inactiveBoard.value = _inactiveBoard.value
        }
    }

    private fun placeRandomShot() = viewModelScope.launch {
        var x: Int
        var y: Int

        do {
            x = Random.nextInt(BOARD_SIZE)
            y = Random.nextInt(BOARD_SIZE)
        } while (ownBoard.shots.value!!.any { it.x == x && it.y == y })

        createShot(x, y, ownBoard)
    }

    private fun setHumanPlayerAtTurn() = viewModelScope.launch {
        val game = game.value
        game!!.playerAtTurnId = player.id
        gameRepository.saveGame(game)
    }

    private fun checkIfGameIsOver(board: BoardModel) {
        val allShipCells = board.ships.value!!.flatMap { it.getShipCells() }
        val allShotCells = board.shots.value!!.filter { it.isHit }.map { Cell(it.x, it.y) }

        val hasWon = allShipCells.minus(allShotCells).isEmpty()

        if (hasWon) {
            endGame()
        }
    }

    private fun endGame() {
        _game.value!!.state = GameState.ENDED
        _game.value!!.winnerId = _game.value!!.playerAtTurnId
        saveGame()
    }

    private fun saveGame() = viewModelScope.launch {
        val result = gameRepository.update(_game.value!!)
        if (result is DataResult.Success) {
            _gameOverEvent.value = Event(Unit)
        }
    }
}
