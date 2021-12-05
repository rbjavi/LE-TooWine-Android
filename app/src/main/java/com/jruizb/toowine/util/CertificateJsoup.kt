package com.jruizb.toowine.util

import android.annotation.SuppressLint
import android.util.Log
import java.security.KeyManagementException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


/**
 * TooWine
 *
 * Creado por Javier RB
 * A fecha 27/11/2021
 */
class CertificateJsoup {
    companion object {
        /**
         * To suppress certificate warnings for specific JSoup connection
         */
        fun socketFactory(): SSLSocketFactory {
            Log.v(Constants.FUN, "Inside socketFactory")
            val trustAllCerts = arrayOf<TrustManager>(@SuppressLint("CustomX509TrustManager")
            object : X509TrustManager {
                @SuppressLint("TrustAllX509TrustManager")
                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
                }

                @SuppressLint("TrustAllX509TrustManager")
                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            })
            Log.v(Constants.FUN, "Before sslContext")
            try {
                val sslContext = SSLContext.getInstance("TLS")
                sslContext.init(null, trustAllCerts, SecureRandom())
                return sslContext.socketFactory
            } catch (e: Exception) {
                when (e) {
                    is RuntimeException, is KeyManagementException -> {
                        throw RuntimeException("Failed to create a SSL socket factory", e)
                    }
                    else -> throw e
                }
            }
        }
    }
}