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
import com.jruizb.toowine.R
import com.jruizb.toowine.databinding.HomewineRecyclerItemsBinding
import com.jruizb.toowine.domain.WineItems
import java.util.*

import androidx.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.jruizb.toowine.domain.FavoriteItems
import com.jruizb.toowine.preferences.PreferencesKey
import com.jruizb.toowine.preferences.PreferencesProvider
import com.jruizb.toowine.provides.Firebase
import org.jetbrains.anko.sdk27.coroutines.onClick
import kotlin.collections.ArrayList


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
     * Recupera
     */
    override fun onBindViewHolder(holder: RecyclerHolder, position: Int) {
        holder.bind(wineItemsList[position])

        Log.i("ADAPTER POS", position.toString())
        val favoriteButton = holder.itemView.findViewById<ImageButton>(R.id.wineStarImageButton)
        val currentUser = firebaseAuth.currentUser

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
//                if (!wineItemsList[position].favStatus) {
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
                                "Vino Agregado: " + wineItemsList[position].name
                                        + "  ,Estado: " + wineItemsList[position].favStatus
                                        + "\nPosición: " + position,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        ?.addOnFailureListener { e ->
                            Toast.makeText(
                                contexto,
                                contexto.getString(R.string.failed_to_get_document_data) + " " + { e.message },
                                Toast.LENGTH_LONG
                            ).show()
//
                        }
                } else {
                //ELIMINA UN VINO AL CLICAR DESACTIVANDO LA STAR
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
                                    }
                                }
                            } else {
                                Log.d("ERROR DOC", "Error getting documents: ", it.exception)
                            }
                        }
//                   dbFavRef?.whereEqualTo("userID", userId)
//                       ?.get()?.addOnCompleteListener { task ->
//                        if (task.isSuccessful) {
//                            for (document in task.result!!) {
//                                if(wineItemsList[position].favStatus == document.getBoolean("state")) {
//                                    favoriteButton.setBackgroundResource(R.drawable.ic_fav_unselected_star_24)
//                                    wineItemsList[position].favStatus = false
//                                    dbFavRef?.document(document.get("name").toString())?.delete()
//                                }
//
//                                Log.d("DOC", document.id + " => " + document.data)
//                                dbFavRef?.whereEqualTo("Documet", document.id)
//                            }
//                        } else {
//                            Log.d("ERROR DOC", "Error getting documents: ", task.exception)
//                        }
//                    }
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
        //RECUPERACIÓN DE VINOS FAVORITOS DESDE FIRESTORE
        // Si hay usuario logueado, activa button star de los vinos que han sido seleccionados como favoritos
        // por ese usuario
        if (currentUser != null) {
            dbUsersRef?.document(currentUser.uid)?.collection("favoriteWines")
            ?.whereEqualTo("userID", currentUser.uid)
            ?.get()?.addOnCompleteListener {
                if (it.isSuccessful) {
                    for (docSnap: DocumentSnapshot in it.result!!) {
                        if (wineItemsList[position].name == docSnap.get("name")){
                            holder.itemView.setOnClickListener {
                                Toast.makeText(contexto,"pos$position"+" // "+wineItemsList[position],Toast.LENGTH_SHORT).show() }
                            favoriteButton?.setBackgroundResource(R.drawable.ic_fav_selected_star_24)
                        }

                        //Log.d("DOC", docSnap.id + " => " + docSnap.data)

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