package com.example.firebaseproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth

    private var adapter: MyAdapter? = null
    private val db: FirebaseFirestore = Firebase.firestore
    private val itemsCollectionRef = db.collection("items")
    private var snapshotListener: ListenerRegistration? = null
    private val recyclerViewItems by lazy { findViewById<RecyclerView>(R.id.recyclerViewItems) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth
        if (auth.currentUser == null) {
            startActivity(
                Intent(this, LoginActivity::class.java)
            )
            finish()
        }

        // recyclerview setup
        recyclerViewItems.layoutManager = LinearLayoutManager(this)
        adapter = MyAdapter(this, emptyList())
        recyclerViewItems.adapter = adapter

        updateList()  // list items on recyclerview

        findViewById<Switch>(R.id.switchStatus).setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                updateSellingList();
            } else {
                updateList();
            }
        }

        adapter!!.setOnItemClickListener(object : MyAdapter.OnItemClickListener {
            override fun onItemClick(item_id: String) {
                Toast.makeText(
                    applicationContext,
                    "Clicked item at position $item_id",
                    Toast.LENGTH_SHORT
                ).show()

                itemsCollectionRef.document(item_id).get()
                    .addOnSuccessListener { documentSnapshot ->
                        Log.e("Firestore12345", "${auth.currentUser?.getEmail().toString()}" + "/" + "${documentSnapshot.getString("owner").toString()}")

                        if (auth.currentUser?.getEmail().toString().equals(documentSnapshot.getString("owner").toString())) {
                            //현재 사용자와 판매글의 소유자가 같을 때 -> 글 편집
                            val intent = Intent(this@MainActivity, EditItemsActivity::class.java)
                            intent.putExtra("documentReferencePath", documentSnapshot.reference.path)
                            intent.putExtra("variableName", documentSnapshot.getString("name"))
                            intent.putExtra("variablePrice", documentSnapshot.getString("price"))
                            intent.putExtra("variableStatus", documentSnapshot.getString("status"))
                            startActivity(intent)
                            finish()
                        } else {
                            //다를 때 -> 글 보기
                            val intent = Intent(this@MainActivity, ViewItemsActivity::class.java)
                            //메세지 추가 부분 시작
                            intent.putExtra("addOwner", auth.currentUser?.getEmail().toString())
                            //메세지 추가 부부 끝
                            intent.putExtra("variableName2", documentSnapshot.getString("name"))
                            intent.putExtra("variablePrice2", documentSnapshot.getString("price"))
                            intent.putExtra("variableOwner", documentSnapshot.getString("owner"))
                            intent.putExtra("variableStatus2", documentSnapshot.getString("status"))
                            intent.putExtra("variableContent", documentSnapshot.getString("content"))
                            startActivity(intent)
                            finish()
                        }
                    }
                    .addOnFailureListener { exception ->
                        // 쿼리 실패 시 처리할 로직 추가
                    }
            }
        })

        findViewById<FloatingActionButton>(R.id.addFloatingButton).setOnClickListener { documentSnapshot ->
            val intent = Intent(this@MainActivity, AddItemsActivity::class.java)
            intent.putExtra("addOwner", auth.currentUser?.getEmail().toString())
            startActivity(intent)
            finish()
        }
 /*
        findViewById<Button>(R.id.signout)?.setOnClickListener {
            Firebase.auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }


  */

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.message -> {
                val intent = Intent(this, ViewMessageActivity::class.java)
                intent.putExtra("addOwner", auth.currentUser?.getEmail().toString())
                startActivity(intent)
            }
            R.id.logout -> {
                Firebase.auth.signOut()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            //R.id.message -> {startActivity(Intent(this, ViewMessageActivity::class.java))
            //intent.putExtra("addOwner", auth.currentUser?.getEmail().toString())}
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun updateSellingList() {
        itemsCollectionRef
            .whereEqualTo("status", "판매중")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val items = mutableListOf<Item>()
                for (doc in querySnapshot) {
                    items.add(Item(doc))
                }
                adapter?.updateList(items)
            }
            .addOnFailureListener { exception ->
                Log.e("updateSellingList", "Error getting documents: ", exception)
            }


    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
        snapshotListener?.remove()
    }

    private fun updateList() {
        itemsCollectionRef.get().addOnSuccessListener {
            val items = mutableListOf<Item>()
            for (doc in it) {
                items.add(Item(doc))
            }
            adapter?.updateList(items)
        }
    }


    companion object {
        const val TAG = "MainActivity"
    }

}