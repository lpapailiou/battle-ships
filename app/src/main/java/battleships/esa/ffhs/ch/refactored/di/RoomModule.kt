package battleships.esa.ffhs.ch.refactored.di

import android.content.Context
import androidx.room.Room
import battleships.esa.ffhs.ch.refactored.data.impl.local.BattleShipsDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
object RoomModule {

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
}
