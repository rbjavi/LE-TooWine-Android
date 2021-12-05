package com.jruizb.toowine.usecases.account

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jruizb.toowine.R
import com.jruizb.toowine.databinding.FragmentSignupBinding
import com.jruizb.toowine.domain.UserProfile
import com.jruizb.toowine.main.HomeActivity
import java.util.*
import kotlin.collections.HashMap



class SignupFragment : Fragment() {
    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    private val homeActivity: HomeActivity? = null

    private lateinit var profile: UserProfile

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
        firebaseAuth = FirebaseAuth.getInstance()

        //Inicialización de progressDialog
        if (context!=null) {
            progressDialog = ProgressDialog(context)
            progressDialog.setTitle("Cargando...")
            progressDialog.setCanceledOnTouchOutside(false)
        }

        binding.registerMaterialButton.setOnClickListener {
//            Log.i("CL","ENTRANDO POR EL EVENTO CLICK LISTENER")
            validateData()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): SignupFragment {
            return SignupFragment()
        }
    }

    /**
     * Función para validar los datos del registro de usuario
     */
    private fun validateData() {
        var userList = mutableListOf<String>()

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
//        val dt = Instant.ofEpochSecond(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime()

        //id autogenerado por firebaseauth y se lo pasamos a la variable uid
        val  uid = firebaseAuth.uid

        val userAccountHashMap: HashMap<String, Any?> = hashMapOf(
            "uid" to uid,
            "email" to email,
            "name" to name,
            "profileImage" to "",
            "timestamp" to date
        )
        //Inserta el usuario que se acaba de registrar en la colección client si es exitoso
        val db = FirebaseFirestore.getInstance().collection("client")
            db.document().set(userAccountHashMap)
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


//    private fun replaceFragment(fragment: Fragment) {
//        val transaction = requireActivity().supportFragmentManager.beginTransaction()
//        transaction.replace(R.id.fragmentContainer, fragment)
//        transaction.commit()
//    }

//    private fun stringtoDate(dates: String): Date {
//        val sdf = SimpleDateFormat("EEE, MMM dd yyyy",
//            Locale.ENGLISH)
//        var date: Date? = null
//        try {
//            date = sdf.parse(dates)
//            println(date)
//        } catch (e: ParseException) {
//            e.printStackTrace()
//        }
//        return date!!
//    }

//    override fun onClick(v: View?) {
//        if (v != null){
//            Navigation.findNavController(v).navigate(R.id.action_signupFragment_to_profileFragment)
//        }
//    }


}