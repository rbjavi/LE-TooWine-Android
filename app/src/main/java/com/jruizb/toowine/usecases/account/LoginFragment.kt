package com.jruizb.toowine.usecases.account

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jruizb.toowine.R
import com.jruizb.toowine.databinding.FragmentLoginBinding
import com.jruizb.toowine.domain.UserLogin
import com.jruizb.toowine.domain.UserProfile
import com.jruizb.toowine.home.HomeActivity


/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseAuth: FirebaseAuth
    private val firebaseFirestoreInstance = FirebaseFirestore.getInstance()
    private lateinit var progressDialog: ProgressDialog

    private val homeActivity:HomeActivity? = null
    private var activityContext: Context? = null

    private var email = ""
    private var password = ""



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
//        return inflater.inflate(R.layout.fragment_login, container, false)

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


        //obtiene la vista del botón para ir al fragment de registrarse y al clicar
        // se le pasa el contexto de este fragment
//        val action = view.findViewById<View>(R.id.signUpLoginTV)
//            action.setOnClickListener(this)
        binding.signUpLoginTV.setOnClickListener(this) //Necesario para pasar al otro fragment a que se quiere navegar

        binding.loginMaterialButton.setOnClickListener {

            validateData()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    companion object {
//        fun newInstance():LoginFragment {
//            return LoginFragment()
//        }
//    }

    /**
     * Función que al clicar sobre el evento del botón <si no tienes una cuenta, regístrate>
     * navega desde el fragment de Login hasta el fragment de Registrarse mediante una acción
     * establecida en en nav_graph hacia un destination
     */
    override fun onClick(view: View?) {
        if (view != null){
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_signupFragment)
        }
    }

    private fun validateData() {
        //input user data login
        email = binding.emailET.text.toString().trim()
        password = binding.passwordET.text.toString().trim()

        //Validate data
        when {
            //Comprobación del formato de email
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                Toast.makeText(context,requireActivity().getString(R.string.invalid_email),Toast.LENGTH_SHORT).show()
            }
            password.isEmpty() -> {
                Toast.makeText(context,requireActivity().getString(R.string.pass_not_empty),Toast.LENGTH_SHORT).show()
            }
            password.length < 6 -> {
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
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                progressDialog.dismiss()
//                replaceFromFragmentToFragment(ProfileFragment())
                //Navega mediante una acción definida en el navigation graph hacia otro fragment

                findNavController().navigate(R.id.action_loginFragment_to_profileFragment)
                Toast.makeText(context,requireActivity().getString(R.string.login_welcome),Toast.LENGTH_LONG).show()
                }

            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(context,requireActivity().getString(R.string.fail_to_log_in)
                        +" "+e.localizedMessage,Toast.LENGTH_LONG).show()
            }
    }





   private fun replaceFromFragmentToFragment(fragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }




}