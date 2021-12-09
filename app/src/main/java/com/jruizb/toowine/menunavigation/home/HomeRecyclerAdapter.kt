package com.jruizb.toowine.menunavigation.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.jruizb.toowine.R
import com.jruizb.toowine.databinding.HomewineRecyclerItemsBinding
import com.jruizb.toowine.domain.WineItems
import com.jruizb.toowine.preferences.PreferencesKey
import com.jruizb.toowine.preferences.PreferencesProvider
import org.jetbrains.anko.backgroundResource


/**
 * TooWine
 *
 * Creado por Javier RB
 * A fecha 16/10/2021
 */
class HomeRecyclerAdapter(private val contexto: Context,private val wineItemsList: ArrayList<WineItems>) :
    RecyclerView.Adapter<HomeRecyclerAdapter.RecyclerHolder>() {
    private var binding: HomewineRecyclerItemsBinding? = null
    private var isFavSelected: Boolean = false

    private var firebaseFirestoreInstance: FirebaseFirestore? = null
    private var dbReference: CollectionReference? = null
    private var dbFavReference: CollectionReference? = null

    private lateinit var firebaseAuth: FirebaseAuth
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
        holder.itemView.findViewById<ImageButton>(R.id.wineStarImageButton).setOnClickListener {
            if (!holder.itemView.findViewById<ImageButton>(R.id.wineStarImageButton).isSelected) {
                holder.itemView.findViewById<ImageButton>(R.id.wineStarImageButton)
                Toast.makeText(
                    contexto, "Pulsado elemento: "
                            + wineItemsList[position].name + "\nPosición: "
                            + position, Toast.LENGTH_SHORT
                ).show()
            } else {
                holder.itemView.findViewById<ImageButton>(R.id.wineStarImageButton)
                    .setImageResource(R.drawable.ic_fav_unselected_star_24)
            }
        }

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
            wineDenominacion.text = wineItems.originCertificate
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


//
//            itemBinding.wineStarImageButton.setOnClickListener {
//                val position = adapterPosition
//                    val wineItem = wineItemsList[position]
//                if (!wineItem.favStatus) {
//                    wineItem.favStatus
//                    itemBinding.wineStarImageButton.setBackgroundResource(R.drawable.ic_fav_selected_star_24)
//                } else {
//                    wineItem.favStatus = false
//                    itemBinding.wineStarImageButton.setBackgroundResource(R.drawable.ic_fav_unselected_star_24)
//                }
//            }
        }
    }
}