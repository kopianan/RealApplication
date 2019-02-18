package com.example.realapplication

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()

    }

    private fun initView() {

        btn_main_login.setOnClickListener {
            Login.launchIntent(this)
        }

        btn_main_register.setOnClickListener {
            Register.launchIntent(this)
        }

    }

    companion object {
        fun launcIntent(context : Context){
            val intent  = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }

        fun launcIntentClearTask(context : Context){
            val intent  = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(intent)

        }
    }
}
