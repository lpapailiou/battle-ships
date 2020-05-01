package battleships.esa.ffhs.ch.refactored.data.ship

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import battleships.esa.ffhs.ch.refactored.data.player.Player
import battleships.esa.ffhs.ch.refactored.ship.Ship

@Entity(
    foreignKeys = [ForeignKey(
        entity = Player::class,
        parentColumns = ["id"],
        childColumns = ["boardId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Ship(
    override var x: Int,
    override var y: Int,
    override val size: Int,
    override var direction: Direction,
    @ColumnInfo(index = true) override val boardId: Int
) : Ship {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true)
    override var id: Int = 0
}
