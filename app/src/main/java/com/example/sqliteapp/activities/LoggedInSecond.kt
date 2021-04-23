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
import com.example.sqliteapp.ui.gallery.GalleryViewModel
import com.example.sqliteapp.ui.home.HomeViewModel
import com.example.sqliteapp.ui.tests.TestsViewModel


open class LoggedInSecond : AppCompatActivity() {
    private var mAppBarConfiguration: AppBarConfiguration? = null
    private var extras: Bundle? = null
    private lateinit var fBinding: ActivityLoggedIn2ndBinding
    protected val binding get() = fBinding
    private var homeViewModel: HomeViewModel ?= null
    private var galleryViewModel: GalleryViewModel ?= null
    private var testsViewModel: TestsViewModel ?= null
    private var backGroundRed: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fBinding = ActivityLoggedIn2ndBinding.inflate(this.layoutInflater)
        val view = fBinding.root
        setContentView(view)
        val intent = intent
        extras = intent.extras
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        galleryViewModel = ViewModelProvider(this).get(GalleryViewModel::class.java)
        testsViewModel = ViewModelProvider(this).get(TestsViewModel::class.java)

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
        val view = fBinding.root
        backGroundRed = if (extras?.getBoolean("Background") == true){
            view.setBackgroundColor(Color.parseColor(getString(R.string.RED)))
            true
        } else {
            view.setBackgroundColor(Color.parseColor(getString(R.string.GREEN)))
            false
        }
        testsViewModel?.initList(extras, backGroundRed)
        galleryViewModel?.initList(extras, backGroundRed)
        homeViewModel?.generateQrCodes(extras)
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