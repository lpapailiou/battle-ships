package battleships.esa.ffhs.ch.ui.main

import android.widget.EditText
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity
import battleships.esa.ffhs.ch.R
import android.widget.Button
import android.content.Intent

class IntroBoard : AppCompatActivity(){

    private var username: EditText? = null
    private var train: Button? = null
    private var online: Button? = null
    private var score: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ic_introboard)

        username = findViewById<EditText>(R.id.username)
        train = findViewById<Button>(R.id.train)
        online = findViewById<Button>(R.id.online)
        score = findViewById<Button>(R.id.score)

        train!!.setOnClickListener{
            val intentTrain = Intent(this, Score::class.java) //TODO: Verify class MainFragment or BoardFragment?
            startActivity(intentTrain)
        }

        online!!.setOnClickListener{
            val intentOnline = Intent(this, Score::class.java) //TODO: Verify class MainFragment or BoardFragment?
            startActivity(intentOnline)
        }

        score!!.setOnClickListener{
            val intentScore = Intent(this, Score::class.java)
        }

    }
}