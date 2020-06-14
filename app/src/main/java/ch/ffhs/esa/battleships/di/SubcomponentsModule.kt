package ch.ffhs.esa.battleships.di

import ch.ffhs.esa.battleships.di.boardpreparation.BoardPreparationComponent
import ch.ffhs.esa.battleships.di.game.BridgeComponent
import ch.ffhs.esa.battleships.di.game.GameComponent
import ch.ffhs.esa.battleships.di.login.LoginComponent
import ch.ffhs.esa.battleships.di.main.MainComponent
import ch.ffhs.esa.battleships.di.score.ScoreComponent
import dagger.Module


@Module(
    subcomponents = [
        BoardPreparationComponent::class,
        GameComponent::class,
        BridgeComponent::class,
        LoginComponent::class,
        ScoreComponent::class,
        MainComponent::class
    ]
)
object SubcomponentsModule
