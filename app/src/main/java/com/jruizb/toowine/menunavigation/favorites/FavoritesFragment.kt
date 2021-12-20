package com.jruizb.toowine.menunavigation.favorites

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.jruizb.toowine.R
import com.jruizb.toowine.databinding.FragmentFavoritesBinding
import com.jruizb.toowine.domain.FavoriteItems
import com.jruizb.toowine.provides.Firebase
import com.jruizb.toowine.util.Constants.FAVORITEWINES_SUBCOLLECTION
import com.google.firebase.firestore.QuerySnapshot

import androidx.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task


class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    // Esta propiedad es sólo válida entre onCreateView y
    // onDestroyView.
    private val binding get() = _binding!!

    private var activityContext: Context? = null

    private var firebaseFirestoreInstance: FirebaseFirestore? = null
    private var dbUsersRef: CollectionReference? = null
    private var dbFavRef: CollectionReference? = null

    private lateinit var firebaseAuth: FirebaseAuth

    private val wineFavsList = ArrayList<FavoriteItems>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Contexto de la actividad a la que está asociada este fragment
        activityContext = context

        //Inicializacion de firebase para obtener una instancia de ese objeto
        firebaseAuth = Firebase.provideFirebaseAuthentication()
        firebaseFirestoreInstance = Firebase.provideFirebaseFirestore()

        dbUsersRef =  Firebase.provideUsersRef(firebaseFirestoreInstance!!)
        dbFavRef = Firebase.provideFavWinesRef(firebaseFirestoreInstance!!)

        getFavoriteWineList()
    }

    override fun onStart() {
        super.onStart()
        setUpFavoriteView()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpFavoriteView() {
        val currentUser: FirebaseUser? = firebaseAuth.currentUser
        if (currentUser != null) {
            dbFavRef?.whereEqualTo("userID", currentUser.uid)
                ?.limit(1)?.get()
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                    val isEmpty: Boolean = task.result?.documents?.size!! < 0
                    Log.i("DATABASE//", isEmpty.toString())
                        if (isEmpty) {
                            binding.favSubTitleEmptyRecycler.visibility = View.VISIBLE
                        } else {
                            binding.recyclerVWineFavs.visibility = View.VISIBLE
                        }
                    }
                }

//            Log.d("ITM", binding.recyclerVWineFavs.adapter?.itemCount.toString())
//            if (binding.recyclerVWineFavs.adapter?.itemCount == null) {
//                binding.favSubTitleEmptyRecycler.visibility = View.VISIBLE
//            } else {
//
//            }
//            binding.recyclerVWineFavs.visibility = View.VISIBLE
        } else {
            binding.favSubTitleMustBeLoggedIn.visibility = View.VISIBLE
            Toast.makeText(context,requireContext().getString(R.string.must_be_logged_in_favs), Toast.LENGTH_SHORT).show()
        }
    }

    private fun getFavoriteWineList() {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            dbUsersRef?.document(currentUser.uid)?.collection(FAVORITEWINES_SUBCOLLECTION)
                ?.whereEqualTo("userID", currentUser.uid)
                ?.get()?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (docSnap: DocumentSnapshot in task.result!!) {
                                wineFavsList.add(
                                    FavoriteItems(
                                        docSnap.getString("image").toString(), docSnap.get("name").toString()
                                    )
                                )
                            Log.d("DOC", docSnap.id + " => " + docSnap.getString("image").toString()+" / "+ docSnap.get("name").toString())
                        }
                    }
                    context?.let {
                        //Le paso al adapter el contexto que es en este caso el de Main Activity y la lista
                        //con los argumentos que conforman un vino(Objeto)
                        binding.recyclerVWineFavs.layoutManager =
                            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                        binding.recyclerVWineFavs.adapter =
                            FavoritesRecyclerAdapter(requireContext(), wineFavsList)
                    }
                }
        }
    }
}