package com.sebade.mymovie.sign.signing

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.sebade.mymovie.HomeActivity
import com.sebade.mymovie.databinding.ActivitySignInBinding
import com.sebade.mymovie.sign.signup.SignUpActivity
import com.sebade.mymovie.utils.Preferences


class SignInActivity : AppCompatActivity() {

    private lateinit var activitySignInBinding: ActivitySignInBinding
    private lateinit var et_username: String
    private lateinit var et_password: String
    private lateinit var mDatabase: DatabaseReference
    private lateinit var preference : Preferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySignInBinding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(activitySignInBinding.root)

        mDatabase = FirebaseDatabase.getInstance().getReference("User")
        preference = Preferences(this@SignInActivity)

        preference.setValue("onboarding", "1")

        if(preference.getValue("status") == "1"){
            finishAffinity()
            var intent = Intent(this@SignInActivity, HomeActivity::class.java)
            startActivity(intent)
        }

        with(activitySignInBinding) {

            btnHome.setOnClickListener {
                et_username = etUsername.text.toString()
                et_password = etPassword.text.toString()
                Log.d(
                    "inputtag",
                    "onCreate: ${etUsername.text.toString()} ${etPassword.text.toString()}"
                )
                if (et_password.equals("")) {
                    etPassword.error = "Password Tidak Boleh Kosong"
                    etPassword.requestFocus()
                }
                if (et_username.equals("")) {
                    etUsername.error = "Username Tidak Boleh Kosong"
                    etUsername.requestFocus()
                } else {
                    pushLogin(et_username, et_password)
                }
            }

            btnDaftar.setOnClickListener{
                var intent = Intent(this@SignInActivity, SignUpActivity::class.java)
                startActivity(intent)
            }
        }

    }

    private fun pushLogin(etUsername: String, etPassword: String) {
        mDatabase.child(etUsername).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var user = snapshot.getValue(User::class.java)
                Log.d(
                    "inputtag",
                    "onDataChange: onDataChange snapshot => ${snapshot} || ${snapshot.getValue(User::class.java).toString()} || $user "
                )
                if(user == null){
                    Toast.makeText(this@SignInActivity, "Username tidak ditemukan", Toast.LENGTH_LONG).show()
                }else {
                    if (user.password == etPassword){
                        preference.setValue("nama", user.nama.toString())
                        preference.setValue("username", user.username.toString())
                        preference.setValue("email", user.email.toString())
                        preference.setValue("status", "1")
                        preference.setValue("saldo", user.saldo.toString())
                        preference.setValue("url", user.url.toString())
                        var intent = Intent(this@SignInActivity, HomeActivity::class.java)
                        startActivity(intent)
                    }
                    else {
                        Toast.makeText(this@SignInActivity, "Password Salah", Toast.LENGTH_LONG).show()
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SignInActivity, "Databases Error", Toast.LENGTH_LONG).show()
            }

        })
    }

    fun testFirebase() {
        val database = Firebase.database
        val refValue = database.getReference("testBOW")
        refValue.setValue("testtest")
        Log.d("test1", "asdasdsad")
    }

    fun checkInternetConnection(context: Context): Boolean {
        // register activity with the connectivity manager service
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // if the android version is equal to M
        // or greater we need to use the
        // NetworkCapabilities to check what type of
        // network has the internet connection
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // Returns a Network object corresponding to
            // the currently active default data network.
            val network = connectivityManager.activeNetwork ?: return false

            // Representation of the capabilities of an active network.
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                // Indicates this network uses a Wi-Fi transport,
                // or WiFi has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                // Indicates this network uses a Cellular transport. or
                // Cellular has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                // else return false
                else -> false
            }
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

}