package battleships.esa.ffhs.ch.refactored.di

import android.content.Context
import battleships.esa.ffhs.ch.refactored.boardpreparation.di.BoardPreparationComponent
import battleships.esa.ffhs.ch.refactored.data.player.PlayerRepository
import dagger.BindsInstance
import dagger.Component
import dagger.Module
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

    val playerRepository: PlayerRepository
}


@Module(
    subcomponents = [
        BoardPreparationComponent::class
    ]
)
object SubcomponentsModule
