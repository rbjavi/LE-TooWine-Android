package com.jruizb.toowine.usecases

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.jruizb.toowine.databinding.ActivityDashBoardBinding
import com.jruizb.toowine.main.HomeActivity

class DashBoard : AppCompatActivity() {
    private lateinit var binding: ActivityDashBoardBinding

    private var firebaseFirestoreInstance: FirebaseFirestore? = null
    private var dbReference: CollectionReference? = null
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Inicialización de firebase auth
        firebaseAuth = FirebaseAuth.getInstance()
        //Inicialización de firestore para poder realizar las opeciaones de lectura o escritura
        firebaseFirestoreInstance = FirebaseFirestore.getInstance()
        //Obtiene la referencia de un documento de la db
        dbReference = firebaseFirestoreInstance!!.collection("client")

            progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Cargando...")
            progressDialog.setCanceledOnTouchOutside(false)

        getInfoFromCurrentProfile()
        profileLogout()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    private fun getInfoFromCurrentProfile() {
        val user = firebaseAuth.currentUser
        user?.let {
            with(binding){
                nameProfile.text = user.displayName
                emailProfile.text = user.email
                val photoUrl = user.photoUrl
            }
        }
    }

    private fun profileLogout() {
        binding.logoutProfileButton.setOnClickListener {
            progressDialog.setMessage("deslogueándose...")
            progressDialog.show()
            firebaseAuth.signOut()
            startActivity(Intent(this@DashBoard, HomeActivity::class.java))
        }
        progressDialog.dismiss()
    }
}