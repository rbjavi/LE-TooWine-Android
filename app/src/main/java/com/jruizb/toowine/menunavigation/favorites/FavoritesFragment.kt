package com.jruizb.toowine.menunavigation.favorites

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.jruizb.toowine.R
import com.jruizb.toowine.databinding.FragmentFavoritesBinding
import com.jruizb.toowine.databinding.FragmentWineCollectionsBinding


class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private var activityContext: Context? = null

    private var firebaseFirestoreInstance: FirebaseFirestore? = null
    private var dbReference: CollectionReference? = null
    private var dbFavReference: CollectionReference? = null

    private lateinit var firebaseAuth: FirebaseAuth

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
        firebaseAuth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()

        val checkUserLoggedIn: FirebaseUser? = firebaseAuth.currentUser
        if (checkUserLoggedIn != null) { //If any user is logged in
            binding.favTitleMustBeLoggedIn.visibility = View.GONE
            binding.favWineWhishList.visibility = View.VISIBLE

        } else {
            Toast.makeText(context,requireContext().getString(R.string.must_be_logged_in), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}