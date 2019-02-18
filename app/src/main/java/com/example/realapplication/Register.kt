package com.example.realapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class Register : AppCompatActivity() {

    val auth = FirebaseAuth.getInstance()

    val PICK_PHOTO = 100
    val PICK_CAMERA = 101
    var PHOTO_URI: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initView()

    }

    private fun initView() {

        btn_register_user.setOnClickListener {
            registerUserToFirebase()
        }

        iv_register_photo_profile.setOnClickListener {
            getPhotoFromPhone()
        }

    }

    private fun getPhotoFromPhone() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_PHOTO)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_PHOTO) {
            if (resultCode == Activity.RESULT_OK && data!!.data != null) {
                PHOTO_URI = data.data
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, PHOTO_URI)
                iv_register_photo_profile.setImageBitmap(bitmap)
            }
        }
    }

    private fun registerUserToFirebase() {
        //validation first
        auth.createUserWithEmailAndPassword(txt_email.text.toString(), txt_password.text.toString())
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "User berhasil dicreate ", Toast.LENGTH_LONG).show()
                    //email dan password SUDAH berhasil di register
                    //kita upload fotonya
                    uploadPhotoToFirebase()

                } else {
                    Toast.makeText(this, it.result.toString(), Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, it.message.toString(), Toast.LENGTH_LONG).show()
            }

    }

    private fun uploadPhotoToFirebase() {

        val photoName = UUID.randomUUID().toString()
        val uploadFirebase = FirebaseStorage.getInstance().getReference("rap/images/$photoName")

        uploadFirebase.putFile(PHOTO_URI!!)
            .addOnSuccessListener {
                uploadFirebase.downloadUrl.addOnSuccessListener {
                    Toast.makeText(this, "$it", Toast.LENGTH_LONG).show()
                    //SIMPAN SEMUA DATA KE DALAM DATABASE
                    saveAllUserDataToDatabase(it.toString())

                }

            }


    }

    private fun saveAllUserDataToDatabase(photoUrl: String) {
        val uid = FirebaseAuth.getInstance().uid
        val db = FirebaseDatabase.getInstance().getReference("user/$uid")

        db.setValue(
            User(
                txt_nama.text.toString(),
                photoUrl,
                txt_email.text.toString()))

            .addOnSuccessListener {
               Toast.makeText(this,"Data sudah di simpan ke database",Toast.LENGTH_LONG).show()
                Home.launcIntentClearTask(this)
            }
            .addOnFailureListener {

            }


    }

    companion object {

        fun launchIntent(context: Context) {
            val intent = Intent(context, Register::class.java)
            context.startActivity(intent)
        }
    }
}
