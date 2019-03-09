package com.example.realapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_room.*

class ChatRoomActivity : AppCompatActivity() {

    lateinit var friend: User
    val adapter = GroupAdapter<ViewHolder>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)

        friend = intent.getParcelableExtra<User>(FriendListActivity.FRIEND_KEY)
        supportActionBar!!.title = friend.name


        rv_chat_room_list.adapter = adapter

        initView()
        loadMessageFromFirebase()


    }

    private fun loadMessageFromFirebase() {

        val MyId = Login.currentUserData.uid
        val FriendId = friend.uid

        val reference = FirebaseDatabase.getInstance().getReference("/message-user/$MyId/$FriendId")

        reference.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {

                val messageCollection = p0.getValue(Message::class.java)

                if (messageCollection != null) {

                    if (messageCollection.fromId == FirebaseAuth.getInstance().uid) {
                        adapter.add(AdapterPesanUntuk(messageCollection.text, Login.currentUserData))
                        //disini kita tambahkan foto pengirim
                    }else{
                        adapter.add(AdapterPesanDari(messageCollection.text, friend))
                            //tambahkan foto teman chat
                    }


                }


            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }

        })

    }

    private fun initView() {
        btn_chat_room_send_message.setOnClickListener {

            sendMessage()

        }
    }

    private fun sendMessage() {

        //disini pesan akan diolah untuk di kirim ke firebase

        //kirim pesan ke user yang akan dikirimkan pesan
        val fromId = FirebaseAuth.getInstance().uid.toString()
        val toId = friend.uid

        val messageDbReference = FirebaseDatabase.getInstance().getReference("/message-user/$fromId/$toId").push()
        val messageDbReferenceTo = FirebaseDatabase.getInstance().getReference("/message-user/$toId/$fromId").push()
        val id = messageDbReference.key.toString()
        val text = et_chat_room_chat_message.text.toString()
        val time = System.currentTimeMillis() / 1000

        messageDbReference.setValue(
            Message(id, fromId, toId, text, time)
        )
            .addOnSuccessListener {
                Toast.makeText(applicationContext, "berhasil dikirim ke database", Toast.LENGTH_LONG).show()
                et_chat_room_chat_message.setText("")

                messageDbReferenceTo.setValue(
                    Message(id, fromId, toId, text, time)
                )
            }



    }

    companion object {

        fun launchIntent(context: Context) {
            val intent = Intent(context, ChatRoomActivity::class.java)
            context.startActivity(intent)
        }
    }
}
