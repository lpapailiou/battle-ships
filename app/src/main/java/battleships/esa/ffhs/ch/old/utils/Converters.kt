package battleships.esa.ffhs.ch.old.utils

import androidx.room.TypeConverter
import battleships.esa.ffhs.ch.refactored.data.ship.Direction
import battleships.esa.ffhs.ch.old.model.GameState
import java.sql.Date

class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }

    @TypeConverter
    fun fromGameState(value: GameState): Int {
        return value.id
    }

    @TypeConverter
    fun toGameState(value: Int): GameState {
        return when(value) {
            0 -> GameState.INIT
            1 -> GameState.PREPARATION
            2 -> GameState.ACTIVE
            3 -> GameState.ENDED
            else -> GameState.INIT
        }
    }

    @TypeConverter
    fun fromDirection(value: Direction): Int {
        return value.id
    }

    @TypeConverter
    fun toDirection(value: Int): Direction {
        return when(value) {
            0 -> Direction.UP
            1 -> Direction.DOWN
            2 -> Direction.LEFT
            3 -> Direction.RIGHT
            4 -> Direction.UP_RIGHT
            5 -> Direction.UP_LEFT
            6 -> Direction.DOWN_RIGHT
            7 -> Direction.DOWN_LEFT
            else -> Direction.UP
        }
    }

}
