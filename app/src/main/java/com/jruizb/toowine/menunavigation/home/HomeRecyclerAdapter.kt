package com.jruizb.toowine.menunavigation.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.jruizb.toowine.R
import com.jruizb.toowine.databinding.HomewineRecyclerItemsBinding
import com.jruizb.toowine.domain.WineItems
import com.jruizb.toowine.provides.Firebase
import java.util.*


/**
 * TooWine
 *
 * Creado por Javier RB
 * A fecha 16/10/2021
 */
class HomeRecyclerAdapter(
    private val contexto: Context,
    private val wineItemsList: ArrayList<WineItems>
) :
    RecyclerView.Adapter<HomeRecyclerAdapter.RecyclerHolder>() {

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
            HomewineRecyclerItemsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    /**
     * Enlaza nuevos datos al reutilizar las mismas filas que antes tenían otra información y no están
     * visibles en ese momento en la pantalla.
     *
     * Modificada para que al clicar en el botón de favoritos inserte los datos de ese vino en la base de datos
     * y active el botón de estado
     */
    override fun onBindViewHolder(holder: RecyclerHolder, position: Int) {
        holder.bind(wineItemsList[position])

        val favoriteButton = holder.itemView.findViewById<ImageButton>(R.id.wineStarImageButton)
        val currentUser = firebaseAuth.currentUser

        // Datos de una botella de vino que serán agregados a una collección para ser insertados
        // en la base de datos
        val userId = firebaseAuth.currentUser?.uid
        val imageW = wineItemsList[position].imageViewUrl
        val nameW = wineItemsList[position].name
        val timestamp = System.currentTimeMillis()
        val date = Date(timestamp)

        val favWineHashMap: HashMap<String, Any?> = hashMapOf(
            "userID" to userId,
            "name" to nameW,
            "image" to imageW,
            "state" to true,
            "timestamp" to date
        )
    //Al clicar en el botón de favoritos
        favoriteButton.setOnClickListener {
            if (currentUser != null) {
                // Elimina los espacios del nombre de vino para usarlo como id de documento
                val wineNameNotSpaces = nameW.filter { !it.isWhitespace() }
                if (!favoriteButton.isSelected) {
                    // Inserta un vino a favoritos del usuario logueado
                    dbUsersRef?.document(currentUser.uid)
                        ?.collection("favoriteWines")?.document(wineNameNotSpaces)
                        ?.set(favWineHashMap)
                        ?.addOnSuccessListener {
                            favoriteButton.setBackgroundResource(R.drawable.ic_fav_selected_star_24)
                            wineItemsList[position].favStatus = true
                            Toast.makeText(
                                contexto,
                                contexto.getString(R.string.data_inserted_successfully),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        ?.addOnFailureListener { e ->
                            Toast.makeText(
                                contexto,
                                contexto.getString(R.string.failed_to_get_document_data) + " " + { e.message },
                                Toast.LENGTH_LONG
                            ).show()
                        }
                } else {
                //ELIMINA UN VINO AL CLICAR DESACTIVANDO LA STAR
                dbUsersRef?.document(currentUser.uid)?.collection("favoriteWines")
                    ?.get()?.addOnCompleteListener {
                        if (it.isSuccessful) {
                            for (docSnap: DocumentSnapshot in it.result!!) {
                                if (wineNameNotSpaces == docSnap.id) {
                                    dbUsersRef?.document(currentUser.uid)
                                        ?.collection("favoriteWines")!!
                                        .document(wineNameNotSpaces)?.delete()
                                }
                            }
                        } else {
                            Log.d("ERROR DOC", "Error getting documents: ", it.exception)
                        }
                    }
                }
            // AL CLICAR EL BOTÓN DE FAVORITOS SI EL USUARIO NO ESTÁ LOGUEADO
            } else {
                Toast.makeText(
                    contexto,
                    contexto.getString(R.string.must_be_logged_in_favs),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        //RECUPERACIÓN DE VINOS FAVORITOS DESDE FIRESTORE DB
        // Si hay usuario logueado, activa button star de los vinos que han sido seleccionados como favoritos
        // por ese usuario
        if (currentUser != null) {
            dbUsersRef?.document(currentUser.uid)?.collection("favoriteWines")
            ?.whereEqualTo("userID", currentUser.uid)
            ?.get()?.addOnCompleteListener {
                if (it.isSuccessful) {
                    for (docSnap: DocumentSnapshot in it.result!!) {
                        if (wineItemsList[position].name == docSnap.get("name")) {
                            favoriteButton?.setBackgroundResource(R.drawable.ic_fav_selected_star_24)
                        }
                    }
                }
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
                wineName.text = wineItems.name
                wineDenominacion.text = wineItems.originCertificate
                wineCurrentPrice.text = wineItems.currentPrice
                wineOriginalPrice.text = wineItems.originalPrice

                Glide.with(contexto)
                    .load(wineItemsList[adapterPosition].imageViewUrl)
                    .override(600, 600) //
                    .into(itemBinding.wineImageView)

            }
        }
    }