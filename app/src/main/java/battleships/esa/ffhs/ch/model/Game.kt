package battleships.esa.ffhs.ch.model

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class Game {

    val id = Timestamp(System.currentTimeMillis())     // TODO: maybe add random
    var lastChange = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())       // TODO: maybe somehow update automatically
    var state = GameState.INIT
}