package com.example.firebaseproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ViewMessageActivity : AppCompatActivity() {
    private val db: FirebaseFirestore = Firebase.firestore
    private val messageCollectionRef = db.collection("message")
    private lateinit var putOwner: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewmessage)

        putOwner = intent.getStringExtra("addOwner").toString()
        val recyclerView = findViewById<RecyclerView>(R.id.messageRecyclerview)
        val adapter = MessageAdapter(this, emptyList())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)


        findViewById<Button>(R.id.button4).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        messageCollectionRef.whereEqualTo("receiver", putOwner)
            .get()
            .addOnSuccessListener {
                val items = mutableListOf<Message>()
                for (doc in it) {
                    items.add(Message(doc))
                }
                adapter?.updateList(items)
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error getting documents: ", exception)
            }

    }
}