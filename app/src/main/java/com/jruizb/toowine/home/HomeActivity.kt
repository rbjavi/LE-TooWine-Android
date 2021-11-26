package com.jruizb.toowine.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jruizb.toowine.R
import com.jruizb.toowine.databinding.ActivityHomeBinding
import com.jruizb.toowine.databinding.ActivityHomeBinding.inflate
import com.jruizb.toowine.databinding.FragmentLoginBinding
import com.jruizb.toowine.domain.WineItems
import com.jruizb.toowine.onboarding.Onboarding
import com.jruizb.toowine.preferences.PreferencesKey
import com.jruizb.toowine.preferences.PreferencesProvider
import org.jetbrains.anko.activityManager
import org.jsoup.Jsoup
import kotlin.concurrent.thread




class HomeActivity : AppCompatActivity() {

    private lateinit var  bindingHome: ActivityHomeBinding

    private lateinit var bindingLoginFragment: FragmentLoginBinding

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
        //Retorna el FragmentManager para interactuar con los fragmentos asociados a esta actividad
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        //El NavHostFragment provee un area dentro del layout para que ocurra la navegación entre fragments
        val navController = navHostFragment.navController

        //Configura la navegación entre fragments para concocer cuando un elemento del menú está seleccionado
        //El elemento seleccionado en el NavigationView se actualizará cuando el destino cambie
        bottomNavigationView.setupWithNavController(navController)
    }



    /**
     *  Función que valida si onboarding ya ha sido inicializada por primera vez, si obtiene true
     *  navega directamente a la actividad principal Home
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
        return !(PreferencesProvider.bool(context, PreferencesKey.ONBOARDING) ?: false)
    }

    fun replaceFragment(fragment: Fragment) {
        val transaction = this.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.commit()
    }
//    private fun navigateToSignup() {
//        bindingLoginFragment.signUpLoginTV.setOnClickListener {
//            val signupF =
//                val fragment =
//                    supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
//
//        }
//    }

}