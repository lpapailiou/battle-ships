package battleships.esa.ffhs.ch.ui.drawable

import battleships.esa.ffhs.ch.entity.Player

data class Shot(val point: Point, val player: Player?) {

    var isHit = false
    var drawable: Boolean = true

    // indicator if hit or water should be drawn
    fun isHit(hit: Boolean) {
        isHit = hit
    }

    // ----------------------------- visibility handling -----------------------------
    // concepts: shots are invisble when whole ship is sunk, because the ship will be
    // painted red. this means, additional overlapping shots would be redundant and less pretty

    fun undraw() {
        drawable = false
    }

    fun isDrawable(): Boolean {
        return drawable
    }
}
