package com.jruizb.toowine.menunavigation.winecollection

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jruizb.toowine.R
import com.jruizb.toowine.databinding.FragmentHomeBinding
import com.jruizb.toowine.databinding.FragmentLoginBinding
import com.jruizb.toowine.databinding.FragmentWineCollectionsBinding
import com.jruizb.toowine.domain.WineItems
import com.jruizb.toowine.menunavigation.home.HomeRecyclerAdapter
import com.jruizb.toowine.util.CertificateJsoup
import com.jruizb.toowine.util.Constants
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.runOnUiThread
import org.jsoup.Jsoup


class WineCollectionsFragment : Fragment() {
    private var _binding: FragmentWineCollectionsBinding? = null
    // Esta propiedad es sólo válida entre onCreateView y
    // onDestroyView.
    private val binding get() = _binding!!


    private var recyclerHome: FragmentHomeBinding? = null
    private val wineList = ArrayList<WineItems>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentWineCollectionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onStart() {
        super.onStart()

        checkVisibleSearchBar()
        clickOnWineTypesCard()
    }

    private fun checkVisibleSearchBar () {
        binding.searchImageButton.setOnClickListener {
        if (binding.collectionsSearchView.visibility == View.GONE) {

                binding.collectionsSearchView.visibility = View.VISIBLE

        } else if (binding.collectionsSearchView.visibility == View.VISIBLE){

                binding.collectionsSearchView.visibility = View.GONE
            }
        }
    }


    private fun clickOnWineTypesCard() {
        binding.redwineCard.setOnClickListener {
            searchWineTypes(Constants.URI_RED_WINE)
            binding.wineTypesGridLayoutWineCollection.visibility = View.GONE
        }

        binding.whitewineCard.setOnClickListener {
            searchWineTypes(Constants.URI_WHITE_WINE)
            binding.wineTypesGridLayoutWineCollection.visibility = View.GONE
        }

        binding.rosewineCard.setOnClickListener {
            searchWineTypes(Constants.URI_ROSE_WINE)
            binding.wineTypesGridLayoutWineCollection.visibility = View.GONE
        }

        binding.sparklingwineCard.setOnClickListener {
            searchWineTypes(Constants.URI_SPARKLING_WINE)
            binding.wineTypesGridLayoutWineCollection.visibility = View.GONE
        }


    }

    private fun searchWineTypes(url:String) {
        var wineUrl: String
        var wineName: String
        var wineDenominacion: String
        var wineCurrentPrice: String
        var wineOriginalPrice: String

        doAsync {
            val doc = Jsoup.connect(
                "https://www.drinksco.es/$url"
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
                    wineDenominacion = Constants.NO_TYPE_WINE_RECYCLER
                }

                /* PRECIO */
                wineCurrentPrice = i.getElementsByClass("current_price").text()
                if (wineCurrentPrice == "") {
                    wineCurrentPrice = Constants.NO_PRICE_WINE_RECYCLER
                }

                /* PRECIO ORIGINAL */
                wineOriginalPrice = i.getElementsByClass("original_price").text()
                if (wineOriginalPrice == "") {
                    wineOriginalPrice = ""
                }
                wineList.add(
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
                    binding.typesListWineCollection.layoutManager =
                        GridLayoutManager(context,2, RecyclerView.VERTICAL, false)
                    binding.typesListWineCollection.adapter =
                        HomeRecyclerAdapter(requireContext(), wineList)
                }
            }
        }
    }

}