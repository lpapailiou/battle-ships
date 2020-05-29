package ch.ffhs.esa.battleships.di.score

import ch.ffhs.esa.battleships.ui.score.ScoreFragment
import dagger.Subcomponent

@Subcomponent(modules = [ScoreModule::class])
interface ScoreComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): ScoreComponent
    }

    fun inject(fragment: ScoreFragment)


}
