package com.jruizb.toowine.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jruizb.toowine.databinding.FragmentHomeBinding
import com.jruizb.toowine.domain.WineItems
import com.jruizb.toowine.home.HomeRecyclerAdapter
import com.jruizb.toowine.util.Constants.FUN
import com.jruizb.toowine.util.Constants.NO_PRICE_WINE_RECYCLER
import com.jruizb.toowine.util.Constants.NO_TYPE_WINE_RECYCLER
import org.jetbrains.anko.support.v4.runOnUiThread
import org.jsoup.Jsoup
import java.security.KeyManagementException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import kotlin.concurrent.thread


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val wineologoList = ArrayList<WineItems>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
//        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        retrieveWebInfo()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun retrieveWebInfo() {
//        val recyclerV = findViewById<RecyclerView>(R.id.fgHomeDealsWineRecyclerView)

        var wineUrl: String
        var wineName: String
        var wineDenominacion: String
        var wineCurrentPrice: String
        var wineOriginalPrice: String

        thread {
            val doc = Jsoup.connect(
                "https://www.drinksco.es/productos:o:ofertas"
            ).sslSocketFactory(socketFactory()).get()
            val winitosGrid = doc.getElementsByClass("product-container")
//            val winitosItems = winitosGrid[0].getElementsByTag("a")
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
            this.runOnUiThread() {
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

    /**
     * To suppress certificate warnings for specific JSoup connection
     */
    private fun socketFactory(): SSLSocketFactory {
        Log.v(FUN, "Inside socketFactory")
        val trustAllCerts = arrayOf<TrustManager>(@SuppressLint("CustomX509TrustManager")
        object : X509TrustManager {
            @SuppressLint("TrustAllX509TrustManager")
            @Throws(CertificateException::class)
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            @SuppressLint("TrustAllX509TrustManager")
            @Throws(CertificateException::class)
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        })
        Log.v(FUN, "Before sslContext")
        try {
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, trustAllCerts, SecureRandom())
            return sslContext.socketFactory
        } catch (e: Exception) {
            when (e) {
                is RuntimeException, is KeyManagementException -> {
                    throw RuntimeException("Failed to create a SSL socket factory", e)
                }
                else -> throw e
            }
        }
    }
}

