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

    fun start(gameUid: String, ownPlayerUid: String, enemyPlayerUid: String) =
        viewModelScope.launch {
            if (_game.value != null) {
                return@launch
            }
            loadGame(gameUid)
            loadPlayer(ownPlayerUid)
            loadEnemyPlayer(enemyPlayerUid)
            loadOwnBoard(gameUid, ownPlayerUid)
            loadEnemyBoard(gameUid, enemyPlayerUid)
        }

    private fun loadGame(gameUid: String) =
        viewModelScope.launch {
            val result = gameRepository.findByUid(gameUid)
            if (result is DataResult.Success) {
                _game.value = result.data
            }
        }


    private suspend fun loadPlayer(currentPlayerUid: String) {

        val result = playerRepository.findByUid(currentPlayerUid)

        if (result is DataResult.Success) {
            player = result.data!!
        }

        if (result is DataResult.Error) {
            throw result.exception
        }
    }


    private fun loadEnemyPlayer(enemyPlayerUid: String) {
        viewModelScope.launch {

            val result = playerRepository.findByUid(enemyPlayerUid)

            if (result is DataResult.Success) {
                enemyPlayer = result.data!!
            }
        }
    }

    private fun loadEnemyBoard(gameUid: String, enemyPlayerUid: String) {
        viewModelScope.launch {
            val result = boardRepository.findByGameAndPlayer(gameUid, enemyPlayerUid)
            if (result is DataResult.Success) {
                val boardModel = BoardModel(
                    result.data.uid,
                    result.data.gameUid,
                    result.data.playerUid
                )
                loadShips(boardModel)
                loadShots(boardModel)
                _enemyBoard.value = boardModel
            }
        }
    }

    private fun loadOwnBoard(gameUid: String, currentPlayerUid: String) {
        viewModelScope.launch {
            val result = boardRepository.findByGameAndPlayer(gameUid, currentPlayerUid)
            if (result is DataResult.Success) {
                val boardModel = BoardModel(
                    result.data.uid,
                    result.data.gameUid,
                    result.data.playerUid
                )
                loadShips(boardModel)
                loadShots(boardModel)
                _ownBoard.value = boardModel
            }
        }
    }

    private fun loadShips(boardModel: BoardModel) = viewModelScope.launch {
        val result = shipRepository.findByBoard(boardModel.uid!!)
        if (result is DataResult.Success) {
            boardModel.ships.value = result.data.map {
                ShipModel(
                    it.x,
                    it.y,
                    it.size,
                    it.direction,
                    it.boardUid,
                    boardModel.playerUid == player.uid,
                    directionLogic
                )
            }.toMutableList()
            _enemyBoard.value = _enemyBoard.value
            _ownBoard.value = _ownBoard.value
        }
    }

    private fun loadShots(boardModel: BoardModel) = viewModelScope.launch {
        val result = shotRepository.findByBoard(boardModel.uid!!)
        if (result is DataResult.Success) {
            val allShipCells = boardModel.ships.value!!.flatMap { it.getShipCells() }
            boardModel.shots.value = result.data.map { shot ->
                ShotModel(
                    shot.x,
                    shot.y,
                    shot.boardUid,
                    allShipCells.contains(Cell(shot.x, shot.y)),
                    true
                )
            }.toMutableList()

            uncoverSunkenEnemyShips(boardModel)
            _enemyBoard.value = _enemyBoard.value
            _ownBoard.value = _ownBoard.value
        }
    }

    fun shootAt(target: Cell) {
        if (_game.value!!.state == GameState.ENDED) {
            return
        }

        if (_game.value!!.playerAtTurnUid != player.uid) {
            return
        }

        if (_enemyBoard.value!!.shots.value!!.any { it.x == target.x && it.y == target.y }) {
            return
        }


        createShot(target.x, target.y, _enemyBoard.value!!)
        swapTurns()

        makeAiMove()
    }

    private fun makeAiMove() {
        placeRandomShot()
        swapTurns()
    }

    private fun createShot(x: Int, y: Int, board: BoardModel) = viewModelScope.launch {
        val shot = Shot(x, y, board.uid!!)
        val result = shotRepository.insert(shot)
        val isShotAHit = board.ships.value!!.flatMap { it.getShipCells() }.contains(Cell(x, y))
        if (isShotAHit) {
            _shipHitEvent.value = Event(Unit)
        }

        if (result is DataResult.Success) {
            val shotModel = ShotModel(
                shot.x,
                shot.y,
                shot.boardUid,
                isShotAHit,
                true
            )
            shotModel.shotUid = shot.uid

            board.shots.value!!.add(shotModel)
            board.shots.value = board.shots.value

            if (isShotAHit) {
                uncoverSunkenEnemyShips(board)
                checkIfGameIsOver(board)
            }
            _enemyBoard.value = _enemyBoard.value
            _ownBoard.value = _ownBoard.value
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun uncoverSunkenEnemyShips(board: BoardModel) {
        val shotCells = board.shots.value!!.map { Cell(it.x, it.y) }
        board.ships.value!!
            .filter { !it.isVisible }
            .filter { ship ->
                ship.getShipCells().intersect(shotCells).size == ship.getShipCells().size
            }.forEach { ship ->
                ship.isVisible = true
                val shots = board.shots.value!!.intersect(ship.getShipCells())
                shots as LinkedHashSet<ShotModel>
                shots.forEach {
                    it.isVisible = false
                }
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

    private fun swapTurns() = viewModelScope.launch {

        _game.value!!.playerAtTurnUid =
            if (_game.value!!.playerAtTurnUid == ownBoard.value!!.playerUid)
                enemyBoard.value!!.playerUid
            else ownBoard.value!!.playerUid


        gameRepository.save(_game.value!!)
    }

    private fun checkIfGameIsOver(board: BoardModel) {
        val allShipCells = board.ships.value!!.flatMap { it.getShipCells() }
        val allShotCells = board.shots.value!!.filter { it.isHit }.map { Cell(it.x, it.y) }

        val hasWon = allShipCells.minus(allShotCells).isEmpty()

        if (hasWon) {
            _game.value!!.winnerUid =
                if (board.playerUid == ownBoard.value!!.playerUid)
                    enemyBoard.value!!.playerUid
                else ownBoard.value!!.playerUid

            endGame()
        }
    }

    private fun endGame() {
        _game.value!!.state = GameState.ENDED
        saveGame()
    }

    private fun saveGame() = viewModelScope.launch {
        val result = gameRepository.save(_game.value!!)
        if (result is DataResult.Success) {
            _gameOverEvent.value = Event(Unit)
        }
    }
}
