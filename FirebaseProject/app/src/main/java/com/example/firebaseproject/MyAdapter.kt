package com.example.firebaseproject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.QueryDocumentSnapshot

data class Item(val id: String, val name: String, val price: String, val status: String, val owner: String) {
    constructor(doc: QueryDocumentSnapshot) :
            this(doc.id, doc["name"].toString(), doc["price"].toString(), doc["status"].toString(), doc["owner"].toString())
    constructor(key: String, map: Map<*, *>) :
            this(key, map["name"].toString(), map["price"].toString(), map["status"].toString(), map["owner"].toString())
}

class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view)

class MyAdapter(private val context: Context, private var items: List<Item>) : RecyclerView.Adapter<MyViewHolder>() {
    fun interface OnItemClickListener {
        fun onItemClick(student_id: String)
    }

    fun getItem(position: Int): String? {
        return items[position].owner
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    fun updateList(newList: List<Item>) {
        items = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = items[position]
        holder.view.findViewById<TextView>(R.id.textID).text = item.name
        holder.view.findViewById<TextView>(R.id.textPrice).text = item.price
        holder.view.findViewById<TextView>(R.id.textStatus).text = item.status
        holder.view.findViewById<TextView>(R.id.textOwner).text = item.owner
        holder.view.findViewById<TextView>(R.id.textID).setOnClickListener {
            //AlertDialog.Builder(context).setMessage("You clicked ${ student.name }.").show()
            itemClickListener?.onItemClick(item.id)
        }
        holder.view.findViewById<TextView>(R.id.textStatus).setOnClickListener {
            //AlertDialog.Builder(context).setMessage("You clicked ${ student.name }.").show()
            itemClickListener?.onItemClick(item.id)
        }
    }

    override fun getItemCount() = items.size
}