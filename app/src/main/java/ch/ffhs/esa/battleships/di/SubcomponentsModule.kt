package ch.ffhs.esa.battleships.di

import ch.ffhs.esa.battleships.di.boardpreparation.BoardPreparationComponent
import ch.ffhs.esa.battleships.di.game.BridgeComponent
import ch.ffhs.esa.battleships.di.game.GameComponent
import ch.ffhs.esa.battleships.di.login.LoginComponent
import dagger.Module


@Module(
    subcomponents = [
        BoardPreparationComponent::class,
        GameComponent::class,
        BridgeComponent::class,
        LoginComponent::class
    ]
)
object SubcomponentsModule
