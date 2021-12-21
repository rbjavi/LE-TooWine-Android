package com.jruizb.toowine.menunavigation.newsfeeds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jruizb.toowine.R
import com.jruizb.toowine.databinding.FragmentNewsFeedsBinding
import com.jruizb.toowine.domain.NewsItems
import com.jruizb.toowine.util.CertificateJsoup
import com.jruizb.toowine.util.Constants.URL_NEWSDATASCRAPED
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.runOnUiThread
import org.jsoup.Jsoup


class NewsFeedsFragment : Fragment() {
    private var _binding: FragmentNewsFeedsBinding? = null
    // Esta propiedad es sólo válida entre onCreateView y
    // onDestroyView.
    private val binding get() = _binding!!

    private val newsFeedsList = ArrayList<NewsItems>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentNewsFeedsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        retrieveNewsInfoFromWeb()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun retrieveNewsInfoFromWeb() {
        var newsUrl: String
        var newsDescription: String

        try {
            doAsync {
                val doc = Jsoup.connect(
                    URL_NEWSDATASCRAPED
                ).sslSocketFactory(CertificateJsoup.socketFactory()).get()

                val newsScrapingGrid = doc.getElementsByTag("article")
                doc.getElementsByClass("abstract-title")
                for (i in newsScrapingGrid) {
                    /* IMAGEN NOTICIA */
                        newsUrl = i.getElementsByTag("img").first()?.attr("src").toString()
                        if (newsUrl == "" || newsUrl == "") {
                            newsUrl = "data:image/svg+xml;charset=utf8,%3Csvg%20xmlns='http://www.w3.org/2000/svg'%3E%3C/svg%3E"
                        }
                        newsUrl = newsUrl.replace("\\s".toRegex(), "")

                    /* TITULAR NOTICIA */
                        newsDescription = i.getElementsByTag("img").first()?.attr("alt").toString()

                    newsFeedsList.add(NewsItems(newsUrl, newsDescription))
                }
                runOnUiThread {
                    //No puede acceder a los elementos UI desde el hilo background
                    context?.let {
                        //Le paso al adapter el contexto que es en este caso el de Main Activity y la lista
                        //con los argumentos que conforman un vino(Objeto)
                        binding.fgNewsRecyclerView.layoutManager =
                            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                        binding.fgNewsRecyclerView.adapter =
                            NewsRecyclerAdapter(requireContext(), newsFeedsList)
                    }
                }
            }
        } catch (e: Exception) {
            Toast.makeText(
                context,
                requireContext().getString(R.string.invalid_webscraping),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}