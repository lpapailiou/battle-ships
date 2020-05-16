package battleships.esa.ffhs.ch.refactored.di

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LoginModule(context: Context) {

    private val mContext = context

    @Singleton
    @Provides
    fun provideContext(): Context {
        return mContext
    }
}