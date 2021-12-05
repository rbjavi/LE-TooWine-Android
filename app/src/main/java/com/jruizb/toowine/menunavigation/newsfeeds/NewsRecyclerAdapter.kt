package com.jruizb.toowine.menunavigation.newsfeeds

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.createBitmap
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.jruizb.toowine.R
import com.jruizb.toowine.databinding.NewsfeedsRecyclerItemsBinding
import com.jruizb.toowine.domain.NewsItems


/**
 * TooWine
 *
 * Creado por Javier RB
 * A fecha 27/11/2021
 */
class NewsRecyclerAdapter (val contexto: Context, val newsFeedsList: ArrayList<NewsItems>) :
    RecyclerView.Adapter<NewsRecyclerAdapter.RecyclerHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerHolder {
        return RecyclerHolder(
            NewsfeedsRecyclerItemsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerHolder, position: Int) {
        holder.bind(newsFeedsList[position])
    }

    override fun getItemCount(): Int {
        return newsFeedsList.size
    }

    //Clase interna con la inicializacion bindeada de los datos que se quieren obtener de un objeto Wine
    inner class RecyclerHolder(private val itemNewsBinding: NewsfeedsRecyclerItemsBinding) :
        RecyclerView.ViewHolder(itemNewsBinding.root) {
        /**
         * Recoge los datos inicializados del objeto en cuestion para pasarlos a los elementos correspondientes
         * de la vista
         */
        fun bind(newsItems: NewsItems) = with(itemNewsBinding) {
//            newsTitleItem.text = newsItems.titleNews
            newsDescriptionItem.text = newsItems.descriptionNews

            Glide.with(contexto)
                .load(newsFeedsList[adapterPosition].imageURLNews)
                .fitCenter()
                .into(itemNewsBinding.newsImageVItem)
        }
    }
}