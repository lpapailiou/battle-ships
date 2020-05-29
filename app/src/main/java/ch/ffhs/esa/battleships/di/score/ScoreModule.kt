package ch.ffhs.esa.battleships.di.score

import androidx.lifecycle.ViewModel
import ch.ffhs.esa.battleships.business.score.ScoreViewModel
import ch.ffhs.esa.battleships.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ScoreModule {
    @Binds
    @IntoMap
    @ViewModelKey(ScoreViewModel::class)
    abstract fun bindViewModel(viewModel: ScoreViewModel): ViewModel

}
