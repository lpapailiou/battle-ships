package battleships.esa.ffhs.ch.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import battleships.esa.ffhs.ch.entity.CoordinateEntity
import battleships.esa.ffhs.ch.model.Direction
import battleships.esa.ffhs.ch.wrapper.ShotWrapper

class ShipMockDao (newId: Int, newCoordinate: CoordinateEntity, newSize: Int, newDirection: Direction) {  // Dao = Data Access Object

    private val id = MutableLiveData<Int>()
    private var bowCoordinate = MutableLiveData<CoordinateEntity>()
    private val size = MutableLiveData<Int>()
    private var direction = MutableLiveData<Direction>()
    private val hits = MutableLiveData<MutableSet<ShotWrapper>>()
    private var isPositionValid = MutableLiveData<Boolean>()
    private var isHidden = MutableLiveData<Boolean>()

    init {
        id.value = newId
        bowCoordinate.value = newCoordinate
        size.value = newSize
        direction.value = newDirection
        hits.value = mutableSetOf<ShotWrapper>()
        isPositionValid.value = true
        isHidden.value = false
    }

    fun getHitCount(): Int {
        return hits.value!!.size
    }

    fun getBowCoordinate(): CoordinateEntity {
        return bowCoordinate.value!!
    }

    fun setCoordinate(newCoordinate: CoordinateEntity) {
        bowCoordinate.value = newCoordinate
    }

    fun getDirection(): Direction {
        return direction.value!!
    }

    fun setDirection(newDirection: Direction) {
        direction.value = newDirection
    }

    fun getShipSize(): Int {
        return size.value!!
    }

    fun addHit(hit: ShotWrapper) {
        hits.value!!.add(hit)
    }

    fun getHits(): Set<ShotWrapper> {
        return hits.value!!
    }

    fun hide(hide: Boolean) {
        isHidden.value = hide
    }

    fun isHidden(): Boolean {
        return isHidden.value!!
    }

    fun isPositionValid(): Boolean {
        return isPositionValid.value!!
    }

    fun setPositionValid(isValid: Boolean) {
        isPositionValid.value = isValid
    }

    fun getObservableCoordinate(): LiveData<CoordinateEntity> {
        return bowCoordinate
    }

    fun getObservableDirection(): LiveData<Direction> {
        return direction
    }
}
