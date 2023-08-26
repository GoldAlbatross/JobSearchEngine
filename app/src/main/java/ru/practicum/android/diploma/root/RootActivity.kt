package ru.practicum.android.diploma.root

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.LoggerImpl
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ActivityRootBinding
import ru.practicum.android.diploma.util.thisName

class RootActivity : AppCompatActivity() {
    
    private val binding by lazy { ActivityRootBinding.inflate(layoutInflater) }
    private val logger = LoggerImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        logger.log(thisName, "onCreate()")
    
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
    
        binding.bottomNavigationView.setupWithNavController(navController)
    
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
    
                R.id.filterBaseFragment -> hideBottomNav()
                R.id.detailsFragment -> hideBottomNav()
    
                else -> showBottomNav()
            }
        }
    
        // Пример использования access token для HeadHunter API
        networkRequestExample(accessToken = BuildConfig.HH_ACCESS_TOKEN)
    }
    
    private fun networkRequestExample(accessToken: String) {
        // ...
    }
    
    private fun hideBottomNav() {
        logger.log(thisName, "hideBottomNav()")
        binding.bottomNavigationView.visibility = View.GONE
    }
    
    private fun showBottomNav() {
        logger.log(thisName, "showBottomNav()")
        binding.bottomNavigationView.visibility = View.VISIBLE
    }
}