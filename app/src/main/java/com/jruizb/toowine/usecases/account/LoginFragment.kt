package com.jruizb.toowine.usecases.account

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jruizb.toowine.R
import com.jruizb.toowine.databinding.FragmentLoginBinding
import com.jruizb.toowine.main.HomeActivity
import com.jruizb.toowine.preferences.PreferencesKey
import com.jruizb.toowine.preferences.PreferencesProvider


class LoginFragment : Fragment(){
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseAuth: FirebaseAuth
    private val firebaseFirestoreInstance = FirebaseFirestore.getInstance()
    private lateinit var progressDialog: ProgressDialog

    private var activityContext: Context? = null
    private var home: HomeActivity? = null



    private var emailLogin = ""
    private var passwordLogin = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Contexto de la actividad a la que está asociada este fragment
        activityContext = context

        //Inicializacion de firebase para obtener una instancia de ese objeto
        firebaseAuth = FirebaseAuth.getInstance()

        if (context!=null) {
            progressDialog = ProgressDialog(context)
            progressDialog.setTitle("Cargando...")
            progressDialog.setCanceledOnTouchOutside(false)
        }

    }

    override fun onStart() {
        super.onStart()

        binding.loginMaterialButton.setOnClickListener {
            validateData()
        }

        //Necesario para pasar al otro fragment a que se quiere navegar
        binding.signUpButtonLogin.setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }

        binding.forgotPasswordLogin.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotPassword)
        }
        //Si hay un usuario logueado, se carga el fragmento profile
        if(firebaseAuth.currentUser != null) {
            navToProfileFragment()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun validateData() {
        //input user data login
        emailLogin = binding.emailETLogin.text.toString().trim()
        passwordLogin = binding.passwordETLogin.text.toString().trim()

        //Validate data
        when {
            emailLogin.isEmpty() -> {
            Toast.makeText(context,requireActivity().getString(R.string.email_not_empty),Toast.LENGTH_SHORT).show()
            }
            //Comprobación del formato de email
            !Patterns.EMAIL_ADDRESS.matcher(emailLogin).matches() -> {
                Toast.makeText(context,requireActivity().getString(R.string.invalid_email),Toast.LENGTH_SHORT).show()
            }
            passwordLogin.isEmpty() -> {
                Toast.makeText(context,requireActivity().getString(R.string.pass_not_empty),Toast.LENGTH_SHORT).show()
            }
            passwordLogin.length < 6 -> {
                Toast.makeText(context,requireActivity().getString(R.string.pass_less_length),Toast.LENGTH_SHORT).show()
            }
            else -> {
                loginUser()

            }
        }
    }

    private fun loginUser() {
        progressDialog.setMessage("logueándose...")
        progressDialog.show()
        //Loguearse en Firebase Auth
        firebaseAuth.signInWithEmailAndPassword(emailLogin, passwordLogin)
            .addOnSuccessListener {
                progressDialog.dismiss()
                PreferencesProvider.set(requireActivity(),PreferencesKey.IS_LOGGED_IN,true)

                Toast.makeText(context,requireActivity().getString(R.string.login_welcome),Toast.LENGTH_LONG).show()

                //Navega mediante una acción definida en el navigation graph hacia profile fragment
                findNavController().navigate(R.id.action_loginFragment_to_profileFragment)
            }

            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(context,requireActivity().getString(R.string.fail_to_log_in)
                        +" "+e.localizedMessage,Toast.LENGTH_LONG).show()
            }
    }

    private fun navToProfileFragment() {
        findNavController().navigate(R.id.action_loginFragment_to_profileFragment)
    }

}