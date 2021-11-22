package com.jruizb.toowine.favorites

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jruizb.toowine.databinding.FavoritewineRecyclerItemsBinding
import com.jruizb.toowine.domain.FavoriteItems


/**
 * TooWine
 *
 * Creado por Javier RB
 * A fecha 20/11/2021
 */
class FavoritesRecyclerAdapter (val contexto: Context, val favoriteItemsList: ArrayList<FavoriteItems>) :
RecyclerView.Adapter<FavoritesRecyclerAdapter.RecyclerHolder>() {

    /**
     * Crea un viewholder y su vista asociada, y los inicializa sin llegar a vincular aun el viewholder
     * con los datos en especifico
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerHolder {
        return RecyclerHolder(
            FavoritewineRecyclerItemsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    /**
     * Recupera
     */
    override fun onBindViewHolder(holder: RecyclerHolder, position: Int) {
        holder.bind(favoriteItemsList[position])
    }

    /**
     * Obtiene el tamaño del conjunto de datos
     */
    override fun getItemCount(): Int {
        return favoriteItemsList.size
    }

    //Clase interna con la inicializacion bindeada de los datos que se quieren obtener de un objeto Wine
    inner class RecyclerHolder(private val itemBinding: FavoritewineRecyclerItemsBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        /**
         * Recoge los datos inicializados del objeto en cuestion para pasarlos a los elementos correspondientes
         * de la vista
         */
        fun bind(favsItems: FavoriteItems) = with(itemBinding) {
//          itemBinding.apply {  //Se puede tambien llamar a la funcion apply() para aplicar solo una vez la variable que se esta bindeando
            favsName.text = favsItems.wineNameFavs

            Glide.with(contexto)
                .load(favoriteItemsList[adapterPosition].wineImageFavs)
                .override(200,200) //
                .into(itemBinding.favsImage)
//            Glide.with(contexto)
//                .load(wineItemsList[adapterPosition].imageUrl)
//                .transform(CircleCrop())
//                .into(itemBinding.wineImage)
//          }
        }
    }
}