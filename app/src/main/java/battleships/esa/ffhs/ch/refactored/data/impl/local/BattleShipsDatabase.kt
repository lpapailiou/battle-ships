package battleships.esa.ffhs.ch.refactored.data.impl.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import battleships.esa.ffhs.ch.refactored.data.Converters
import battleships.esa.ffhs.ch.refactored.data.board.Board
import battleships.esa.ffhs.ch.refactored.data.game.Game
import battleships.esa.ffhs.ch.refactored.data.impl.local.board.BoardDao
import battleships.esa.ffhs.ch.refactored.data.impl.local.game.GameDao
import battleships.esa.ffhs.ch.refactored.data.impl.local.player.PlayerDao
import battleships.esa.ffhs.ch.refactored.data.impl.local.ship.ShipDao
import battleships.esa.ffhs.ch.refactored.data.impl.local.shot.ShotDao
import battleships.esa.ffhs.ch.refactored.data.player.Player
import battleships.esa.ffhs.ch.refactored.data.ship.Ship
import battleships.esa.ffhs.ch.refactored.data.shot.Shot

@Database(
    entities = [Game::class, Board::class, Ship::class, Shot::class, Player::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
public abstract class BattleShipsDatabase : RoomDatabase() {

    abstract fun gameDao(): GameDao

    abstract fun boardDao(): BoardDao

    abstract fun playerDao(): PlayerDao

    abstract fun shipDao(): ShipDao

    abstract fun shotDao(): ShotDao

}
