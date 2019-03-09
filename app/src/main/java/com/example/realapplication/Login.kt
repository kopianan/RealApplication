package com.example.realapplication

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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

        btn_login_register_user.setOnClickListener {
            Register.launchIntent(this)
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
                   //setelah login berhasil dan masuk ke halaman HOME
                   //kita ambil data user nya
                   val uid = FirebaseAuth.getInstance().uid
                   val ref = FirebaseDatabase.getInstance().getReference("/user/$uid")
                   ref.addValueEventListener(object : ValueEventListener{
                       override fun onCancelled(p0: DatabaseError) {

                       }

                       override fun onDataChange(p0: DataSnapshot) {
                           currentUserData = p0.getValue(User::class.java)!!
                       }
                   })

               }else {

               }
            }
            .addOnFailureListener {
                Toast.makeText(this,"Failure ${it.message}", Toast.LENGTH_LONG).show()
            }
    }

    companion object {

        lateinit var currentUserData :User


        fun launchIntent(context: Context){
            val intent = Intent(context, Login::class.java)
            context.startActivity(intent)
        }

        fun launchIntentClearTask(context: Context){
            val intent = Intent(context, Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(intent)
        }
    }
}
