package battleships.esa.ffhs.ch.refactored.di

import android.content.Context
import battleships.esa.ffhs.ch.refactored.data.board.BoardRepository
import battleships.esa.ffhs.ch.refactored.data.game.GameRepository
import battleships.esa.ffhs.ch.refactored.data.player.PlayerRepository
import battleships.esa.ffhs.ch.refactored.data.ship.ShipRepository
import battleships.esa.ffhs.ch.refactored.data.shot.ShotRepository
import battleships.esa.ffhs.ch.refactored.di.boardpreparation.BoardPreparationComponent
import battleships.esa.ffhs.ch.refactored.di.game.BridgeComponent
import battleships.esa.ffhs.ch.refactored.di.game.GameComponent
import battleships.esa.ffhs.ch.refactored.ui.auth.LoginActivity
import battleships.esa.ffhs.ch.refactored.ui.auth.presenter.EmailLogin
import battleships.esa.ffhs.ch.refactored.ui.auth.presenter.GoogleLogin
import battleships.esa.ffhs.ch.refactored.ui.main.IntroModel
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
        FirebaseModule::class
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

    val playerRepository: PlayerRepository

    val gameRepository: GameRepository

    val boardRepository: BoardRepository

    val shipRepository: ShipRepository

    val shotRepository: ShotRepository

    fun inject(IntroModel: IntroModel)
    fun inject(loginActivity: LoginActivity)
    fun inject(authPresenter: EmailLogin)
    fun inject(authPresenter: GoogleLogin)
}

