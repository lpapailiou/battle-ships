package battleships.esa.ffhs.ch.entity

import battleships.esa.ffhs.ch.ui.drawable.Point

data class Shot(val point: Point, val player: Player?) {

    var isHit = false

    fun isHit(hit: Boolean) {
        isHit = hit
    }
}
