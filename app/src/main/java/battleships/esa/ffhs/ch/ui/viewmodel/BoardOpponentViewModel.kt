package battleships.esa.ffhs.ch.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import battleships.esa.ffhs.ch.database.GameRepository
import battleships.esa.ffhs.ch.entity.BoardEntity
import battleships.esa.ffhs.ch.entity.GameEntity
import battleships.esa.ffhs.ch.entity.ShipEntity
import battleships.esa.ffhs.ch.model.GameState
import battleships.esa.ffhs.ch.wrapper.Cell
import battleships.esa.ffhs.ch.wrapper.ShotWrapper

class BoardOpponentViewModel(activeGame: GameViewModel, boardEntity: BoardEntity, repository: GameRepository) : BoardViewModel(activeGame, boardEntity) {   // TODO: do not address repository directly

    init {
        initializeShips(activeGame.data.value!!, repository)
        initializeShots(activeGame.data.value!!, repository)
    }

    private fun initializeShips(game: GameEntity, repository: GameRepository) {
        var shipList = repository.getMyShips().value
        if (shipList != null) {
            ships.value = shipList.map { ship ->
                var liveShip = MutableLiveData<ShipEntity>()
                liveShip.value = ship
                ShipViewModel(liveShip, repository.getOpponentShipShots())
            }.toMutableList()
        } else {
            setShips(initShips())
            setShipsRandomly()
        }
    }

    private fun initializeShots(game: GameEntity, repository: GameRepository) {
        var shotList = repository.getOpponentShots().value
        if (shotList != null) {
            shots.value = shotList.map { shot -> ShotWrapper(shot) }.toMutableList()
        }
    }

    // ----------------------------- handle click action from UI -----------------------------

    override fun repeatClickCheck(pointerPosition: Cell): Boolean {
        return false
    }

    override fun identifyShip(pointerPosition: Cell): Boolean {
        if (currentShip != null) {
            return true
        }
        for (ship in getShips()) {
            if (ship.isCellOnShip(pointerPosition) && currentShip == null) {
                currentShip = ship.getShip(pointerPosition)
                offset = currentShip!!.getOffset(pointerPosition)
                return true
            }
        }
        return false
    }

    override fun clickAction(pointerPosition: Cell): Boolean {
        currentShip!!.rotate(pointerPosition)
        shipInvalidPositionValidityCheck()
        currentShip = null
        return true
    }

    override fun moveAction(pointerPosition: Cell): Boolean {
        var oldPos = currentShip!!.getBowCoordinate()
        currentShip!!.set(pointerPosition, offset)
        if (!currentShip!!.isShipCompletelyOnBoard()) {
            currentShip!!.set(
                Cell(
                    oldPos.x,
                    oldPos.y
                )
            )
        }
        shipInvalidPositionValidityCheck()
        return true
    }

    // ----------------------------- take a shot on opponents board -----------------------------

    fun shoot(shot: ShotWrapper): Boolean {
        if (activeGame.data.value!!.state == GameState.ENDED) {
            return true
        }
        var refresh: Boolean = false
        var pointerPosition: Cell = shot.cell
        if (getShots().filter { s -> s.cell == pointerPosition }.count() > 0) {
            return false
        }

        for (ship in getShips()) {
            if (ship.isCellOnShip(pointerPosition)) {
                currentShip = ship.getShip(pointerPosition)
                if (currentShip != null) {
                    hit(currentShip!!, shot)
                    currentShip = null
                }
                refresh = true

            } else {
                hit(shot)
                refresh = true
            }
        }
        if (refresh) {
            endGameCheck()
            return true
        }
        return false
    }

    override fun endGameCheck(): Boolean {
        val gameEnded = super.endGameCheck()
        if (gameEnded && activeGame.data.value!!.state != GameState.ENDED) {
            val tempGame = activeGame
            tempGame.data.value!!.state = GameState.ENDED
            activeGame = tempGame
            return true
        }
        return false
    }

    fun shipInvalidPositionValidityCheck(): Boolean {
        var changed = false
        val overlapList = getOverlappingShips()
        for (ship in getShips()) {
            var isPosValid = true

            if (!ship.isShipCompletelyOnBoard() || overlapList.contains(ship)) {
                isPosValid = false
            }

            if (isPosValid != ship.isPositionValid()) {
                ship.isPositionValid(isPosValid)
                changed = true
            }
        }
        return changed
    }

}
