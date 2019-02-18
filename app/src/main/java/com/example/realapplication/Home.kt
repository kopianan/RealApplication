package com.example.realapplication

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*

class Home : AppCompatActivity() {

    val firebase = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        checkUserLogin()
        initView()


    }

    private fun initView() {
        btn_home_sign_out.setOnClickListener {
            firebase.signOut()
            MainActivity.launcIntentClearTask(this)
        }
    }

    private fun  checkUserLogin(){
        if(firebase.currentUser == null) {
            //sedang tidak login
            MainActivity.launcIntentClearTask(this)
        }
    }

    companion object {
         fun launchIntent(context : Context){
            val intent = Intent(context, Home::class.java)
            context.startActivity(intent)
        }
        fun launcIntentClearTask(context : Context){
            val intent  = Intent(context, Home::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(intent)

        }
    }
}