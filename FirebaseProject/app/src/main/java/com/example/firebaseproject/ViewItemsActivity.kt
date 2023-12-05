package com.example.firebaseproject

import android.content.Intent
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.firebaseproject.databinding.ActivityViewitemsBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ViewItemsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityViewitemsBinding
    private val db: FirebaseFirestore = Firebase.firestore
    private val messageCollectionRef = db.collection("message")
    private lateinit var putOwner: String
    //private lateinit var putMessage: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewitemsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var receivedName = intent.getStringExtra("variableName2")
        var receivedPrice = intent.getStringExtra("variablePrice2")
        var receivedOwner = intent.getStringExtra("variableOwner")
        var receivedStatus = intent.getStringExtra("variableStatus2")
        var receivedContent = intent.getStringExtra("variableContent")
        //메세지 추가 부분 시작
        //var putOwner = intent.getStringExtra("addOwner")

        findViewById<TextView>(R.id.textView2).setText(receivedName)
        findViewById<TextView>(R.id.textView3).setText(receivedPrice)
        findViewById<TextView>(R.id.textView4).setText(receivedOwner)
        findViewById<TextView>(R.id.textView5).setText(receivedStatus)
        findViewById<TextView>(R.id.textView6).setText(receivedContent)

        findViewById<Button>(R.id.button2).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.sendMessage).setOnClickListener {
            //val intent = Intent(this@ViewItemsActivity, ChatRoomActivity::class.java)
            //startActivity(intent)
            val dialogBuilder = AlertDialog.Builder(this)
            val inflater = this.layoutInflater
            val dialogView = inflater.inflate(R.layout.activity_dialog, null)
            dialogBuilder.setView(dialogView)

            val editTextMessage = dialogView.findViewById<EditText>(R.id.editTextText)

            dialogBuilder.setTitle("메시지 보내기")
            dialogBuilder.setMessage("메시지를 입력해 주세요:")
            dialogBuilder.setPositiveButton("보내기") { _, _ ->
                val messageContent = editTextMessage.text.toString().trim()
                if (messageContent.isNotEmpty()) {
                    sendMessage(receivedOwner.toString(), messageContent)
                } else {
                    Toast.makeText(this, "Message cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            dialogBuilder.setNegativeButton("취소") { _, _ ->
                // User canceled the dialog
            }

            val alertDialog = dialogBuilder.create()
            alertDialog.show()
        }
    }

    private fun sendMessage(sellerId: String, messageContent: String) {
        //putMessage = findViewById<EditText>(R.id.editTextText).text.toString()
        putOwner = intent.getStringExtra("addOwner").toString()
        val messageMap = hashMapOf(
            "sender" to putOwner,
            "receiver" to sellerId,
            "message" to messageContent
        )
        messageCollectionRef.add(messageMap)
            .addOnSuccessListener {
                messageCollectionRef.get().addOnSuccessListener {
                    val messages = mutableListOf<Item>()
                    for (doc in it) {
                        messages.add(Item(doc))
                    }
                }
                Toast.makeText(this, "메세지 보내기에 성공했습니다.", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            }

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}