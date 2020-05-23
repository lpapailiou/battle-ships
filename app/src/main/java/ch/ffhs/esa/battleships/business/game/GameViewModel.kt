package ch.ffhs.esa.battleships.business.game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.ffhs.esa.battleships.business.BOARD_SIZE
import ch.ffhs.esa.battleships.business.board.BoardModel
import ch.ffhs.esa.battleships.business.board.Cell
import ch.ffhs.esa.battleships.business.ship.DirectionLogic
import ch.ffhs.esa.battleships.business.ship.ShipModel
import ch.ffhs.esa.battleships.business.shot.ShotModel
import ch.ffhs.esa.battleships.data.DataResult
import ch.ffhs.esa.battleships.data.board.BoardRepository
import ch.ffhs.esa.battleships.data.game.Game
import ch.ffhs.esa.battleships.data.game.GameRepository
import ch.ffhs.esa.battleships.data.player.Player
import ch.ffhs.esa.battleships.data.player.PlayerRepository
import ch.ffhs.esa.battleships.data.ship.ShipRepository
import ch.ffhs.esa.battleships.data.shot.Shot
import ch.ffhs.esa.battleships.data.shot.ShotRepository
import ch.ffhs.esa.battleships.event.Event
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

    private val _enemyBoard = MutableLiveData<BoardModel>()
    val enemyBoard: LiveData<BoardModel> = _enemyBoard

    private val _ownBoard = MutableLiveData<BoardModel>()
    val ownBoard: LiveData<BoardModel> = _ownBoard

    private val _gameOverEvent = MutableLiveData<Event<Unit>>()
    val gameOverEvent: LiveData<Event<Unit>> = _gameOverEvent

    private val _shipHitEvent = MutableLiveData<Event<Unit>>()
    val shipHitEvent: LiveData<Event<Unit>> = _shipHitEvent

    private lateinit var player: Player

    private lateinit var enemyPlayer: Player

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
                loadShips(boardModel)
                loadShots(boardModel)
                _enemyBoard.value = boardModel
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
                loadShips(boardModel)
                loadShots(boardModel)
                _ownBoard.value = boardModel
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
            _enemyBoard.value = _enemyBoard.value
            _ownBoard.value = _ownBoard.value
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
            _enemyBoard.value = _enemyBoard.value
            _ownBoard.value = _ownBoard.value
        }
    }

    fun shootAt(target: Cell) {
        if (_game.value!!.state == GameState.ENDED) {
            return
        }

        if (_game.value!!.playerAtTurnId != player.id) {
            return
        }

        if (_enemyBoard.value!!.shots.value!!.any { it.x == target.x && it.y == target.y }) {
            return
        }


        createShot(target.x, target.y, _enemyBoard.value!!)
        checkIfGameIsOver(_enemyBoard.value!!) // TODO: resolve race condition
        setEnemyPlayerAtTurn()

        makeAiMove()
    }

    private fun makeAiMove() {
        placeRandomShot()
        checkIfGameIsOver(_ownBoard.value!!)
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
        val isShotAHit = board.ships.value!!.flatMap { it.getShipCells() }.contains(Cell(x, y))
        if (isShotAHit) {
            _shipHitEvent.value = Event(Unit)
        }

        if (result is DataResult.Success) {
            val shotModel = ShotModel(
                result.data,
                shot.x,
                shot.y,
                shot.boardId,
                isShotAHit
            )
            board.shots.value!!.add(shotModel)
            board.shots.value = board.shots.value

            _enemyBoard.value = _enemyBoard.value
            _ownBoard.value = _ownBoard.value
        }
    }

    private fun placeRandomShot() = viewModelScope.launch {
        var x: Int
        var y: Int

        do {
            x = Random.nextInt(BOARD_SIZE)
            y = Random.nextInt(BOARD_SIZE)
        } while (_ownBoard.value!!.shots.value!!.any { it.x == x && it.y == y })

        createShot(x, y, _ownBoard.value!!)
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
