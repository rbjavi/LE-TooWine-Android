package com.jruizb.toowine.domain

import android.widget.ImageView


/**
 * TooWine
 *
 * Creado por Javier RB
 * A fecha 16/10/2021
 */
data class WineItems(
    var imageViewUrl: String,
    val name: String,
    val originCertificate: String,
    val currentPrice: String,
    val originalPrice: String,
    var favStatus:Boolean = false
)


