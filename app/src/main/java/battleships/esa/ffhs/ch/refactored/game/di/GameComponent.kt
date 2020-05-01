package battleships.esa.ffhs.ch.refactored.game.di

import battleships.esa.ffhs.ch.refactored.game.GameFragment
import dagger.Subcomponent

@Subcomponent(modules = [GameModule::class])
interface GameComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): GameComponent
    }

    fun inject(fragment: GameFragment)
}
