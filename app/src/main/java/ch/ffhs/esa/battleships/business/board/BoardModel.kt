package ch.ffhs.esa.battleships.business.board

import androidx.lifecycle.MutableLiveData
import ch.ffhs.esa.battleships.business.ship.ShipModel
import ch.ffhs.esa.battleships.business.shot.ShotModel

class BoardModel(
    val id: Long,
    val gameId: Long,
    val playerId: Long
) {

    val ships = MutableLiveData<MutableList<ShipModel>>()
    val shots = MutableLiveData<MutableList<ShotModel>>()

    init {
        ships.value = mutableListOf()
        shots.value = mutableListOf()
    }


}
