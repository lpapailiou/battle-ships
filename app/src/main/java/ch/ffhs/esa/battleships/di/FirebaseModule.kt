package ch.ffhs.esa.battleships.di

import ch.ffhs.esa.battleships.data.ConnectivityListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object FirebaseModule {
    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Singleton
    @Provides
    fun provideFirebaseDatabase(): FirebaseDatabase {
        return Firebase.database
    }

    @Singleton
    @Provides
    fun provideConnectivityListener(
        firebaseDatabase: FirebaseDatabase
    ): ConnectivityListener {
        return ConnectivityListener(firebaseDatabase)
    }


}
