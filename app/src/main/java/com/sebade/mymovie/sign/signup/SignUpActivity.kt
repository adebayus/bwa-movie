package com.sebade.mymovie.sign.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.sebade.mymovie.databinding.ActivitySignUpBinding
import com.sebade.mymovie.sign.signing.User

class SignUpActivity : AppCompatActivity() {

    private lateinit var activitySignUpBinding: ActivitySignUpBinding

    private lateinit var mDatabaseReference: DatabaseReference
    private lateinit var mFirebaseInstance: FirebaseDatabase
//    private lateinit var mDatabase : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySignUpBinding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(activitySignUpBinding.root)

        mFirebaseInstance = FirebaseDatabase.getInstance()
//        mDatabase = FirebaseDatabase.getInstance().getReference()
        mDatabaseReference = mFirebaseInstance.getReference("User")

        with(activitySignUpBinding) {
            btnLanjutkan.setOnClickListener {
                var et_username = etUsername.text.toString()
                var et_nama = etNama.text.toString()
                var et_password = etPassword.text.toString()
                var et_email = etEmail.text.toString()
                if (et_username == "" || et_nama == "" || et_password == "" || et_email == "") {
                    if (et_username == "") {
                        etUsername.error = "Silahkan diisi"
                        etUsername.requestFocus()
                    }
                    if (et_nama == "") {
                        etNama.error = "Silahkan diisi"
                        etNama.requestFocus()
                    }
                    if (et_password == "") {
                        etPassword.error = "Silahkan diisi"
                        etPassword.requestFocus()
                    }
                    if (et_email == "") {
                        etEmail.error = "Silahkan diisi"
                        etEmail.requestFocus()
                    }
                } else {
                    saveUsername(et_username, et_nama, et_password, et_email)
                }
            }


        }

    }

    private fun saveUsername(
        et_username: String,
        et_nama: String,
        et_password: String,
        et_email: String
    ) {
        var user = User()
        user.nama = et_nama
        user.email = et_email
        user.password = et_password
        user.username = et_username

        if (et_username != null) {

            checkingUsername(et_username, user)
        }
    }

    private fun checkingUsername(et_username: String, userData: User) {

        mDatabaseReference.child(et_username).addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                Toast.makeText(this@SignUpActivity, "loop", Toast.LENGTH_SHORT).show()

                if (user == null) {
                    mDatabaseReference.child(et_username).setValue(userData)
                    var goSignUpPhotoScreenActivity =
                        Intent(this@SignUpActivity, SignUpPhotoScreenActivity::class.java)
                            .putExtra("nama", userData.nama)
                    startActivity(goSignUpPhotoScreenActivity)
                    finishAffinity()
                    Log.d("testtest", "onDataChange: ${user} ${snapshot} ")
                } else {
                    Log.d("testtesttest", " ${user == null} onDataChange: ${user} ${snapshot} ")
                    Toast.makeText(this@SignUpActivity, "user Sudah ada @@SignUpActivity", Toast.LENGTH_LONG).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SignUpActivity, "Database error : ${error}", Toast.LENGTH_LONG)
                    .show()
            }

        })

    }
}