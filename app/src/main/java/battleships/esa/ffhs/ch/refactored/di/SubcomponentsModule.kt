package battleships.esa.ffhs.ch.refactored.di

import battleships.esa.ffhs.ch.refactored.di.boardpreparation.BoardPreparationComponent
import battleships.esa.ffhs.ch.refactored.di.game.BridgeComponent
import battleships.esa.ffhs.ch.refactored.di.game.GameComponent
import dagger.Module


@Module(
    subcomponents = [
        BoardPreparationComponent::class,
        GameComponent::class,
        BridgeComponent::class
    ]
)
object SubcomponentsModule
