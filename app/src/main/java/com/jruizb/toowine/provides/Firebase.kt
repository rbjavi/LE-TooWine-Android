package com.jruizb.toowine.provides


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jruizb.toowine.util.Constants


/**
 * TooWine
 *
 * Creado por Javier RB
 * A fecha 18/12/2021
 */
object Firebase {
    fun provideFirebaseAuthentication() = FirebaseAuth.getInstance()
    fun provideFirebaseFirestore() = FirebaseFirestore.getInstance()

    fun provideUsersRef(rootRef: FirebaseFirestore) = rootRef.collection(Constants.USERS_COLLECTION)
    fun provideUserRef(rootRef: FirebaseFirestore) = rootRef.collection(Constants.USER_SUBCOLLECTION)
    fun provideFavWinesRef(rootRef: FirebaseFirestore) = rootRef.collection(Constants.FAVORITEWINES_SUBCOLLECTION)

}