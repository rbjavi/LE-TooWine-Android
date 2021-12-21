package com.jruizb.toowine.usecases

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.jruizb.toowine.databinding.FragmentProfileBinding
import com.jruizb.toowine.main.HomeActivity
import com.jruizb.toowine.preferences.PreferencesKey
import com.jruizb.toowine.preferences.PreferencesProvider
import com.jruizb.toowine.provides.Firebase.provideFirebaseAuthentication
import com.jruizb.toowine.provides.Firebase.provideFirebaseFirestore
import com.jruizb.toowine.provides.Firebase.provideUsersRef
import kotlin.concurrent.timerTask


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    // Esta propiedad es sólo válida entre onCreateView y
    // onDestroyView.
    private val binding get() = _binding!!

    private var firebaseFirestoreInstance: FirebaseFirestore? = null
    private var dbUsersRef: CollectionReference? = null

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

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
        //Botón logout invisible inicialmente mientras no haya un usuario logueado
        binding.logoutProfileButton.visibility = View.INVISIBLE

        //Contexto de la actividad a la que está asociada este fragment
        activityContext = context
        //Inicialización de firebase auth
        firebaseAuth = provideFirebaseAuthentication()
        //Inicialización de firestore para poder realizar las operaciones de lectura o escritura
        firebaseFirestoreInstance = provideFirebaseFirestore()
        //Obtiene la referencia de un documento de la db
        dbUsersRef = provideUsersRef(firebaseFirestoreInstance!!)

        //Inicialización de progressDialog
        if (context != null) {
            progressDialog = ProgressDialog(context)
            progressDialog.setTitle("Cargando...")
            progressDialog.setCanceledOnTouchOutside(false)
        }
        isUserLoggedIn(requireContext())

    }

    override fun onStart() {
        super.onStart()


        getInfoFromCurrentProfile()
        profileLogout()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

    private fun isUserLoggedIn(context: Context): Boolean {
        return !(PreferencesProvider.getBool(context, PreferencesKey.IS_LOGGED_IN) ?: false)
    }

    /**
     * Función que obtiene y muestra los datos del actual usuario que haya iniciado sessión
     */
    private fun getInfoFromCurrentProfile() {
        val userLoggedIn = firebaseAuth.currentUser
        var name: String

        userLoggedIn?.let {
            with(binding) {
                dbUsersRef?.document(userLoggedIn.uid)?.collection("user")
                    ?.whereEqualTo("uid", userLoggedIn.uid)
                    ?.get()?.addOnCompleteListener {
                        if (it.isSuccessful) {
                            for (docSnap: DocumentSnapshot in it.result!!) {
                                name = docSnap.get("name").toString()
                                nameProfile.text = name
                                emailProfile.text = userLoggedIn.email
                            }
                        }
                    }
                //Si hay usuario logueado
                binding.logoutProfileButton.visibility = View.VISIBLE
            }
        }
    }

    private fun profileLogout() {
        binding.logoutProfileButton.setOnClickListener {
            progressDialog.setMessage("deslogueándose...")
            progressDialog.show()
            firebaseAuth.signOut()
            PreferencesProvider.set(requireActivity(),PreferencesKey.IS_LOGGED_IN,false)
            startActivity(Intent(requireActivity(), HomeActivity::class.java))
        }
        progressDialog.dismiss()
    }
}