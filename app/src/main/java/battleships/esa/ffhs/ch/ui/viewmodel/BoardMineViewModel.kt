package battleships.esa.ffhs.ch.ui.viewmodel

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import battleships.esa.ffhs.ch.database.GameRepository
import battleships.esa.ffhs.ch.entity.BoardEntity
import battleships.esa.ffhs.ch.entity.GameEntity
import battleships.esa.ffhs.ch.entity.ShipEntity
import battleships.esa.ffhs.ch.model.GameState
import battleships.esa.ffhs.ch.ui.main.MainActivity
import battleships.esa.ffhs.ch.wrapper.Cell
import battleships.esa.ffhs.ch.wrapper.ShotWrapper

class BoardMineViewModel(activeGame: GameViewModel, boardEntity: BoardEntity, repository: GameRepository) : BoardViewModel(activeGame, boardEntity) {

    init {
        initializeShips(activeGame.data.value!!, repository)
        initializeShots(activeGame.data.value!!, repository)
        getShips().forEach { it.hide(true) }
    }

    private fun initializeShips(game: GameEntity, repository: GameRepository) {
        var shipList = repository.getMyShips().value
        if (shipList != null) {
            ships.value = shipList.map { ship ->
                var liveShip = MutableLiveData<ShipEntity>()
                liveShip.value = ship
                ShipViewModel(liveShip, repository.getMyShipShots())
            }.toMutableList()

        } else {
            setShips(initShips())
            setShipsRandomly()
        }
    }

    private fun initializeShots(game: GameEntity, repository: GameRepository) {
        var shotList = repository.getMyShots().value
        if (shotList != null) {
            shots.value = shotList.map { shot -> ShotWrapper(shot) }.toMutableList()
        }
    }

    // ----------------------------- handle click action from UI -----------------------------

    override fun repeatClickCheck(pointerPosition: Cell): Boolean {
        return (getShots().filter { s -> s.cell == pointerPosition }.count() > 0)
    }

    override fun identifyShip(pointerPosition: Cell): Boolean {
        return false
    }

    override fun clickAction(pointerPosition: Cell, act: Activity): Boolean {
        var handled: Boolean

        var shot = ShotWrapper(pointerPosition)

        currentShip = findShipAtPosition(pointerPosition)

        if (currentShip != null) {
            hit(currentShip!!, shot)
            (act as MainActivity).vibrate()
            currentShip = null
        } else {
            hit(shot)
        }
        handled = true

        if (!endGameCheck() && handled) {
            randomShot()
        }

        return handled
    }

    override fun endGameCheck(): Boolean {
        val gameEnded = super.endGameCheck()
        if (gameEnded && activeGame.data.value!!.state != GameState.ENDED) {
            val tempGame = activeGame.data.value!!
            tempGame.result = 1
            tempGame.state = GameState.ENDED
            activeGame.data.value =  tempGame
            return true
        }
        return false
    }

    fun findShipAtPosition(position: Cell): ShipViewModel? {
        for (ship in getShips()) {
            if (ship.isCellOnShip(position)) {

                return ship
            }
        }

        return null
    }

    override fun moveAction(pointerPosition: Cell): Boolean {
        var handled = false
        return handled
    }

    // ----------------------------- trigger shot on opponents board -----------------------------

    fun randomShot(): Boolean {
        if (activeGame.data.value!!.state != GameState.ENDED) {
            var success = activeGame.getOpponentBoard().shoot(
                ShotWrapper(
                    Cell(0, 0).getRandomCell()
                )
            )
            if (!success) {
                // TODO: refactor, replace with bot (and more stable version)
                randomShot()
            } else {
                return true
            }
        } else {
            return true
        }
        return false
    }


}
