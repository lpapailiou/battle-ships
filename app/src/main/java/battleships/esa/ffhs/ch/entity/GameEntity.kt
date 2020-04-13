package battleships.esa.ffhs.ch.entity

import androidx.room.*
import battleships.esa.ffhs.ch.model.GameState

@Entity(tableName = "game_table")
class GameEntity (

    var lastChange: String,
    var state: GameState,
    var result: Int,

    var isCurrentGame: Boolean,
    var isMyTurn: Boolean,
    var isMyBoardVisible: Boolean,
    var opponentName: String?
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true)
    var game_id: Int = 0
}