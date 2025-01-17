package com.example.android_application_course

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.android_application_course.data.repository.AuthListener
import com.example.android_application_course.data.repository.UserRepository

class MainActivity : AppCompatActivity() {

    var navController: NavController? = null
    var previousIsLogged: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val navHostFragment: NavHostFragment? =
            supportFragmentManager.findFragmentById(R.id.main_nav_host) as? NavHostFragment
        navController = navHostFragment?.navController
        navController?.let {
            NavigationUI.setupActionBarWithNavController(
                activity = this,
                navController = it
            )
        }

        UserRepository.getInstance().addAuthStateListener(object : AuthListener {
            override fun onAuthStateChanged() {
                if (previousIsLogged == UserRepository.getInstance().isLogged()) return

                previousIsLogged = UserRepository.getInstance().isLogged()
                navController?.let {
                    if (previousIsLogged == true) {
                        it.navigate(R.id.studentsListFragment)
                        it.graph.setStartDestination(R.id.studentsListFragment)
                    } else {
                        it.navigate(R.id.loginFragment)
                        it.graph.setStartDestination(R.id.loginFragment)
                    }
                }
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> navController?.popBackStack()
            R.id.edit_student_menu -> return false
            R.id.logout -> UserRepository.getInstance().logout()
            else -> navController?.let { NavigationUI.onNavDestinationSelected(item, it) }
        }
        return true
    }
}