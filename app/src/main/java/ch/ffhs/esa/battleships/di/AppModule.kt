package ch.ffhs.esa.battleships.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import ch.ffhs.esa.battleships.business.BOT_PLAYER_ID
import ch.ffhs.esa.battleships.business.OFFLINE_PLAYER_ID
import ch.ffhs.esa.battleships.business.ship.DirectionLogic
import ch.ffhs.esa.battleships.data.board.BoardDataSource
import ch.ffhs.esa.battleships.data.game.GameDataSource
import ch.ffhs.esa.battleships.data.source.local.BattleShipsDatabase
import ch.ffhs.esa.battleships.data.source.local.board.LocalBoardDataSource
import ch.ffhs.esa.battleships.data.source.local.game.LocalGameDataSource
import ch.ffhs.esa.battleships.data.source.local.player.LocalPlayerDataSource
import ch.ffhs.esa.battleships.data.source.local.ship.LocalShipDataSource
import ch.ffhs.esa.battleships.data.source.local.shot.LocalShotDataSource
import ch.ffhs.esa.battleships.data.player.PlayerDataSource
import ch.ffhs.esa.battleships.data.ship.ShipDataSource
import ch.ffhs.esa.battleships.data.shot.ShotDataSource
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
object AppModule {
    
    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class LocalPlayerDataSource

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class LocalGameDataSource

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class LocalBoardDataSource

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class LocalShipDataSource

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class LocalShotDataSource


    @JvmStatic
    @Singleton
    @LocalPlayerDataSource
    @Provides
    fun provideLocalPlayerDataSource(
        database: BattleShipsDatabase,
        ioDispatcher: CoroutineDispatcher
    ): PlayerDataSource {
        return LocalPlayerDataSource(
            database.playerDao(), ioDispatcher
        )
    }

    @JvmStatic
    @Singleton
    @LocalGameDataSource
    @Provides
    fun provideLocalGameDataSource(
        database: BattleShipsDatabase,
        ioDispatcher: CoroutineDispatcher
    ): GameDataSource {
        return LocalGameDataSource(
            database.gameDao(), ioDispatcher
        )
    }

    @JvmStatic
    @Singleton
    @LocalBoardDataSource
    @Provides
    fun provideLocalBoardDataSource(
        database: BattleShipsDatabase,
        ioDispatcher: CoroutineDispatcher
    ): BoardDataSource {
        return LocalBoardDataSource(
            database.boardDao(), ioDispatcher
        )
    }

    @JvmStatic
    @Singleton
    @LocalShipDataSource
    @Provides
    fun provideLocalShipDataSource(
        database: BattleShipsDatabase,
        ioDispatcher: CoroutineDispatcher
    ): ShipDataSource {
        return LocalShipDataSource(
            database.shipDao(), ioDispatcher
        )
    }

    @JvmStatic
    @Singleton
    @LocalShotDataSource
    @Provides
    fun provideLocalShotDataSource(
        database: BattleShipsDatabase,
        ioDispatcher: CoroutineDispatcher
    ): ShotDataSource {
        return LocalShotDataSource(
            database.shotDao(), ioDispatcher
        )
    }


    @JvmStatic
    @Singleton
    @Provides
    fun provideDataBase(context: Context): BattleShipsDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            BattleShipsDatabase::class.java,
            "BattleShips.db"
        )
            .fallbackToDestructiveMigration()
            .addCallback(CALLBACK)
            .build()
    }


    private val CALLBACK = object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            db.execSQL("delete from Ship")
            db.execSQL("delete from Board")
            db.execSQL("delete from Game")
            db.execSQL("delete from Player")
            db.execSQL(
                "INSERT INTO Player values(null, :offlinePlayerId, 'You')",
                arrayOf(OFFLINE_PLAYER_ID)
            )
            db.execSQL(
                "INSERT INTO Player values(null, :botPlayerId, 'BOT')",
                arrayOf(BOT_PLAYER_ID)
            )
        }
    }


    @JvmStatic
    @Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO

    @Singleton
    @Provides
    fun provideDirectionLogic(): DirectionLogic {
        return DirectionLogic()
    }

}
