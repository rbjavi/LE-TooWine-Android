package com.jruizb.toowine.menunavigation.winecollection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jruizb.toowine.databinding.FragmentWineTypeListBinding
import com.jruizb.toowine.domain.WineItems
import com.jruizb.toowine.menunavigation.home.HomeRecyclerAdapter
import com.jruizb.toowine.util.CertificateJsoup
import com.jruizb.toowine.util.Constants
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.runOnUiThread
import org.jsoup.Jsoup


class WineTypeList : Fragment() {
    private var _binding: FragmentWineTypeListBinding? = null
    // Esta propiedad es sólo válida entre onCreateView y
    // onDestroyView.
    private val binding get() = _binding!!
    private val wineList = ArrayList<WineItems>()

    private val args: WineTypeListArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentWineTypeListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        searchWineTypes(args.myUri, args.myTextF)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun searchWineTypes(url:String, text:String) {
        var wineUrl: String
        var wineName: String
        var wineDenominacion: String
        var wineCurrentPrice: String
        var wineOriginalPrice: String

        //Cambiamos el texto del TextField del título con el texto del producto correspondiente
        binding.titleLabelWineTypeList.text = text

        doAsync {
            val doc = Jsoup.connect(
                Constants.URL_WEBSCRAPING+url
            ).sslSocketFactory(CertificateJsoup.socketFactory()).get()

            val winitosGrid = doc.getElementsByClass("product-container")
            for (i in winitosGrid) {

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
                    binding.recyclerVWineTypeList.layoutManager =
                        GridLayoutManager(context,2, RecyclerView.VERTICAL, false)
                    binding.recyclerVWineTypeList.adapter =
                        HomeRecyclerAdapter(requireContext(), wineList)
                }
            }
        }
    }

}