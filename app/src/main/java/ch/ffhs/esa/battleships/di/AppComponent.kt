package ch.ffhs.esa.battleships.di

import android.content.Context
import ch.ffhs.esa.battleships.data.board.BoardRepository
import ch.ffhs.esa.battleships.data.game.GameRepository
import ch.ffhs.esa.battleships.data.player.PlayerRepository
import ch.ffhs.esa.battleships.data.ship.ShipRepository
import ch.ffhs.esa.battleships.data.shot.ShotRepository
import ch.ffhs.esa.battleships.di.boardpreparation.BoardPreparationComponent
import ch.ffhs.esa.battleships.di.game.BridgeComponent
import ch.ffhs.esa.battleships.di.game.GameComponent
import ch.ffhs.esa.battleships.di.login.LoginComponent
import ch.ffhs.esa.battleships.di.login.LoginModule
import ch.ffhs.esa.battleships.di.score.ScoreComponent
import ch.ffhs.esa.battleships.di.score.ScoreModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        AppModule::class,
        SubcomponentsModule::class,
        AppModuleBinds::class,
        ViewModelBuilderModule::class,
        FirebaseModule::class,
        LoginModule::class,
        ScoreModule::class
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

    fun bridgeComponent(): BridgeComponent.Factory

    fun loginComponent(): LoginComponent.Factory

    fun scoreComponent(): ScoreComponent.Factory

    val playerRepository: PlayerRepository

    val gameRepository: GameRepository

    val boardRepository: BoardRepository

    val shipRepository: ShipRepository

    val shotRepository: ShotRepository

}

