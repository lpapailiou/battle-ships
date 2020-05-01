package battleships.esa.ffhs.ch.refactored.di

import android.content.Context
import battleships.esa.ffhs.ch.refactored.boardpreparation.di.BoardPreparationComponent
import battleships.esa.ffhs.ch.refactored.data.player.PlayerRepository
import battleships.esa.ffhs.ch.refactored.game.di.GameComponent
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
}

