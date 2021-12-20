package com.jruizb.toowine.menunavigation.favorites

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.jruizb.toowine.R
import com.jruizb.toowine.databinding.FavoritewineRecyclerItemsBinding
import com.jruizb.toowine.databinding.FragmentFavoritesBinding
import com.jruizb.toowine.domain.FavoriteItems
import com.jruizb.toowine.preferences.PreferencesKey
import com.jruizb.toowine.preferences.PreferencesProvider
import com.jruizb.toowine.provides.Firebase


/**
 * TooWine
 *
 * Creado por Javier RB
 * A fecha 20/11/2021
 */
class FavoritesRecyclerAdapter(
    private val contexto: Context,
    val favoriteItemsList: ArrayList<FavoriteItems>
) :
    RecyclerView.Adapter<FavoritesRecyclerAdapter.RecyclerHolder>() {

    private var firebaseAuth: FirebaseAuth = Firebase.provideFirebaseAuthentication()
    private var firebaseFirestoreInstance = Firebase.provideFirebaseFirestore()

    private var dbUsersRef: CollectionReference? =
        Firebase.provideUsersRef(firebaseFirestoreInstance)
    private var dbFavRef: CollectionReference? =
        Firebase.provideFavWinesRef(firebaseFirestoreInstance)

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
     * Actualiza y recupera los contenidos del RecyclerViewHolder con el item en la posición dada
     */
    override fun onBindViewHolder(holder: RecyclerHolder, position: Int) {
        holder.bind(favoriteItemsList[position])

        val currentUser = firebaseAuth.currentUser
        val deleteFavButton = holder.itemView.findViewById<ImageButton>(R.id.deleteFavsImageButton)
        // Botón eliminar un vino de las lista de favoritos
        deleteFavButton.setOnClickListener {
            if (currentUser != null) {
                val wineNameNotSpaces =
                    favoriteItemsList[position].wineNameFavs.filter { !it.isWhitespace() }
                dbUsersRef?.document(currentUser.uid)?.collection("favoriteWines")
                ?.get()?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        for (docSnap: DocumentSnapshot in it.result!!) {
                            if (wineNameNotSpaces == docSnap.id) {
//                                Toast.makeText(
//                                    contexto,
//                                    "Vino : " + favoriteItemsList[position].wineNameFavs
//                                            + "\nPosición: " + position,
//                                    Toast.LENGTH_SHORT
//                                ).show()
                                dbUsersRef?.document(currentUser.uid)
                                    ?.collection("favoriteWines")!!
                                    .document(wineNameNotSpaces)?.delete()
                                favoriteItemsList.removeAt(position)
                                notifyItemRemoved(position)
                            }

                        }
                    } else {
                        Log.d("ERROR DOC", "Error getting documents: ", it.exception)
                    }
                }
            }
        }
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
         * Recoge los datos inicializados del objeto en cuestión para pasarlos a los elementos correspondientes
         * de la vista
         */
        fun bind(favsItems: FavoriteItems) = with(itemBinding) {
            favsName.text = favsItems.wineNameFavs
            Glide.with(contexto)
                .load(favoriteItemsList[adapterPosition].wineImageFavs)
                .override(200, 200) //
                .into(itemBinding.favsImage)
        }
    }
}