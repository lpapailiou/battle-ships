package ch.ffhs.esa.battleships.ui.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.iterator
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import ch.ffhs.esa.battleships.BattleShipsApplication
import ch.ffhs.esa.battleships.R
import ch.ffhs.esa.battleships.business.OFFLINE_PLAYER_ID
import ch.ffhs.esa.battleships.data.ConnectivityListener
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.main_activity.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    companion object {
        var skipLogin: Boolean = false
        var navOwnPlayerId: String = OFFLINE_PLAYER_ID
        var navEnemyId: String = ""
        var navGameId: MutableLiveData<String> = MutableLiveData()
        var navIsBotGame: Boolean = false
        var isThisOnForeGround = false
    }

    @Inject
    lateinit var connectivityListener: ConnectivityListener

    var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)

            (application as BattleShipsApplication).appComponent.mainComponent()
                .create().inject(this)

            setContentView(R.layout.main_activity)

            val navController = Navigation.findNavController(
                this,
                R.id.nav_host_fragment
            )

            setupActionBar(navController)

            val auth: FirebaseAuth = FirebaseAuth.getInstance()
            navOwnPlayerId = auth.currentUser?.uid ?: OFFLINE_PLAYER_ID

            if (!hasWifi() || navOwnPlayerId != OFFLINE_PLAYER_ID) {
                skipLogin = true
            }

            connectivityListener.observeConnectivity()
            FirebaseListener(this).listen()


        } catch (e: Exception) {
            e.stackTrace
        }
    }

    override fun onResume() {
        super.onResume()
        isThisOnForeGround = true
    }

    override fun onPause() {
        super.onPause()
        isThisOnForeGround = false
    }


    fun hasWifi(): Boolean {
        val connManager: ConnectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val mWifi: NetworkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        return mWifi.isConnected
    }

    // ----------------------------- navigation -----------------------------

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        this.menu = menu
        val item: MenuItem? = menu?.getItem(2)
        if (item != null) {
            item.isVisible = false
            navGameId.observe(this, Observer {
                item.isVisible = navGameId.value != null
                invalidateOptionsMenu()
            })
        }

        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        auth.addAuthStateListener {
            invalidateConnectionIcon()
        }

        connectivityListener.connected.observe(this, Observer {
            invalidateConnectionIcon()
        })

        if (!skipLogin) {
            setMenuVisible(false)
        }
        return true
    }

    fun invalidateConnectionIcon() {
        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        val item: MenuItem? = menu?.getItem(0)
        if (auth.currentUser != null && connectivityListener.connected.value == true) {
            item?.icon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_wifi_24)
        } else {
            item?.icon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_wifi_off_24)
        }
    }

    fun setMenuVisible(isVisible: Boolean) {
        if (menu != null) {
            for (item in menu!!) {
                item.isVisible = isVisible
            }
            if (isVisible) {
                navGameId.value = navGameId.value
            } else {
                invalidateOptionsMenu()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val navController = Navigation.findNavController(
            this,
            R.id.nav_host_fragment
        )
        val navigated = NavigationUI.onNavDestinationSelected(item!!, navController)
        return navigated || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = Navigation.findNavController(
            this,
            R.id.nav_host_fragment
        )
        return navController.navigateUp()
    }

    override fun onBackPressed() {
        try {
            super.onBackPressed()
        } catch (e: Exception) {
            e.stackTrace
        }
    }

    private fun setupActionBar(navController: NavController) {
        NavigationUI.setupActionBarWithNavController(this, navController, container)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}

