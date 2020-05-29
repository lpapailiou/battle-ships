package ch.ffhs.esa.battleships.business.boardpreparation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.ffhs.esa.battleships.business.BOT_PLAYER_ID
import ch.ffhs.esa.battleships.business.board.BoardModel
import ch.ffhs.esa.battleships.business.board.Cell
import ch.ffhs.esa.battleships.business.game.GameState
import ch.ffhs.esa.battleships.business.ship.DirectionLogic
import ch.ffhs.esa.battleships.business.ship.ShipModel
import ch.ffhs.esa.battleships.data.DataResult
import ch.ffhs.esa.battleships.data.board.Board
import ch.ffhs.esa.battleships.data.board.BoardRepository
import ch.ffhs.esa.battleships.data.game.Game
import ch.ffhs.esa.battleships.data.game.GameRepository
import ch.ffhs.esa.battleships.data.player.Player
import ch.ffhs.esa.battleships.data.player.PlayerRepository
import ch.ffhs.esa.battleships.data.ship.Direction
import ch.ffhs.esa.battleships.data.ship.ShipRepository
import ch.ffhs.esa.battleships.event.Event
import kotlinx.coroutines.launch
import javax.inject.Inject

class BoardPreparationViewModel @Inject constructor(
    private val playerRepository: PlayerRepository,
    private val gameRepository: GameRepository,
    private val boardRepository: BoardRepository,
    private val shipRepository: ShipRepository,
    private val directionLogic: DirectionLogic
) : ViewModel() {

    private val _player = MutableLiveData<Player>()
    val player: LiveData<Player> = _player

    private val _board = MutableLiveData<BoardModel>()
    val board: LiveData<BoardModel> = _board

    private val _gameReadyEvent = MutableLiveData<Event<Game>>()
    val gameReadyEvent: LiveData<Event<Game>> = _gameReadyEvent

    private val _waitForEnemyEvent = MutableLiveData<Event<Unit>>()
    val waitForEnemyEvent: LiveData<Event<Unit>> = _waitForEnemyEvent

    private val botBoardModel = BoardModel(null, null, BOT_PLAYER_ID)

    private var isBotGame: Boolean = true


    fun start(ownPlayerUid: String, isBotGame: Boolean) {
        if (_player.value != null) {

            return
        }

        this.isBotGame = isBotGame

        val boardModel = BoardModel(null, null, ownPlayerUid)
        loadPlayer(ownPlayerUid)
        initializeShips(boardModel)
        boardModel.revealShips()
        _board.value = boardModel

        if (isBotGame) {
            initializeShips(botBoardModel)
        }
    }

    private fun loadPlayer(uid: String) = viewModelScope.launch {
        val result = playerRepository.findByUid(uid)
        if (result is DataResult.Success) {
            _player.value = result.data
        } else if (result is DataResult.Error) {
            throw result.exception
        }
    }

    private fun initializeShips(boardModel: BoardModel) {
        val ships = createShips()
        boardModel.ships.value = ships
        boardModel.randomizeShipPositions()
    }

    private fun createShips(): MutableList<ShipModel> {
//        val shipsSizes = arrayOf(4, 3, 3, 2, 2, 2, 1, 1, 1, 1) // TODO: Refactor?
        val shipsSizes = mutableListOf(4, 4) // TODO: Refactor?
        return shipsSizes.map { size ->
            ShipModel(
                0,
                0,
                size,
                Direction.UP,
                null,
                false,
                directionLogic
            )
        }.toMutableList()
    }

    fun getShipAt(cell: Cell): ShipModel? {
        return _board.value!!.ships.value!!.find { ship ->
            ship.getShipCells().contains(cell)
        }
    }

    fun rotateAround(ship: ShipModel, cell: Cell) {
        ship.rotateAround(cell)
        _board.value!!.updateShipPositionValidity()
        _board.value = _board.value
    }

    fun moveShipTo(ship: ShipModel, cell: Cell) {
        val oldX = ship.x
        val oldY = ship.y

        ship.x = cell.x
        ship.y = cell.y

        if (ship.isShipCompletelyOnBoard()) {
            _board.value!!.updateShipPositionValidity()
            _board.value = _board.value
            return
        }
        ship.x = oldX
        ship.y = oldY
    }

    fun isBoardInValidState(): Boolean {
        return _board.value!!.isBoardInValidState()
    }

    fun startGame() = viewModelScope.launch {

        val game = Game(
            System.currentTimeMillis(),
            GameState.ACTIVE
        )

        if (isBotGame) {
            game.defenderUid = _player.value!!.uid
            game.attackerUid = BOT_PLAYER_ID
            game.playerAtTurnUid = _player.value!!.uid

            saveGame(game)


            _board.value!!.gameUid = game.uid
            game.playerAtTurnUid = _player.value!!.uid
            saveBoard(_board.value!!, game)


            saveShipsToBoard(_board.value!!.uid!!, _board.value!!.ships.value!!)


            botBoardModel.gameUid = game.uid
            botBoardModel.playerUid = BOT_PLAYER_ID
            saveBoard(botBoardModel, game)


            saveShipsToBoard(botBoardModel.uid!!, botBoardModel.ships.value!!)


            _gameReadyEvent.value = Event(game)
            return@launch
        }

        val openGame = findOpenGame()
        if (openGame == null) {
            game.defenderUid = _player.value!!.uid
            game.playerAtTurnUid = _player.value!!.uid
            saveGame(game)


            _board.value!!.gameUid = game.uid
            _board.value!!.playerUid = _player.value!!.uid
            saveBoard(_board.value!!, game)


            saveShipsToBoard(_board.value!!.uid!!, _board.value!!.ships.value!!)

            _waitForEnemyEvent.value = Event(Unit)
            return@launch
        }

        openGame.attackerUid = _player.value!!.uid
        openGame.playerAtTurnUid = _player.value!!.uid

        saveGame(openGame)
        gameRepository.removeFromOpenGames(openGame)

        _board.value!!.gameUid = openGame.uid
        _board.value!!.playerUid = _player.value!!.uid
        saveBoard(_board.value!!, openGame)


        saveShipsToBoard(_board.value!!.uid!!, _board.value!!.ships.value!!)

        _gameReadyEvent.value = Event(openGame)

    }


    private suspend fun saveGame(game: Game) {
        val result = gameRepository.save(game)

        if (result is DataResult.Error) {
            throw result.exception
        }
    }

    private suspend fun saveBoard(boardModel: BoardModel, game: Game) {
        val board = Board(game.uid, boardModel.playerUid!!)


        val result = boardRepository.saveBoard(board)

        boardModel.uid = board.uid
        if (result is DataResult.Error) {
            throw result.exception
        }
    }

    private suspend fun saveShipsToBoard(boardUid: String, shipModels: MutableList<ShipModel>) {
        shipModels
            .map { it.toShip() }
            .forEach {
                it.boardUid = boardUid
                shipRepository.insert(it)
            }

    }

    private suspend fun findOpenGame(): Game? {

        val result = gameRepository.findLatestGameWithNoOpponent(_player.value!!.uid)

        if (result is DataResult.Success) {
            if (result.data == null) {
                return null
            }

            val game = result.data

            game.attackerUid = _player.value!!.uid
            game.playerAtTurnUid = _player.value!!.uid
            return game
        }


        if (result is DataResult.Error) {
            throw result.exception
        }

        throw Exception("Logic Exception. This state should never be reached!")
    }
}
