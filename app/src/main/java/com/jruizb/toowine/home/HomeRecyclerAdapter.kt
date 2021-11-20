package com.jruizb.toowine.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jruizb.toowine.databinding.HomewineRecyclerItemsBinding
import com.jruizb.toowine.domain.WineItems


/**
 * TooWine
 *
 * Creado por Javier RB
 * A fecha 16/10/2021
 */
class HomeRecyclerAdapter(val contexto: Context, val wineItemsList: ArrayList<WineItems>) :
    RecyclerView.Adapter<HomeRecyclerAdapter.RecyclerHolder>() {
//    private lateinit var binding: HomewineRecyclerItemsBinding

    /**
     * Crea un viewholder y su vista asociada, y los inicializa sin llegar a vincular aun el viewholder
     * con los datos en especifico
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerHolder {
        return RecyclerHolder(
            HomewineRecyclerItemsBinding.inflate(
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
        holder.bind(wineItemsList[position])
    }

    /**
     * Obtiene el tamaño del conjunto de datos
     */
    override fun getItemCount(): Int {
        return wineItemsList.size
    }

    //Clase interna con la inicializacion bindeada de los datos que se quieren obtener de un objeto Wine
    inner class RecyclerHolder(private val itemBinding: HomewineRecyclerItemsBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        /**
         * Recoge los datos inicializados del objeto en cuestion para pasarlos a los elementos correspondientes
         * de la vista
         */
        fun bind(wineItems: WineItems) = with(itemBinding) {
//          itemBinding.apply {  //Se puede tambien llamar a la funcion apply() para aplicar solo una vez la variable que se esta bindeando
            wineName.text = wineItems.name
            wineDenominacion.text = wineItems.denominacion
            wineCurrentPrice.text = wineItems.currentPrice
            wineOriginalPrice.text = wineItems.originalPrice

            Glide.with(contexto)
                .load(wineItemsList[adapterPosition].imageViewUrl)
                .override(600,600) //
                .into(itemBinding.wineImageView)
//            Glide.with(contexto)
//                .load(wineItemsList[adapterPosition].imageUrl)
//                .transform(CircleCrop())
//                .into(itemBinding.wineImage)
//          }
        }
    }
}