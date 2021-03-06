package com.jruizb.toowine.usecases.account

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jruizb.toowine.R
import com.jruizb.toowine.databinding.FragmentSignupBinding
import com.jruizb.toowine.provides.Firebase
import java.util.*


class SignupFragment : Fragment(){
    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    private var activityContext: Context? = null

    private var name = ""
    private var email = ""
    private var pass = ""
    private var confirmPass = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Contexto de la actividad a la que está asociada este fragment
        activityContext = context

        //Inicializacion de firebase para obtener una instancia de ese objeto
        firebaseAuth = Firebase.provideFirebaseAuthentication()

        //Inicialización de progressDialog
        if (context!=null) {
            progressDialog = ProgressDialog(context)
            progressDialog.setTitle("Cargando...")
            progressDialog.setCanceledOnTouchOutside(false)
        }
    }

    override fun onStart() {
        super.onStart()

        binding.registerMaterialButton.setOnClickListener {
            validateData()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Función para validar los datos del registro de usuario
     */
    private fun validateData() {

        name = binding.nameET.text.toString().trim()
        email = binding.emailET.text.toString().trim()
        pass = binding.passwordET.text.toString().trim()
        confirmPass = binding.confirmPasswordET.text.toString().trim()

        when {
            name.isEmpty() -> {
                Toast.makeText(context,requireActivity().getString(R.string.name_not_empty),Toast.LENGTH_SHORT).show()
            }
            email.isEmpty() -> {
                Toast.makeText(context,requireActivity().getString(R.string.email_not_empty),Toast.LENGTH_SHORT).show()
            }
            pass.isEmpty()  -> {
                Toast.makeText(context,requireActivity().getString(R.string.pass_not_empty),Toast.LENGTH_SHORT).show()
            }
            pass.length < 6 -> {
                Toast.makeText(context,requireActivity().getString(R.string.pass_less_length),Toast.LENGTH_SHORT).show()
            }
            confirmPass.isEmpty() -> {
                Toast.makeText(context,requireActivity().getString(R.string.cpass_not_empty),Toast.LENGTH_SHORT).show()
            }
            pass != confirmPass -> {
                Toast.makeText(context,requireActivity().getString(R.string.pasword_not_match),Toast.LENGTH_SHORT).show()
            }
            else -> {
                createUserAccount() //llamada a la función para crear un usuario en firebase auth si los datos son válidos
            }
        }
    }

    /**
     * Crea una cuenta de usuario en firebase auth
     */
    private fun createUserAccount() {
        //show progress dialog
        progressDialog.setMessage("Creando cuenta")
        progressDialog.show()

        //Creación de email y contraseña en Firebase auth
        firebaseAuth.createUserWithEmailAndPassword(email, pass)
            .addOnSuccessListener {
                updateUserInfo()

            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
//                Log.e("Error crear cuenta",""+e)
                Toast.makeText(context,requireActivity().getString(R.string.fail_to_create_account)
                        +" "+e.localizedMessage,Toast.LENGTH_LONG).show()
            }
    }

    /**
     * Guardar los datos en cloud firestore database
     */
    private fun updateUserInfo() {
        progressDialog.setMessage("Guardando información del usuario")
        val timestamp = System.currentTimeMillis()
        val date = Date(timestamp)  //parsea los milisengundos del sistema a fecha

        //ID autogenerado por firebaseauth y se lo pasamos a la key uid de la colección hashmap
        val  uid = firebaseAuth.uid

        val userAccountHashMap: HashMap<String, Any?> = hashMapOf(
            "uid" to uid,
            "email" to email,
            "name" to name,
            "profileImage" to "",
            "timestamp" to date
        )
        //Inserta el usuario que se acaba de registrar en la colección client si es exitoso
        val dbUserRef = FirebaseFirestore.getInstance().collection("users")
        if (uid != null) {
            dbUserRef.document(uid).collection("user").document(uid).set(userAccountHashMap)
                .addOnSuccessListener {
                    progressDialog.dismiss()
                    Toast.makeText(context,requireActivity().getString(R.string.successful_saving_user_info),Toast.LENGTH_SHORT).show()
                    //Navega al fragment login
                    findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
                }
                .addOnFailureListener { e ->
                    progressDialog.dismiss()
                    Toast.makeText(context,requireActivity().getString(R.string.failed_saving_user_info)+" "+{e.message},Toast.LENGTH_LONG).show()
                }
        }
    }
}