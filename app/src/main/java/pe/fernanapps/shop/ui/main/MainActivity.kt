package pe.fernanapps.shop.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import pe.fernanapps.shop.R
import pe.fernanapps.shop.databinding.ActivityMainBinding
import pe.fernanapps.shop.databinding.BadgeCustomBinding
import pe.fernanapps.shop.utils.UIUtils.themeAccentColor
import java.util.concurrent.atomic.AtomicBoolean

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    private lateinit var navController: NavController
    lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var bin: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bin = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bin.root)

        initViews()
        initActions()
        addNumberNotification(1)

    }


    fun addNumberNotification(value: Int) {
        val badge = bin.bottomNavView.getOrCreateBadge(R.id.navigation_page_notification);
        badge.isVisible = true;
        badge.number = value;
        //badge.backgroundColor = this.themeAccentColor()
    }

    private fun initViews() {

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_page_home,
                R.id.navigation_page_cart,
                R.id.navigation_page_notification,
                R.id.navigation_page_profile
            ),
            bin.drawerLayout
        )
        //val appBarConfiguration = AppBarConfiguration(navController.graph)
        //setupWithNavController(bin.toolbar, navController, appBarConfiguration)

        setSupportActionBar(bin.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""


        //supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_home)

        //setSupportActionBar(bin.toolbar)
        //setupActionBarWithNavController(navController, appBarConfiguration)
        bin.bottomNavView.setupWithNavController(navController)
        bin.navView.setupWithNavController(navController)

    }

    private fun initActions() {
        with(bin) {
            toolbar.setNavigationOnClickListener {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }

    }

    override fun onResume() {
        super.onResume()
        if (navigateCartFragment.get()) {
            navigateCartFragment.set(false)
            navigateFix(R.id.navigation_page_cart)
        }
        if(navigateProfileDetailsFragment.get()){
            navigateProfileDetailsFragment.set(false)
            navigateFix(R.id.navigation_page_personal_details)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true

            }
            R.id.menu_user -> {
                navigateFix(R.id.navigation_page_profile)
                true
            }
            R.id.menu_filter -> {
                navigateFix(R.id.navigation_page_categories)
                true
            }
            R.id.menu_search -> {
                navigateFix(R.id.navigation_page_products)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun navigateFix(@IdRes resId: Int){
        if (navController.currentDestination?.id != resId) {
            //navController.popBackStack(currentDestination!!, false)
            navController.popBackStack()
            navController.navigate(resId)
        }
    }


    companion object {
        val navigateCartFragment = AtomicBoolean(false)
        val navigateProfileDetailsFragment = AtomicBoolean(false)

    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return if (item.itemId == R.id.termsAndConditions) {
//            val action = NavGraphDirections.actionGlobalTermsFragment()
//            navController.navigate(action)
//            true
//        } else {
//            item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
//        }
//    }
//
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (!navController.popBackStack()) {
            super.onBackPressed()
        }
    }
}