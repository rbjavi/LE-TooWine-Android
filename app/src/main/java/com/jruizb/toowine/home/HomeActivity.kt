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
//        retrieveWebInfo()
//        loadFragment(FavoritesFragment())
//        navigateToOtherViewBottomBarMenu()
        //Initialize the bottom navigation view
        //create bottom navigation view object
//        val bottomNavigationView = findViewById<BottomNavigationView
//                >(R.id.bottom_navigatin_view)
//        val navController = findNavController(R.id.nav_fragment)
//        bottomNavigationView.setupWithNavController(navController
//        )
//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
//        val navController = navHostFragment.navController
//        navController.navigate(R.id.homeFragment)

//        title=resources.getString(R.string.favorites)
//        loadFragment(HomeFragment())

//        bindingHome.bottomNavigatinView.setOnNavigationItemSelectedListener {
//            when(it.itemId){
//                R.id.-> {
//                    title=resources.getString(R.string.text_label_1)
//                    loadFragment(HomeFragment())
//                    return@setOnNavigationItemSelectedListener true
//                }
//
//                R.id.-> {
//                    title=resources.getString(R.string.fragment_wine_collections)
//                    loadFragment(WineCollectionFragment())
//                    return@setOnNavigationItemSelectedListener true
//                }
//
//                R.id.navigation_settings-> {
//                    title=resources.getString(R.string.settings)
//                    loadFragment(SettingsFragment())
//                    return@setOnNavigationItemSelectedListener true
//                }
//                R.id.navigation_settings-> {
//                    title=resources.getString(R.string.settings)
//                    loadFragment(SettingsFragment())
//                    return@setOnNavigationItemSelectedListener true
//                }
//
//            }
//            false
//
//        }
        /* Configuración del controlador de bottomNavigationBar y UI ActionBar label */
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigatin_view)
        //Retorna el FragmentManager para interactuar con los fragmentos asociodos a esta actividad
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        //El NavHostFragment provee un area dentro del layout para que ocurra la navegación entre fragments
        val navController = navHostFragment.navController

        //Configura los labels definidos en nav_graph.xml de los fragments para ser mostrados en el UI Action Bar
        //acorde con su fragment asociado entre navegaciones
  //Posible Eliminacion
//        val appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment,R.id.wineCollectionFragment,
//            R.id.favoritesFragment,R.id.newsFragment))
//        if (!appBarConfiguration.equals(actionBar) ) {
//            appBarConfiguration.fallbackOnNavigateUpListener
//        }else {
//            setupActionBarWithNavController(navController, appBarConfiguration)
//        }
    //
        //Configura la navegación entre fragments para concocer cuando un elemento del menú está seleccionado
        //El elemento seleccionado en el NavigationView se actualizará cuando el destino cambie
        bottomNavigationView.setupWithNavController(navController)
    }



//    private fun retrieveWebInfo() {
//        val wineologoList = ArrayList<WineItems>()
//        val recyclerV = findViewById<RecyclerView>(R.id.fgHomeDealsWineRecyclerView)
//
//        var wineUrl: String
//        var wineName: String
//        var wineDenominacion: String
//        var wineCurrentPrice: String
//        var wineOriginalPrice: String
//
//        thread {
//            val doc = Jsoup.connect(
//                "https://www.drinksco.es/productos:o:ofertas"
//            ).get()
//            val winitosGrid = doc.getElementsByClass("product-container")
////            val winitosItems = winitosGrid[0].getElementsByTag("a")
//            for (i in winitosGrid) {
//                /*https://www.drinksco.es/productos:o:ofertas" */
//                /* IMAGEN VINO */
//                wineUrl = i.getElementsByTag("img").attr("data-src")
//                wineUrl = wineUrl.replace("\\s".toRegex(), "")
//
//                if (wineUrl == "") {
//                    wineUrl = i.getElementsByTag("img").attr("src")
//                }
//
//                /* NOMBRE VINO */
//                wineName = i.getElementsByTag("h2").text()
//
//                /* DENOMINACION VINO */
//                wineDenominacion = i.getElementsByClass("region-name").text()
//                if (wineDenominacion == ""){
//                    wineDenominacion = noType
//                }
//
//                /* PRECIO */
//                wineCurrentPrice = i.getElementsByClass("current_price").text()
//                if (wineCurrentPrice == "") {
//                    wineCurrentPrice = noPrice
//                }
//
//                /* PRECIO ORIGINAL */
//                wineOriginalPrice = i.getElementsByClass("original_price").text()
//                if (wineOriginalPrice == ""){
//                    wineOriginalPrice = ""
//                }
//                wineologoList.add(WineItems(
//                    wineUrl, wineName, wineDenominacion, wineCurrentPrice, wineOriginalPrice
//                ))
//            }
//
//            //No puede acceder a los elementos UI desde el hilo background
//            this.runOnUiThread() {
//                //Le paso al adapter el contexto que es en este caso el de Main Activity y la lista
//                //con los argumentos que conforman un vino(Objeto)
//                val adapterRV = HomeRecyclerAdapter(this@HomeActivity, wineologoList)
//                recyclerV.layoutManager =
//                    LinearLayoutManager(this@HomeActivity, RecyclerView.HORIZONTAL, false)
//                recyclerV.adapter = adapterRV
//            }
//        }
//    }


    private fun loadFragment(fragment: Fragment) {
        // load fragment
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun setupHomewineRecycler() {
        val wineList: ArrayList<WineItems> = ArrayList()

//        val recyclerV = findViewById<RecyclerView>(R.id.homewineRecycler)
//        val adapterRV = HomeRecyclerAdapter(this,wineList)
//        binding.homewineRecycler.adapter = adapterRV
//        binding.homewineRecycler.layoutManager = LinearLayoutManager(this)
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