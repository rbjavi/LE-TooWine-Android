package com.jruizb.toowine.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jruizb.toowine.R
import com.jruizb.toowine.databinding.ActivityHomeBinding
import com.jruizb.toowine.databinding.ActivityHomeBinding.inflate
import com.jruizb.toowine.domain.WineItems
import com.jruizb.toowine.onboarding.Onboarding
import com.jruizb.toowine.preferences.PreferencesKey
import com.jruizb.toowine.preferences.PreferencesProvider
import org.jsoup.Jsoup
import kotlin.concurrent.thread




class HomeActivity : AppCompatActivity() {

    private lateinit var  bindingHome: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingHome = inflate(layoutInflater)
        setContentView(bindingHome.root)
//        setTheme(R.style.Theme_TooWine)
//        setContentView(R.layout.activity_home)
//        setTheme(R.style.SplashTheme)
        setupOnboarding()

        /* Configuración del controlador de bottomNavigationBar y UI ActionBar label */
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigatin_view)
        //Retorna el FragmentManager para interactuar con los fragmentos asociodos a esta actividad
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        //El NavHostFragment provee un area dentro del layout para que ocurra la navegación entre fragments
        val navController = navHostFragment.navController

        //Configura la navegación entre fragments para concocer cuando un elemento del menú está seleccionado
        //El elemento seleccionado en el NavigationView se actualizará cuando el destino cambie
        bottomNavigationView.setupWithNavController(navController)
    }



    /**
     *  Función para comprobar si es la primera vez usada en ese dispositivo, se activa la vista inicial onboarding
     */
    private fun setupOnboarding() {
        if (onboarding(this)) {
            startActivity(Intent(this@HomeActivity, Onboarding::class.java))
        }
    }

    /**
     * Función que comprueba si esta guardado en ese dispositivo el par clave-valor para saber si es
     * el primer uso de la app y, por lo tanto, mostrar o no la vista onboarding inicial
     */
    private fun onboarding(context: Context): Boolean {
        return !(PreferencesProvider.bool(context, PreferencesKey.ONBOARDING) ?: false)
    }

}