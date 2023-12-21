package com.cashcue.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.cashcue.R
import com.cashcue.data.local.pref.user.UserModel
import com.cashcue.databinding.ActivityMainBinding
import com.cashcue.ui.ViewModelFactory
import com.cashcue.ui.welcome.WelcomeActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        mainViewModel.getSession().observe(this) { user ->
            if (user.nama.isEmpty()) {
                startActivity(Intent(this@MainActivity, WelcomeActivity::class.java))
                finish()
            } else {
                mainViewModel.getAnggaranList().observe(this) {
                    var saldo = 0
                    it.forEach { anggaran ->
                        if (anggaran.jenis == 0) {
                            saldo += anggaran.saldo
                        } else {
                            saldo -= anggaran.saldo
                        }
                    }

                    mainViewModel.saveSession(
                        UserModel(
                            user.id,
                            user.email,
                            user.nama,
                            user.fotoUrl,
                            saldo
                        )
                    )
                }
            }
        }

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
//            )
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}