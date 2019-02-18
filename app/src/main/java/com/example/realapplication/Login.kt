package com.example.realapplication

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {

    val firebase = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initView()

    }

    private fun initView() {
        btn_login_login_user.setOnClickListener {

            loginToFirebase()

        }
    }

    private fun loginToFirebase() {
        val email = et_login_email.text.toString().trim()
        val password = et_login_password.text.toString()

        firebase.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {
               if(it.isSuccessful){
                   Toast.makeText(this,"Berhasil Login", Toast.LENGTH_LONG).show()
                   Home.launcIntentClearTask(this)
               }else {

               }
            }
            .addOnFailureListener {
                Toast.makeText(this,"Failure ${it.message}", Toast.LENGTH_LONG).show()
            }
    }

    companion object {

        fun launchIntent(context: Context){
            val intent = Intent(context, Login::class.java)
            context.startActivity(intent)
        }
    }
}