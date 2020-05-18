package ch.ffhs.esa.battleships.di

import ch.ffhs.esa.battleships.data.game.GameRepository
import ch.ffhs.esa.battleships.data.game.GameRepositoryImpl
import ch.ffhs.esa.battleships.data.player.PlayerRepository
import ch.ffhs.esa.battleships.data.player.PlayerRepositoryImpl
import ch.ffhs.esa.battleships.data.ship.ShipRepository
import ch.ffhs.esa.battleships.data.ship.ShipRepositoryImpl
import ch.ffhs.esa.battleships.data.shot.ShotRepository
import ch.ffhs.esa.battleships.data.shot.ShotRepositoryImpl
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

    @Singleton
    @Binds
    abstract fun bindShotRepository(repository: ShotRepositoryImpl): ShotRepository
}
