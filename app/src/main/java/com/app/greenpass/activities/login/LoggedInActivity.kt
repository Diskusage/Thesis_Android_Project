package com.app.greenpass.activities.login

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.PopupMenu
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.app.greenpass.R
import com.app.greenpass.activities.locale.BaseActivity
import com.app.greenpass.databinding.ActivityLoggedIn2ndBinding
import com.app.greenpass.loginFragments.profile.ProfileViewModel
import com.app.greenpass.loginFragments.tests.TestsViewModel
import com.app.greenpass.loginFragments.vaccinations.VaccinationsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


//activity behind the fragments, provides navigation and passes on necessary information in a bundle
//to the fragments.

class LoggedInActivity : BaseActivity(), PopupMenu.OnMenuItemClickListener  {
    private var mAppBarConfiguration: AppBarConfiguration? = null
    private lateinit var fBinding: ActivityLoggedIn2ndBinding
    private val binding get() = fBinding
    private lateinit var loggedInActivityViewModel: LoggedInActivityViewModel
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var vaccinationsViewModel: VaccinationsViewModel
    private lateinit var testsViewModel: TestsViewModel
    private lateinit var starterIntent: Intent

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        starterIntent = intent
        fBinding = ActivityLoggedIn2ndBinding.inflate(this.layoutInflater)
        loggedInActivityViewModel = ViewModelProvider(this).get(LoggedInActivityViewModel::class.java)
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        vaccinationsViewModel = ViewModelProvider(this).get(VaccinationsViewModel::class.java)
        testsViewModel = ViewModelProvider(this).get(TestsViewModel::class.java)
        GlobalScope.launch(Dispatchers.Default) {
            loggedInActivityViewModel.initFragments(
                    intent.extras,
                    profileViewModel,
                    vaccinationsViewModel,
                    testsViewModel,
            )
        }
        GlobalScope.launch (Dispatchers.Default) {
            val user = loggedInActivityViewModel.fetchPerson(intent.extras?.getInt("user"))
            fBinding.navView.getHeaderView(0).findViewById<TextView>(R.id.text_title).text =
                getString(R.string.nav_header_title, user?.firstName, user?.secondName)
            fBinding.navView.getHeaderView(0).findViewById<TextView>(R.id.text_subtitle).text =
                user?.iDNP
        }
        setSupportActionBar(binding.appBarLogin.toolbar)
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
        setContentView(fBinding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.logged_in_2nd, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_language){
            val popup = PopupMenu(this, findViewById(R.id.action_language))
            popup.setOnMenuItemClickListener (this)
            popup.inflate(R.menu.language_choose)
            popup.show()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        return (NavigationUI.navigateUp(navController, mAppBarConfiguration!!)
                || super.onSupportNavigateUp())
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when(item?.itemId){
            R.id.lang_en -> let {
                val pref = applicationContext.getSharedPreferences("MyPref", MODE_PRIVATE)
                val editor: SharedPreferences.Editor = pref.edit()
                editor.putString("lang_code", "en") // Saving string
                editor.apply()
                reload()
                true
            }
            R.id.lang_ru -> let{
                val pref = applicationContext.getSharedPreferences("MyPref", MODE_PRIVATE)
                val editor: SharedPreferences.Editor = pref.edit()
                editor.putString("lang_code", "ru") // Saving string
                editor.apply()
                reload()
                true
            }
            else -> false
        }
    }

    private fun reload(){
        finish()
        startActivity(starterIntent)
    }

}