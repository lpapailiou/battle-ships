package battleships.esa.ffhs.ch.entity

import battleships.esa.ffhs.ch.ui.drawable.Point

data class Shot(val point: Point, val player: Player?) {

    var isHit = false
    var drawable: Boolean = true

    fun isHit(hit: Boolean) {
        isHit = hit
    }

    fun undraw() {
        drawable = false
    }

    fun isDrawable(): Boolean {
        return drawable
    }
}
