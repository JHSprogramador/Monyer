package com.example.aprendiendo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import com.example.aprendiendo.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        val navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        // FAB contextual - cambia según el fragmento actual
        binding.fab.setOnClickListener {
            val currentDestination = navController.currentDestination?.id

            when (currentDestination) {
                R.id.ExpenseListFragment -> {
                    navController.navigate(R.id.action_ExpenseListFragment_to_AddExpenseFragment)
                }
                R.id.SavingGoalsFragment -> {
                    navController.navigate(R.id.action_SavingGoalsFragment_to_AddSavingGoalFragment)
                }
                R.id.DashboardFragment -> {
                    val options = arrayOf("➕ Agregar Gasto", "🎯 Agregar Objetivo")
                    MaterialAlertDialogBuilder(this)
                        .setTitle("¿Qué deseas agregar?")
                        .setItems(options) { _, which ->
                            when (which) {
                                0 -> navController.navigate(R.id.action_DashboardFragment_to_ExpenseListFragment)
                                1 -> navController.navigate(R.id.action_DashboardFragment_to_SavingGoalsFragment)
                            }
                        }
                        .show()
                }
            }
        }

        // Ocultar FAB en pantallas de formulario y Asistente de IA
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.AddExpenseFragment,
                R.id.AddSavingGoalFragment,
                R.id.AIAssistantFragment -> {
                    binding.fab.hide()
                }
                else -> {
                    binding.fab.show()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        val navController = navHostFragment.navController

        val currentDestination = navController.currentDestination?.id

        return when (item.itemId) {
            R.id.action_dashboard -> {
                if (currentDestination != R.id.DashboardFragment) {
                    navController.navigate(R.id.DashboardFragment)
                }
                true
            }
            R.id.action_expenses -> {
                if (currentDestination != R.id.ExpenseListFragment) {
                    navController.navigate(R.id.ExpenseListFragment)
                }
                true
            }
            R.id.action_goals -> {
                if (currentDestination != R.id.SavingGoalsFragment) {
                    navController.navigate(R.id.SavingGoalsFragment)
                }
                true
            }
            R.id.action_ai_assistant -> {
                if (currentDestination != R.id.AIAssistantFragment) {
                    navController.navigate(R.id.AIAssistantFragment)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        val navController = navHostFragment.navController

        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}

