package battleships.esa.ffhs.ch.refactored

import android.app.Application
import battleships.esa.ffhs.ch.refactored.di.AppComponent
import battleships.esa.ffhs.ch.refactored.di.AppModule
import battleships.esa.ffhs.ch.refactored.di.DaggerAppComponent
import battleships.esa.ffhs.ch.refactored.di.FirebaseModule

class BattleShipsApplication : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }

    companion object {

        private lateinit var mAppComponent: AppComponent

        fun getAppComponent(): AppComponent {
            return mAppComponent
        }
    }

    override fun onCreate() {
        super.onCreate()

        mAppComponent = DaggerAppComponent
            .builder()
            .appModule(AppModule(this))
            .firebaseModule(FirebaseModule())
            .build()

    }
}
