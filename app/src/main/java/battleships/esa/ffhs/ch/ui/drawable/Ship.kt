package battleships.esa.ffhs.ch.ui.drawable

class Ship (val id : Int, val position: Point?, val size: Int, val direction: Direction?, val hits: Set<Point> = setOf()) {

    val didSink = hits.size == size

    fun rotate() {
        //direction = direction?.getNext()
    }
}
