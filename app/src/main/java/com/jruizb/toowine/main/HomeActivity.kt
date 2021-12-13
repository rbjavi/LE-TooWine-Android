package com.jruizb.toowine.main

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jruizb.toowine.R
import com.jruizb.toowine.databinding.ActivityHomeBinding
import com.jruizb.toowine.databinding.ActivityHomeBinding.inflate
import com.jruizb.toowine.onboarding.Onboarding
import com.jruizb.toowine.preferences.PreferencesKey
import com.jruizb.toowine.preferences.PreferencesProvider


class HomeActivity : AppCompatActivity() {

    private lateinit var  bindingHome: ActivityHomeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()
        setupOnboarding()
    }

    override fun onStart() {
        super.onStart()
        loadAppView()
    }

    private fun loadAppView () {
        bindingHome = inflate(layoutInflater)
        setContentView(bindingHome.root)

        /* Configuración del controlador de bottomNavigationBar y UI ActionBar label */
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigatin_view)
        //Retorna el FragmentManager para interactuar con los fragmentos asociados a esta actividad
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        //El NavHostFragment provee un area dentro del layout para que ocurra la navegación entre fragments
        val navController = navHostFragment.navController

        //Configura la navegación entre fragments para concocer cuando un elemento del menú está seleccionado
        //El elemento seleccionado en el NavigationView se actualizará cuando el destino cambie
        bottomNavigationView.setupWithNavController(navController)
    }

    /**
     *  Función que valida si onboarding ya ha sido inicializada por primera vez, si obtiene false
     *  se inicia la actividad onboarding; en cambio, si es true, se inicia directamente
     *  la actividad principal Home que está definida en el manifest por defecto
     */
    private fun setupOnboarding() {
        if (isOnboardingOnSharedPreferences(this)) {
            startActivity(Intent(this@HomeActivity, Onboarding::class.java))
        }
    }

    /**
     * Función que comprueba y devuelve true, si ya está guardado en ese dispositivo el par clave-valor
     * o, false, si no y es la primera vez que se inicializa en este dispositivo
     */
    private fun isOnboardingOnSharedPreferences(context: Context): Boolean {
        return !(PreferencesProvider.getBool(context, PreferencesKey.ONBOARDING) ?: false)
    }

}