package com.jruizb.toowine.usercases

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentHostCallback
import com.google.firebase.auth.FirebaseAuth
import com.jruizb.toowine.R
import com.jruizb.toowine.databinding.FragmentLoginBinding
import com.jruizb.toowine.databinding.FragmentProfileBinding
import com.jruizb.toowine.databinding.FragmentSignupBinding
import com.jruizb.toowine.home.HomeActivity
import org.jetbrains.anko.support.v4.progressDialog


/**
 * A simple [Fragment] subclass.
 * Use the [SignupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignupFragment : Fragment() {
    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    private val globalContext: Context? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
//        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

     //Inicializacion de firebase para obtener una instancia de ese objeto
    firebaseAuth = FirebaseAuth.getInstance()

    progressDialog = ProgressDialog(context)



    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun getContext(): Context? {
        return globalContext
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}