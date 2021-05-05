package com.example.sqliteapp.activities.login

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.sqliteapp.R
import com.example.sqliteapp.databinding.ActivityLoggedIn2ndBinding
import com.example.sqliteapp.loginFragments.profile.ProfileViewModel
import com.example.sqliteapp.loginFragments.tests.TestsViewModel
import com.example.sqliteapp.loginFragments.vaccinations.VaccinationsViewModel

//activity behind the fragments, provides navigation and passes on necessary information in a bundle
//to the fragments.

class LoggedInActivity : AppCompatActivity() {
    private var mAppBarConfiguration: AppBarConfiguration? = null
    private lateinit var fBinding: ActivityLoggedIn2ndBinding
    private val binding get() = fBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val loggedInActivityViewModel: LoggedInActivityViewModel = ViewModelProvider(this).get(LoggedInActivityViewModel::class.java)
        val profileViewModel: ProfileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        val vaccinationsViewModel: VaccinationsViewModel = ViewModelProvider(this).get(VaccinationsViewModel::class.java)
        val testsViewModel: TestsViewModel = ViewModelProvider(this).get(TestsViewModel::class.java)
        loggedInActivityViewModel.initFragments(
                intent.extras?.getString("IDNP"),
                intent.extras?.getString("fn"),
                intent.extras?.getString("sn"),
                profileViewModel,
                vaccinationsViewModel,
                testsViewModel
        )
        fBinding = ActivityLoggedIn2ndBinding.inflate(this.layoutInflater)
        setSupportActionBar(binding.appBarMain.toolbar)
        mAppBarConfiguration = AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_tests, R.id.nav_gallery)
                .setDrawerLayout(binding.drawerLayout)
                .build()
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration!!)
        NavigationUI.setupWithNavController(binding.navView, navController)
    }

    override fun onResume() {
        super.onResume()
        val view = fBinding.root
        setContentView(view)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.logged_in_2nd, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        return (NavigationUI.navigateUp(navController, mAppBarConfiguration!!)
                || super.onSupportNavigateUp())
    }
}