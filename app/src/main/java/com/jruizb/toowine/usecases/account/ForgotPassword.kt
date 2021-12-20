package com.jruizb.toowine.usecases.account

import android.os.Bundle
import android.util.Patterns.EMAIL_ADDRESS
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.jruizb.toowine.R
import com.jruizb.toowine.databinding.FragmentForgotPasswordBinding


class ForgotPassword : Fragment() {

    private var _binding: FragmentForgotPasswordBinding? = null
    // Esta propiedad es sólo válida entre onCreateView y
    // onDestroyView.
    private val binding get() = _binding!!
    private var rEmail: EditText? = null

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Inicializacion de firebase para obtener una instancia de ese objeto
        firebaseAuth = FirebaseAuth.getInstance()

        rEmail = binding.emailETForgot

        binding.backToLoginForgot.setOnClickListener {
            findNavController().navigate(R.id.action_forgotPassword_to_loginFragment)
        }

        performForgetPassword()
    }

    private fun validateInput(): Boolean {
        if (rEmail?.text.toString() == "") {
            rEmail?.error = "Introduce un email"
            return false
        }
        // checking the proper email format
        if (!isEmailValid(rEmail?.text.toString())) {
            rEmail?.error = "Introduce un email válido"
            return false
        }
        return true
    }

    private fun isEmailValid(email: String): Boolean {
        return EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun performForgetPassword () {
        binding.continueButtonForgot.setOnClickListener {
            if (validateInput()) {
                val linkMail = rEmail?.text.toString()
                firebaseAuth.sendPasswordResetEmail(linkMail)
                    .addOnSuccessListener {
                        Toast.makeText(context, requireContext().getString(R.string.email_sent), Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_forgotPassword_to_loginFragment)
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context,requireActivity().getString(R.string.fail_to_send_recovery_email)
                                +" "+e.localizedMessage,Toast.LENGTH_LONG).show()
                    }

            }
        }
    }
}