package battleships.esa.ffhs.ch.ui.viewmodel

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import battleships.esa.ffhs.ch.model.Direction
import battleships.esa.ffhs.ch.wrapper.Cell
import battleships.esa.ffhs.ch.entity.*
import battleships.esa.ffhs.ch.ui.main.MainActivity.Companion.mainViewModel
import battleships.esa.ffhs.ch.wrapper.ShotWrapper

open class BoardViewModel(val isThisBoardMine: Boolean) : ViewModel() {

    val username = MutableLiveData<String>()

    var ships: MutableLiveData<MutableList<ShipViewModel>> = MutableLiveData<MutableList<ShipViewModel>>()
    var shots: MutableLiveData<MutableList<ShotWrapper>> = MutableLiveData<MutableList<ShotWrapper>>()

    var currentShip: ShipViewModel? = null
    var offset = Cell(0, 0)

    // ----------------------------- game handling -----------------------------
    fun getObservableShips(): LiveData<List<ShipViewModel>> {
        return ships as LiveData<List<ShipViewModel>>
    }

    fun getObservableShots(): LiveData<List<ShotWrapper>> {
        return shots as LiveData<List<ShotWrapper>>
    }

    fun getShips(): List<ShipViewModel> {
        return ships.value!!
    }

    fun getShots(): List<ShotWrapper> {
        if (shots.value != null) {
            return shots.value as List<ShotWrapper>
        }
        return listOf()
    }

    fun setShips(newShips: List<ShipViewModel>) {
        var newShipList = mutableListOf<ShipViewModel>()
        newShipList.addAll(newShips)
        ships.value = newShipList
    }

    fun addShot(shot: ShotWrapper) {
        var shotList = mutableListOf<ShotWrapper>()
        if (shots.value != null) {
            shotList.addAll(shots.value as Collection<ShotWrapper>)
        }
        shotList.add(shot)
        shots.value = shotList      // TODO: not working yet
    }

    open fun endGameCheck(): Boolean {
        var sunkenShips = getShips().filter { s -> s.isSunken() }.count()
        return (sunkenShips == getShips().size)
    }

    fun validateStart(): Boolean {
        if (getShips().filter { s -> !s.isPositionValid() }.count() > 0) {
            return false
        }
        return true
    }

    // ----------------------------- handle click action from UI -----------------------------
    open fun releaseShip() {
        currentShip = null
        offset = Cell(0, 0)
    }

    open fun repeatClickCheck(pointerPosition: Cell): Boolean {
        return false
    }

    open fun identifyShip(pointerPosition: Cell): Boolean {
        return false
    }

    open fun clickAction(pointerPosition: Cell): Boolean {
        return false
    }

    open fun clickAction(pointerPosition: Cell, act: Activity): Boolean {
        return false
    }

    open fun moveAction(pointerPosition: Cell): Boolean {
        return false
    }

    // ----------------------------- shot handling -----------------------------

    protected fun hit(shot: ShotWrapper) {
        hit(null, shot)
    }

    protected fun hit(ship: ShipViewModel?, shot: ShotWrapper) {
        if (ship != null) {
            shot.isHit(true)
            ship.hit(shot)
        }
        addShot(shot)
    }

    fun getOverlappingShips(): HashSet<ShipViewModel> {
        val overlappingShips = hashSetOf<ShipViewModel>()
        var shipList = getShips()
        for (ship in shipList) {
            if (overlappingShips.contains(ship)) continue

            for (otherShip in shipList) {
                if (ship == otherShip) continue

                if (ship.isWithinOccupiedAreaOfOtherShip(otherShip)) {
                    overlappingShips.add(ship)
                    overlappingShips.add(otherShip)
                }
            }
        }
        return overlappingShips
    }

    // ----------------------------- initialization of ships -----------------------------

    protected fun initShips(): List<ShipViewModel> {
        val newShips =  mainViewModel.getShips(isThisBoardMine)
        if (newShips.value != null) {
            return newShips.value!!.map { ship ->
                ShipViewModel(ship)
            }.toList()
        }
        return listOf()
    }

    // TODO: unstable, should be refactored
    fun setShipsRandomly() {
        getShips().forEach { s -> s.setRandomly() }
        var overlapList = getOverlappingShips()
        while (!overlapList.isEmpty()) {
            overlapList.first().setRandomly()
            overlapList = getOverlappingShips()
        }
    }
}
