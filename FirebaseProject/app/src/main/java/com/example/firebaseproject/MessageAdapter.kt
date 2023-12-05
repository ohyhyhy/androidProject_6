package com.example.firebaseproject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.QueryDocumentSnapshot

data class Message(val message: String, val sender: String) {
    constructor(doc: QueryDocumentSnapshot) : this(doc["message"].toString(), doc["sender"].toString())
}

class MessageAdapter(private val context: Context, private var messages: List<Message>) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    // 뷰홀더 클래스
    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textmessage)
        val textView2: TextView = itemView.findViewById(R.id.textsender)
        // 추가적인 뷰 요소가 있다면 여기에 추가
    }

    // onCreateViewHolder: 뷰홀더 객체 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.message, parent, false)
        return MessageViewHolder(itemView)
    }

    // onBindViewHolder: 뷰홀더에 데이터 바인딩
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val currentItem = messages[position]
        holder.textView.text = currentItem.message
        holder.textView2.text = currentItem.sender
        // 다른 뷰 요소에 대한 데이터 바인딩
    }

    fun updateList(newList: List<Message>) {
        messages = newList
        notifyDataSetChanged()
    }

    // getItemCount: 데이터 아이템 수 반환
    override fun getItemCount() = messages.size
}