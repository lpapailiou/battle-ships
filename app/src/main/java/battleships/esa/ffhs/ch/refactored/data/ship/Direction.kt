package battleships.esa.ffhs.ch.refactored.data.ship

enum class Direction(val tag: String, val x: Int, val y: Int, val id: Int) {

    UP("UP", 0, -1, 0),
    DOWN("DOWN", 0, 1, 1),
    LEFT("LEFT", -1, 0, 2),
    RIGHT("RIGHT", 1, 0,3),

    // directions used for overlap-with-gap-checks:
    UP_RIGHT("UP_RIGHT", 1, -1, 4),
    UP_LEFT("UP_LEFT", -1, -1, 5),
    DOWN_RIGHT("DOWN_RIGHT", 1, 1, 6),
    DOWN_LEFT("DOWN_LEFT", -1, 1, 7);

}
