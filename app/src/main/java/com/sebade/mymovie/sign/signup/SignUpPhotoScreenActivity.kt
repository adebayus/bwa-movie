package com.sebade.mymovie.sign.signup

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.sebade.mymovie.HomeActivity
import com.sebade.mymovie.R
import com.sebade.mymovie.databinding.ActivitySignUpPhotoscreenBinding
import com.sebade.mymovie.utils.Preferences
import java.util.*


class SignUpPhotoScreenActivity : AppCompatActivity(), MultiplePermissionsListener {

    private lateinit var binding: ActivitySignUpPhotoscreenBinding

    private var REQUEST_IMAGE_CAPTURE = 1
    private var statusAdd: Boolean = false
    private lateinit var filePath: Uri

    private lateinit var storage: FirebaseStorage
    private lateinit var sReference: StorageReference
    private lateinit var preferences: Preferences

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpPhotoscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = Preferences(this@SignUpPhotoScreenActivity)
        storage = FirebaseStorage.getInstance()
        sReference = storage.getReference()

        with(binding) {
            tvHello.text = "Selamat Datang\n${intent.getStringExtra("nama")}"
            ivAdd.setOnClickListener {
                if (statusAdd) {
                    statusAdd = false
                    btnSave.isInvisible = true
                    ivAdd.setImageResource(R.drawable.btn_upload)
                    ivProfile.setImageResource(R.drawable.user_pic)
                } else {
                    val manifest = listOf<String>(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )

                    Dexter.withActivity(this@SignUpPhotoScreenActivity)
                        .withPermissions(manifest)
                        .withListener(this@SignUpPhotoScreenActivity)
                        .check()
                }
            }

            btnHome.setOnClickListener {
                finishAffinity()
                startActivity(Intent(this@SignUpPhotoScreenActivity, HomeActivity::class.java))
            }

            btnSave.setOnClickListener {
                if (filePath != null) {
                    var progressDialog = ProgressDialog(this@SignUpPhotoScreenActivity)
                    progressDialog.setTitle("Uploading...")
                    progressDialog.show()

                    var ref = sReference.child("images/${UUID.randomUUID().toString()}")
                    ref.putFile(filePath)
                        .addOnSuccessListener {
                            progressDialog.dismiss()
                            Toast.makeText(
                                this@SignUpPhotoScreenActivity,
                                "berhasil di upload",
                                Toast.LENGTH_SHORT
                            ).show()

                            ref.downloadUrl.addOnSuccessListener {
                                preferences.setValue("url", it.toString())
                            }

                            finishAffinity()
                            startActivity(
                                Intent(
                                    this@SignUpPhotoScreenActivity,
                                    HomeActivity::class.java
                                )
                            )

                        }
                        .addOnFailureListener {
                            progressDialog.dismiss()
                            Toast.makeText(
                                this@SignUpPhotoScreenActivity,
                                "Gagal di upload",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .addOnProgressListener {
                            var progress = 100.0 * it.bytesTransferred / it.totalByteCount
                            progressDialog.setMessage("Upload ${progress.toInt()}%")
                        }
                }
            }
        }
    }

//    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
//        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
//            intent.resolveActivity(packageManager)?.also { componentName ->
//                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
//            }
//        }
//    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            var bitmap = data?.extras?.get("data") as Bitmap
            statusAdd = true

            filePath = data.data!!
            Glide.with(this@SignUpPhotoScreenActivity)
                .load(bitmap)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.ivProfile)

            binding.btnSave.visibility = View.VISIBLE
            binding.ivAdd.setImageResource(R.drawable.ic_btn_delete_24)

        }
    }

    override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
//        var test = p0
        if (p0!!.areAllPermissionsGranted()){
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
                intent.resolveActivity(packageManager)?.also { componentName ->
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
                }
            }
            Toast.makeText(this@SignUpPhotoScreenActivity, "Permissions Granted", Toast.LENGTH_SHORT).show()
        }else {
            Toast.makeText(this@SignUpPhotoScreenActivity, "Please Grant Permissions to use the app", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPermissionRationaleShouldBeShown(
        p0: MutableList<PermissionRequest>?,
        p1: PermissionToken?
    ) {
        Log.d("permission", "onPermissionRationaleShouldBeShown: ${p0} || ${p1} ")
    }
}

//override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
//    Toast.makeText(
//        this@SignUpPhotoScreenActivity,
//        "Permission ditolakkkk",
//        Toast.LENGTH_SHORT
//    ).show()
//}
//
//override fun onPermissionRationaleShouldBeShown(p0: PermissionRequest?, p1: PermissionToken?) {
//    Toast.makeText(
//        this@SignUpPhotoScreenActivity,
//        "Permission ditolak",
//        Toast.LENGTH_SHORT
//    ).show()
//}
//
//override fun onBackPressed() {
//    Toast.makeText(
//        this@SignUpPhotoScreenActivity,
//        "On Back Pressed",
//        Toast.LENGTH_SHORT
//    ).show()
//}