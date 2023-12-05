package com.example.firebaseproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebaseproject.databinding.ActivityAdditemsBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.util.Log

class AddItemsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdditemsBinding
    private var adapter: MyAdapter? = null
    private val db: FirebaseFirestore = Firebase.firestore
    private val itemsCollectionRef = db.collection("items")
    private lateinit var putText: String
    private lateinit var putPrice: String
    private lateinit var putContent: String
    private lateinit var putOwner: String
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityAdditemsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        findViewById<Button>(R.id.backButton2).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.addItem).setOnClickListener {
            putText = findViewById<EditText>(R.id.addTitle).text.toString()
            putPrice = findViewById<EditText>(R.id.addPrice).text.toString()
            putContent = findViewById<EditText>(R.id.addContent).text.toString()
            putOwner = intent.getStringExtra("addOwner").toString()
            val itemMap = hashMapOf(
                "name" to putText,
                "price" to putPrice,
                "status" to "판매중",
                "owner" to putOwner,
                "content" to putContent
            )
            itemsCollectionRef.add(itemMap)
                .addOnSuccessListener {
                    itemsCollectionRef.get().addOnSuccessListener {
                        val items = mutableListOf<Item>()
                        for (doc in it) {
                            items.add(Item(doc))
                        }
                        adapter?.updateList(items)
                    }
                    Toast.makeText(this, "글 등록에 성공했습니다.", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}