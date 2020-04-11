package battleships.esa.ffhs.ch.model

const val BOARD_SIZE = 10
const val STRICT_OVERLAP_RULE = true  // if true: no ships are allowed to touch each other, else: ships can touch, but not overlap
const val WON_GAME_VALUE = 5


/*
    Game
        Status
        Users
        Board opponent
            Shots
                Coordinate
                Is hit
                Is drawable
            Ships
                Id
                Bow Coordinate
                Direction
                Size
                Hits
                    Coordinate
                    Is hit
                    Is drawable
                Has valid position
                Is hidden
        Board mine
            Shots
                Coordinate
                Is hit
                Is drawable
            Ships
                Id
                Bow Coordinate
                Direction
                Size
                Hits
                    Coordinate
                    Is hit
                    Is drawable
                Has valid position
                Is hidden

 */
