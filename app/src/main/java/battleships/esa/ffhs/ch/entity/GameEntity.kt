package battleships.esa.ffhs.ch.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import battleships.esa.ffhs.ch.model.GameState

@Entity(tableName = "game_table")
class GameEntity (

    var lastChange: String,
    var state: GameState,
    var result: Int,
    @Embedded(prefix = "opponent_") var opponentBoard: BoardEntity,
    @Embedded(prefix = "mine_") var myBoard: BoardEntity,

    var isCurrentGame: Boolean,
    var isMyTurn: Boolean,
    var isMyBoardVisible: Boolean,
    var opponentName: String?
) {
    @PrimaryKey(autoGenerate = true)
    var game_id: Int = 0
}