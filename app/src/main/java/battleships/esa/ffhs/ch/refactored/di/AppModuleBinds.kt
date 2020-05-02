package battleships.esa.ffhs.ch.refactored.di

import battleships.esa.ffhs.ch.refactored.data.game.GameRepository
import battleships.esa.ffhs.ch.refactored.data.game.GameRepositoryImpl
import battleships.esa.ffhs.ch.refactored.data.player.PlayerRepository
import battleships.esa.ffhs.ch.refactored.data.player.PlayerRepositoryImpl
import battleships.esa.ffhs.ch.refactored.data.ship.ShipRepository
import battleships.esa.ffhs.ch.refactored.data.ship.ShipRepositoryImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class AppModuleBinds {

    @Singleton
    @Binds
    abstract fun bindPlayerRepository(repository: PlayerRepositoryImpl): PlayerRepository

    @Singleton
    @Binds
    abstract fun bindGameRepository(repository: GameRepositoryImpl): GameRepository

    @Singleton
    @Binds
    abstract fun bindShipRepository(repository: ShipRepositoryImpl): ShipRepository
}
