package com.example.sqliteapp.activities

import android.graphics.Color
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
import com.example.sqliteapp.ui.vaccinations.VaccinationsViewModel
import com.example.sqliteapp.ui.profile.ProfileViewModel
import com.example.sqliteapp.ui.tests.TestsViewModel

//activity behind the fragments, provides navigation and passes on necessary information in a bundle
//to the fragments.

open class LoggedInActivity : AppCompatActivity() {
    private var mAppBarConfiguration: AppBarConfiguration? = null
    private var extras: Bundle? = null
    private lateinit var fBinding: ActivityLoggedIn2ndBinding
    private val binding get() = fBinding
    private var profileViewModel: ProfileViewModel ?= null
    private var vaccinationsViewModel: VaccinationsViewModel ?= null
    private var testsViewModel: TestsViewModel ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fBinding = ActivityLoggedIn2ndBinding.inflate(this.layoutInflater)
        setSupportActionBar(binding.appBarMain.toolbar)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
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
        val intent = intent
        extras = intent.extras
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        vaccinationsViewModel = ViewModelProvider(this).get(VaccinationsViewModel::class.java)
        testsViewModel = ViewModelProvider(this).get(TestsViewModel::class.java)
        val view = fBinding.root
        setContentView(view)
        testsViewModel?.initList(extras)
        vaccinationsViewModel?.initList(extras)
        profileViewModel?.generateQrCodes(extras)
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