package battleships.esa.ffhs.ch.refactored.business.board

import androidx.lifecycle.MutableLiveData
import battleships.esa.ffhs.ch.refactored.business.ship.ShipModel
import battleships.esa.ffhs.ch.refactored.business.shot.ShotModel

class BoardModel(
    val id: Long,
    val gameId: Long,
    val playerId: Long
) {

    val ships = MutableLiveData<MutableList<ShipModel>>()
    val shots = MutableLiveData<MutableList<ShotModel>>()

    init {
        ships.value = mutableListOf<ShipModel>()
        shots.value = mutableListOf<ShotModel>()
    }


}
