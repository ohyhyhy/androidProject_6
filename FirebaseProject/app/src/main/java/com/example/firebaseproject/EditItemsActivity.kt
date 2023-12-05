package com.example.firebaseproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebaseproject.databinding.ActivityEdititemsBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.SetOptions

class EditItemsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEdititemsBinding
    private val db: FirebaseFirestore = Firebase.firestore
    private val itemsCollectionRef = db.collection("items")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEdititemsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var drp = intent.getStringExtra("documentReferencePath")
        var receivedName = intent.getStringExtra("variableName")
        var receivedPrice = intent.getStringExtra("variablePrice")
        var receivedStatus = intent.getStringExtra("variableStatus")

        findViewById<TextView>(R.id.editText).setText(receivedName)
        findViewById<EditText>(R.id.editPrice).setText(receivedPrice)
        if(receivedStatus.equals("판매중")) {
            findViewById<CheckBox>(R.id.editStatus).isChecked = true
        }

        findViewById<Button>(R.id.button3).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        Log.e("Firestore12345678", "${drp?.split("/")?.last()} / $receivedName / $receivedPrice / $receivedStatus")
        findViewById<Button>(R.id.editButton).setOnClickListener {
            val updatePrice = findViewById<EditText>(R.id.editPrice).text.toString()
            var updateStatus = "판매완료"
            if (findViewById<CheckBox>(R.id.editStatus).isChecked) {
                updateStatus = "판매중"
            }

            Log.e("Firestore123456", "문서 업데이트 중: ${drp?.split("/")?.last()}, 가격: $updatePrice, 상태: $updateStatus")

            val data = mapOf(
                "price" to updatePrice,
                "status" to updateStatus
            )
            itemsCollectionRef.document(drp?.split("/")?.last()!!)
                .update(data)
                .addOnSuccessListener {
                    Log.e("Firestore12345678", "문서 수정 성공")
                    val intent = Intent(this@EditItemsActivity, MainActivity::class.java)
                    startActivity(intent)
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore12345678", "Error updating or creating document", e)
                }
        }
    }
}

