package ch.ffhs.esa.battleships.ui.main

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.iterator
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import ch.ffhs.esa.battleships.BattleShipsApplication
import ch.ffhs.esa.battleships.R
import ch.ffhs.esa.battleships.business.OFFLINE_PLAYER_ID
import ch.ffhs.esa.battleships.data.ConnectivityListener
import ch.ffhs.esa.battleships.data.game.Game
import ch.ffhs.esa.battleships.ui.auth.AuthHostFragment
import ch.ffhs.esa.battleships.ui.auth.AuthHostFragmentDirections
import ch.ffhs.esa.battleships.ui.auth.SignUpFragment
import ch.ffhs.esa.battleships.ui.auth.SignUpFragmentDirections
import ch.ffhs.esa.battleships.ui.rules.RulesFragment
import ch.ffhs.esa.battleships.ui.rules.RulesFragmentDirections
import ch.ffhs.esa.battleships.ui.score.ScoreFragment
import ch.ffhs.esa.battleships.ui.score.ScoreFragmentDirections
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.main_activity.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    companion object {
        var skipLogin: Boolean = false
        var navOwnPlayerId: String = OFFLINE_PLAYER_ID
        var navEnemyId: String = ""
        var navGameId: MutableLiveData<String> = MutableLiveData()
        var navIsBotGame: Boolean = false
        var activeGame: Game? = null
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

        } catch (e: Exception) {
            println(e.stackTrace.toString())
        }
    }

    open fun hasWifi(): Boolean {
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
            val item: MenuItem? = menu?.getItem(0)
            if (auth.currentUser != null) {
                item?.icon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_wifi_24)
            } else {
                item?.icon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_wifi_off_24)
            }
        }

        if (!skipLogin) {
            setMenuVisible(false)
        }
        return true
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
        var navigated = false
        if (item == menu?.getItem(2)) {
            var currentFragment: Fragment? = null
            supportFragmentManager.fragments.forEach {
                it.childFragmentManager.fragments.forEach {
                    currentFragment = it
                }
            }
            if (currentFragment != null && navGameId.value != null) {
                if (currentFragment is MainFragment) {
                    val action =
                        MainFragmentDirections.actionMainFragmentToGameHostFragment()
                    (currentFragment as MainFragment).findNavController().navigate(action)
                } else if (currentFragment is ScoreFragment) {
                    val action =
                        ScoreFragmentDirections.actionScoreFragmentToGameHostFragment()
                    (currentFragment as ScoreFragment).findNavController().navigate(action)
                } else if (currentFragment is RulesFragment) {
                    val action =
                        RulesFragmentDirections.actionRulesFragmentToGameHostFragment()
                    (currentFragment as RulesFragment).findNavController().navigate(action)
                } else if (currentFragment is AuthHostFragment) {
                    val action =
                        AuthHostFragmentDirections.actionAuthHostFragmentToGameHostFragment()
                    (currentFragment as AuthHostFragment).findNavController().navigate(action)
                } else if (currentFragment is SignUpFragment) {
                    val action =
                        SignUpFragmentDirections.actionSignUpFragmentToGameHostFragment()
                    (currentFragment as SignUpFragment).findNavController().navigate(action)
                }
            }

        } else {
            val navController = Navigation.findNavController(
                this,
                R.id.nav_host_fragment
            )
            navigated = NavigationUI.onNavDestinationSelected(item!!, navController)
        }
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
            println(e.stackTrace.toString())
        }
    }

    private fun setupActionBar(navController: NavController) {
        NavigationUI.setupActionBarWithNavController(this, navController, container)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}

