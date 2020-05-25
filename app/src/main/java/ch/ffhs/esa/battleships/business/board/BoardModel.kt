package ch.ffhs.esa.battleships.business.board

import androidx.lifecycle.MutableLiveData
import ch.ffhs.esa.battleships.business.ship.ShipModel
import ch.ffhs.esa.battleships.business.shot.ShotModel

class BoardModel(
    val uid: String?,
    val gameUid: String?,
    val playerUid: String?
) {

    val ships = MutableLiveData<MutableList<ShipModel>>()
    val shots = MutableLiveData<MutableList<ShotModel>>()

    init {
        ships.value = mutableListOf()
        shots.value = mutableListOf()
    }

}
