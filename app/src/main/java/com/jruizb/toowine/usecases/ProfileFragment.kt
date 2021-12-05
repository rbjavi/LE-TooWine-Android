package com.jruizb.toowine.usecases

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.jruizb.toowine.databinding.FragmentProfileBinding
import com.jruizb.toowine.main.HomeActivity
import com.jruizb.toowine.preferences.PreferencesKey
import com.jruizb.toowine.preferences.PreferencesProvider


/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private var firebaseFirestoreInstance: FirebaseFirestore? = null
    private var dbUserReference: CollectionReference? = null

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    private val homeActivity: HomeActivity? = null
    private var activityContext: Context? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Contexto de la actividad a la que está asociada este fragment
        activityContext = context
        //Inicialización de firebase auth
        firebaseAuth = FirebaseAuth.getInstance()
        //Inicialización de firestore para poder realizar las opeciaones de lectura o escritura
        firebaseFirestoreInstance = FirebaseFirestore.getInstance()
        //Obtiene la referencia de un documento de la db
        dbUserReference = firebaseFirestoreInstance!!.collection("client")

        //Inicialización de progressDialog
        if (context!=null) {
            progressDialog = ProgressDialog(context)
            progressDialog.setTitle("Cargando...")
            progressDialog.setCanceledOnTouchOutside(false)
        }

        getInfoFromCurrentProfile()
        profileLogout()
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        if (isUserLoggedIn(requireContext())) {
            _binding = null
//        }
    }

    private fun isUserLoggedIn(context: Context): Boolean {
        return !(PreferencesProvider.getBool(context, PreferencesKey.IS_LOGGED_IN) ?: false)
    }

    private fun getInfoFromCurrentProfile() {
        val user = firebaseAuth.currentUser
        if (user != null) {
            binding.logoutProfileButton.visibility = View.VISIBLE
            user?.let {
                with(binding) {
                    nameProfile.text = user.displayName
                    emailProfile.text = user.email
//                val photoUrl = user.photoUrl
                }
            }
        }
    }

    private fun profileLogout() {
        binding.logoutProfileButton.setOnClickListener {
            progressDialog.setMessage("deslogueándose...")
            progressDialog.show()
            firebaseAuth.signOut()
            startActivity(Intent(requireActivity(), HomeActivity::class.java))
        }
        progressDialog.dismiss()
    }
}