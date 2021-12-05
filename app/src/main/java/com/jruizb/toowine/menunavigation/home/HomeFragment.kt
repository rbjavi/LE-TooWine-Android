package com.jruizb.toowine.menunavigation.home

import android.app.ProgressDialog
import android.content.Context
import android.media.metrics.Event
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.jruizb.toowine.R
import com.jruizb.toowine.databinding.FragmentHomeBinding
import com.jruizb.toowine.databinding.HomewineRecyclerItemsBinding
import com.jruizb.toowine.domain.WineItems
import com.jruizb.toowine.util.Constants.NO_PRICE_WINE_RECYCLER
import com.jruizb.toowine.util.Constants.NO_TYPE_WINE_RECYCLER
import com.jruizb.toowine.util.CertificateJsoup
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.runOnUiThread
import org.jsoup.Jsoup


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var _recyclerBinding: HomewineRecyclerItemsBinding? = null
//    private val recyclerViewWines: RecyclerView? = null

    private var firebaseFirestoreInstance: FirebaseFirestore? = null
    private var dbReference: CollectionReference? = null
    private var dbFavReference: CollectionReference? = null

    private lateinit var firebaseAuth: FirebaseAuth

    private var activityContext: Context? = null


    private val wineologoList = ArrayList<WineItems>()
    private var favButton: ImageButton? = null;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        favButton = _recyclerBinding?.wineStarImageButton
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Contexto de la actividad a la que está asociada este fragment
        activityContext = context
        //Inicialización de firebase auth
        firebaseAuth = FirebaseAuth.getInstance()
        //Inicialización de firestore para poder realizar las opeciaones de lectura o escritura
        firebaseFirestoreInstance = FirebaseFirestore.getInstance()
        //Obtiene la referencia de un documento de la db
        dbReference = firebaseFirestoreInstance!!.collection("client")
        dbFavReference = firebaseFirestoreInstance!!.collection("favoriteWines")
        retrieveWineDealsInfoFromWeb()



//        favoriteSetup()
    }

    override fun onStart() {
        super.onStart()

      val checkUserLoggedIn:FirebaseUser? = firebaseAuth.currentUser
        if (checkUserLoggedIn != null) { //If any user is logged in
            favButton?.setOnClickListener {

            }
        } else {
            Toast.makeText(context,requireContext().getString(R.string.must_be_logged_in),Toast.LENGTH_SHORT).show()
        }
    }

    //    private fun favoriteSetup() {
//        TODO("Not yet implemented")
//    }



    private fun retrieveWineDealsInfoFromWeb() {
//        val recyclerV = findViewById<RecyclerView>(R.id.fgHomeDealsWineRecyclerView)
        var wineUrl: String
        var wineName: String
        var wineDenominacion: String
        var wineCurrentPrice: String
        var wineOriginalPrice: String

        doAsync {
            val doc = Jsoup.connect(
                "https://www.drinksco.es/productos:o:ofertas"
            ).sslSocketFactory(CertificateJsoup.socketFactory()).get()

            val winitosGrid = doc.getElementsByClass("product-container")
            for (i in winitosGrid) {
                /*https://www.drinksco.es/productos:o:ofertas" */
                /* IMAGEN VINO */
                wineUrl = i.getElementsByTag("img").attr("data-src")
                wineUrl = wineUrl.replace("\\s".toRegex(), "")

                if (wineUrl == "") {
                    wineUrl = i.getElementsByTag("img").attr("src")
                }

                /* NOMBRE VINO */
                wineName = i.getElementsByTag("h2").text()

                /* DENOMINACION VINO */
                wineDenominacion = i.getElementsByClass("region-name").text()
                if (wineDenominacion == "") {
                    wineDenominacion = NO_TYPE_WINE_RECYCLER
                }

                /* PRECIO */
                wineCurrentPrice = i.getElementsByClass("current_price").text()
                if (wineCurrentPrice == "") {
                    wineCurrentPrice = NO_PRICE_WINE_RECYCLER
                }

                /* PRECIO ORIGINAL */
                wineOriginalPrice = i.getElementsByClass("original_price").text()
                if (wineOriginalPrice == "") {
                    wineOriginalPrice = ""
                }
                wineologoList.add(
                    WineItems(
                        wineUrl, wineName, wineDenominacion, wineCurrentPrice, wineOriginalPrice
                    )
                )
            }
            runOnUiThread {
                //No puede acceder a los elementos UI desde el hilo background
                context?.let {
                    //Le paso al adapter el contexto que es en este caso el de Main Activity y la lista
                    //con los argumentos que conforman un vino(Objeto)
//                    val adapterRV = HomeRecyclerAdapter(context, wineologoList)
                    binding.fgHomeDealsWineRecyclerView.layoutManager =
                        LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                    binding.fgHomeDealsWineRecyclerView.adapter =
                        HomeRecyclerAdapter(requireContext(), wineologoList)
                }
            }
        }
    }

    //FIX THIS
    private fun updateEvent(event: Event) {
        _recyclerBinding?.wineStarImageButton?.setOnClickListener() {
            deleteEvent(event)
        }
    }
    //******
    private fun deleteEvent(event: Event) {

    }
}

