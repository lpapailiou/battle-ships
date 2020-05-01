package battleships.esa.ffhs.ch.refactored.di

import battleships.esa.ffhs.ch.refactored.boardpreparation.di.BoardPreparationComponent
import battleships.esa.ffhs.ch.refactored.game.di.GameComponent
import dagger.Module


@Module(
    subcomponents = [
        BoardPreparationComponent::class,
        GameComponent::class
    ]
)
object SubcomponentsModule
