package com.jruizb.toowine.domain


/**
 * TooWine
 *
 * Creado por Javier RB
 * A fecha 22/11/2021
 */
data class UserProfile(
    var name:String,
    var email:String,
    var pass:String,
    var confirmPass:String
)
