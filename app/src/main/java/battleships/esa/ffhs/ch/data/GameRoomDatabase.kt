package battleships.esa.ffhs.ch.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import battleships.esa.ffhs.ch.entity.GameEntity
import battleships.esa.ffhs.ch.utils.Converters

@Database(entities = arrayOf(GameEntity::class), version = 1, exportSchema = false)       // version needs to be increased if table is altered
@TypeConverters(Converters::class)
public abstract class GameRoomDatabase: RoomDatabase() {

    abstract fun gameDao(): GameDao

    companion object {

        @Volatile
        private var INSTANCE: GameRoomDatabase? = null

        fun getDatabase(context: Context): GameRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GameRoomDatabase::class.java,
                    "game_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}