package ch.ffhs.esa.battleships.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ch.ffhs.esa.battleships.data.Converters
import ch.ffhs.esa.battleships.data.board.Board
import ch.ffhs.esa.battleships.data.game.Game
import ch.ffhs.esa.battleships.data.player.Player
import ch.ffhs.esa.battleships.data.ship.Ship
import ch.ffhs.esa.battleships.data.shot.Shot
import ch.ffhs.esa.battleships.data.source.local.board.BoardDao
import ch.ffhs.esa.battleships.data.source.local.game.GameDao
import ch.ffhs.esa.battleships.data.source.local.player.PlayerDao
import ch.ffhs.esa.battleships.data.source.local.ship.ShipDao
import ch.ffhs.esa.battleships.data.source.local.shot.ShotDao

@Database(
    entities = [Game::class, Board::class, Ship::class, Shot::class, Player::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class BattleShipsDatabase : RoomDatabase() {

    abstract fun gameDao(): GameDao

    abstract fun boardDao(): BoardDao

    abstract fun playerDao(): PlayerDao

    abstract fun shipDao(): ShipDao

    abstract fun shotDao(): ShotDao

}
