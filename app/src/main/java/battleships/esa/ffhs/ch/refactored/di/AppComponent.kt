package battleships.esa.ffhs.ch.refactored.di

import android.content.Context
import battleships.esa.ffhs.ch.refactored.data.board.BoardRepository
import battleships.esa.ffhs.ch.refactored.data.game.GameRepository
import battleships.esa.ffhs.ch.refactored.data.player.PlayerRepository
import battleships.esa.ffhs.ch.refactored.data.ship.ShipRepository
import battleships.esa.ffhs.ch.refactored.data.shot.ShotRepository
import battleships.esa.ffhs.ch.refactored.di.boardpreparation.BoardPreparationComponent
import battleships.esa.ffhs.ch.refactored.di.game.GameComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        AppModule::class,
        SubcomponentsModule::class,
        AppModuleBinds::class,
        ViewModelBuilderModule::class
//        RoomModule::class
    ]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun boardPreparationComponent(): BoardPreparationComponent.Factory

    fun gameComponent(): GameComponent.Factory

    val playerRepository: PlayerRepository

    val gameRepository: GameRepository

    val boardRepository: BoardRepository

    val shipRepository: ShipRepository

    val shotRepository: ShotRepository
}

