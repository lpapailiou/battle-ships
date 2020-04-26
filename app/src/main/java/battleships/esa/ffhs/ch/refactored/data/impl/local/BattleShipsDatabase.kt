package battleships.esa.ffhs.ch.refactored.data.impl.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import battleships.esa.ffhs.ch.old.dao.BoardDao
import battleships.esa.ffhs.ch.old.utils.Converters
import battleships.esa.ffhs.ch.refactored.data.MIGRATION_1_2
import battleships.esa.ffhs.ch.refactored.data.board.Board
import battleships.esa.ffhs.ch.refactored.data.game.Game
import battleships.esa.ffhs.ch.refactored.data.impl.local.game.GameDao
import battleships.esa.ffhs.ch.refactored.data.impl.local.player.PlayerDao
import battleships.esa.ffhs.ch.refactored.data.player.Player
import battleships.esa.ffhs.ch.refactored.data.ship.Ship
import battleships.esa.ffhs.ch.refactored.data.shot.Shot

@Database(
    entities = [Game::class, Board::class, Ship::class, Shot::class, Player::class],
    version = 9,
    exportSchema = false
)
@TypeConverters(Converters::class)
public abstract class BattleShipsDatabase : RoomDatabase() {

    abstract fun gameDao(): GameDao

    abstract fun boardDao(): BoardDao

    abstract fun playerDao(): PlayerDao

    companion object {

        @Volatile
        private var INSTANCE: BattleShipsDatabase? = null

        fun getDatabase(context: Context): BattleShipsDatabase {
            val tempInstance =
                INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BattleShipsDatabase::class.java,
                    "game_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
