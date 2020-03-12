package battleships.esa.ffhs.ch.ui.viewmodel

import battleships.esa.ffhs.ch.ui.drawable.Direction
import battleships.esa.ffhs.ch.ui.drawable.Point

class ShipViewModel(val id : Int, val position: Point, val size: Int, val direction: Direction?, val hits: Set<Point> = setOf()) {


}
