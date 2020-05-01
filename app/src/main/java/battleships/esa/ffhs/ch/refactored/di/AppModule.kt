package battleships.esa.ffhs.ch.refactored.di

import android.content.Context
import androidx.room.Room
import battleships.esa.ffhs.ch.refactored.data.impl.local.BattleShipsDatabase
import battleships.esa.ffhs.ch.refactored.data.impl.local.player.LocalPlayerDataSource
import battleships.esa.ffhs.ch.refactored.data.player.PlayerDataSource
import battleships.esa.ffhs.ch.refactored.ship.DirectionLogic
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
    @Provides
    fun provideDataBase(context: Context): BattleShipsDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            BattleShipsDatabase::class.java,
            "BattleShips.db"
        ).build()
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
