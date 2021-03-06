package ch.ffhs.esa.battleships.business.game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.ffhs.esa.battleships.business.BOARD_SIZE
import ch.ffhs.esa.battleships.business.BOT_PLAYER_ID
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
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
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

    private var isBotGame: Boolean = true;

    fun start(gameUid: String, ownPlayerUid: String, enemyPlayerUid: String) =
        viewModelScope.launch {
            isBotGame = enemyPlayerUid == BOT_PLAYER_ID
            try {
                Log.d("procedureLogger", "------------- >>>>>>> game start()")
                Log.d(
                    "gameInfo",
                    "launching game (id: $gameUid) as own player (id: $ownPlayerUid) and defender (id: $enemyPlayerUid)"
                )
                if (_game.value != null) {
                    Log.d("gameViewModel", "game has ID")
                    return@launch
                }
                loadGame(gameUid)
                loadPlayer(ownPlayerUid)
                loadEnemyPlayer(enemyPlayerUid)

                val ownBoard = loadBoard(gameUid, ownPlayerUid)
                loadShips(ownBoard)
                loadShots(ownBoard)
                _ownBoard.value = ownBoard

                val enemyBoard = loadBoard(gameUid, enemyPlayerUid)
                loadShips(enemyBoard)
                loadShots(enemyBoard)

                _enemyBoard.value = enemyBoard

                if (enemyPlayerUid == BOT_PLAYER_ID) {
                    return@launch
                }

                observeShots(_ownBoard)
                observeGame(gameUid, ownPlayerUid)


            } catch (e: Exception) {
                Log.d("gameviewmodelexception", "failed to load game")
                e.stackTrace
                throw e
            }
        }

    private fun loadGame(gameUid: String) = viewModelScope.launch {
        try {
            Log.d("procedureLogger", "------------- >>>>>>> game loadGame()")
            val result = gameRepository.findByUid(gameUid)
            if (result is DataResult.Success) {
                _game.value = result.data
            }
        } catch (e: Exception) {
            e.stackTrace
        }
    }

    @OptIn(InternalCoroutinesApi::class)
    private suspend fun observeGame(gameUid: String, playerUid: String) {
        gameRepository.observe(gameUid, playerUid).collect(object : FlowCollector<Game> {
            override suspend fun emit(value: Game) {
                _game.value = value
            }
        })
    }

    @OptIn(InternalCoroutinesApi::class)
    private fun observeShots(boardLiveData: MutableLiveData<BoardModel>) = viewModelScope.launch {
        try {
            Log.d("procedureLogger", "------------- >>>>>>> game observeShots()")
            shotRepository.observe(boardLiveData.value!!.uid!!)
                .collect(object : FlowCollector<List<Shot>> {
                    override suspend fun emit(value: List<Shot>) {
                        if (game.value!!.defenderUid != BOT_PLAYER_ID) {
                            val allShipCells =
                                boardLiveData.value!!.ships.value!!.flatMap { it.getShipCells() }
                            boardLiveData.value!!.shots.value = value.map {
                                ShotModel(
                                    it.x,
                                    it.y,
                                    it.boardUid,
                                    allShipCells.contains(Cell(it.x, it.y)),
                                    true
                                )
                            }.toMutableList()
                            uncoverSunkenEnemyShips(boardLiveData.value!!)
                        }
                        boardLiveData.value = boardLiveData.value
                        checkIfGameIsOver(boardLiveData.value!!)
                    }
                })
        } catch (e: Exception) {
            e.stackTrace
        }
    }


    private suspend fun loadPlayer(currentPlayerUid: String) {

        val result = playerRepository.findByUid(currentPlayerUid)

        if (result is DataResult.Success) {
            player = result.data!!
            Log.d("launchGame: ", "own player loaded: " + player.name)
        }

        if (result is DataResult.Error) {
            throw result.exception
        }
    }


    private fun loadEnemyPlayer(enemyPlayerUid: String) {
        viewModelScope.launch {
            try {
                Log.d("procedureLogger", "------------- >>>>>>> game loadEnemyPlayer()")
                val result = playerRepository.findByUid(enemyPlayerUid)

                if (result is DataResult.Success) {
                    enemyPlayer = result.data!!
                    Log.d("launchGame: ", "own player loaded: " + enemyPlayer.name)
                }
            } catch (e: Exception) {
                e.stackTrace
            }
        }
    }

    private suspend fun loadBoard(gameUid: String, currentPlayerUid: String): BoardModel {
        val result = boardRepository.findByGameAndPlayer(gameUid, currentPlayerUid, isBotGame)
        if (result is DataResult.Success) {
            return BoardModel(
                result.data.uid,
                result.data.gameUid,
                result.data.playerUid
            )
        }

        result as DataResult.Error
        throw result.exception
    }

    private fun loadShips(boardModel: BoardModel) = viewModelScope.launch {
        try {
            Log.d("procedureLogger", "------------- >>>>>>> game loadShips()")
            val result = shipRepository.findByBoard(boardModel.uid!!, isBotGame)
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
        } catch (e: Exception) {
            e.stackTrace
        }
    }

    private fun loadShots(boardModel: BoardModel) = viewModelScope.launch {
        try {
            Log.d("procedureLogger", "------------- >>>>>>> game loadShots()")
            val result = shotRepository.findByBoard(boardModel.uid!!, isBotGame)
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
        } catch (e: Exception) {
            e.stackTrace
        }
    }

    fun shootAt(target: Cell) = viewModelScope.launch {
        try {
            Log.d("procedureLogger", "------------- >>>>>>> game shootAt()")
            if (_game.value!!.state == GameState.ENDED) {
                return@launch
            }

            if (_game.value!!.playerAtTurnUid != player.uid) {
                return@launch
            }

            if (_enemyBoard.value == null) {
                return@launch
            }

            if (_enemyBoard.value!!.shots.value == null) {
                return@launch
            }

            if (_enemyBoard.value!!.shots.value!!.any { it.x == target.x && it.y == target.y }) {
                return@launch
            }


            createShot(target.x, target.y, _enemyBoard.value!!)

            swapTurns()
            if (enemyPlayer.uid == BOT_PLAYER_ID) {
                makeAiMove()
            }
        } catch (e: Exception) {
            e.stackTrace
        }
    }

    private suspend fun makeAiMove() {
        placeRandomShot()
        swapTurns()
    }

    private suspend fun createShot(x: Int, y: Int, board: BoardModel) {
        val shot = Shot(x, y, board.uid!!)
        val result = shotRepository.insert(shot, isBotGame)
        val isShotAHit = board.ships.value!!.flatMap { it.getShipCells() }.contains(Cell(x, y))

        if (isShotAHit && board.uid != _ownBoard.value?.uid) {
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
    private suspend fun uncoverSunkenEnemyShips(board: BoardModel) {
        val shotCells = board.shots.value!!.map { Cell(it.x, it.y) }.toMutableList()
        board.ships.value!!
            //.filter { !it.isVisible }
            .filter { ship ->
                ship.getShipCells().intersect(shotCells).size == ship.getShipCells().size
            }.forEach { ship ->
                ship.isVisible = true
                ship.isSunk = true
                val shots = board.shots.value!!.intersect(ship.getShipCells())
                shots as LinkedHashSet<ShotModel>
                shots.forEach {
                    it.isVisible = false
                }
            }
    }

    private fun placeRandomShot() = viewModelScope.launch {
        try {
            Log.d("procedureLogger", "------------- >>>>>>> game placeRandomShot()")
            var x: Int
            var y: Int

            do {
                x = Random.nextInt(BOARD_SIZE)
                y = Random.nextInt(BOARD_SIZE)
            } while (_ownBoard.value!!.shots.value!!.any { it.x == x && it.y == y })

            createShot(x, y, _ownBoard.value!!)
        } catch (e: Exception) {
            e.stackTrace
        }
    }

    private suspend fun swapTurns() {

        _game.value!!.playerAtTurnUid =
            if (_game.value!!.playerAtTurnUid == ownBoard.value!!.playerUid)
                enemyBoard.value!!.playerUid
            else ownBoard.value!!.playerUid


        gameRepository.save(_game.value!!)
    }

    private suspend fun checkIfGameIsOver(board: BoardModel) {
        val allShipCells = board.ships.value!!.flatMap { it.getShipCells() }
        val allShotCells = board.shots.value!!.filter { it.isHit }.map { Cell(it.x, it.y) }

        val hasWon = allShipCells.minus(allShotCells).isEmpty()

        if (hasWon && allShotCells.isNotEmpty()) {
            _game.value!!.winnerUid =
                if (board.playerUid == ownBoard.value!!.playerUid)
                    enemyBoard.value!!.playerUid
                else ownBoard.value!!.playerUid

            endGame()
        }
    }

    private suspend fun endGame() {
        _game.value!!.state = GameState.ENDED
        saveGame()
    }

    private suspend fun saveGame() {
        val result = gameRepository.save(_game.value!!)
        if (result is DataResult.Success) {
            _gameOverEvent.value = Event(Unit)
        }
    }
}
