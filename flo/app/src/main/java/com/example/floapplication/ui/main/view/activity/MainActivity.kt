package com.example.floapplication.ui.main.view.activity

import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.floapplication.R
import com.example.floapplication.databinding.ActivityMainBinding
import com.example.floapplication.ui.base.BaseActivity

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    // Fragment Attach
    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
    }

    // Fragment appBar연결
    private val appBarConfiguration by lazy {
        AppBarConfiguration(
            setOf(R.id.mainFragment)
        )
    }

    override fun init() {
        super.init()
        initToolbar()
    }

    // Toolbar Set
    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    // 뒤로가기 이벤트
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}