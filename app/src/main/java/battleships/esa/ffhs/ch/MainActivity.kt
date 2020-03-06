package battleships.esa.ffhs.ch

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import battleships.esa.ffhs.ch.ui.main.IntroBoard

class MainActivity : AppCompatActivity() {

    private val SPLASH_TIME_OUT: Long=3000 // 3sec

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        Handler().postDelayed({
            // This method will be executed once the timer is over
            // Start your app main activity
            startActivity(Intent(this,IntroBoard::class.java))
            // close this activity
            finish()
        }, SPLASH_TIME_OUT)
    }


}
