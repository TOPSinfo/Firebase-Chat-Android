package com.app.demo.ui.chat.adapter

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.demo.R
import com.app.demo.ui.chat.activity.ChatActivity
import com.app.demo.model.chat.MessagesModel
import com.app.demo.utils.Utility
import com.google.firebase.auth.FirebaseAuth


class ChatAdapter(context: ChatActivity, private val messageList: ArrayList<MessagesModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var layoutInflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val view = layoutInflater.inflate(R.layout.item_chat_me, parent, false)
            MyMessagesViewHolder(view)
        } else {
            val view = layoutInflater.inflate(R.layout.item_chat_other, parent, false)
            OtherViewHolder(view)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is OtherViewHolder) {
            holder.bindItems(messageList[position])
        } else if (holder is MyMessagesViewHolder) {
            holder.bindItems(messageList[position])
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (messageList[position].senderId
                .equals(FirebaseAuth.getInstance().currentUser!!.uid)
        ) {
            0
        } else 1
    }


    class OtherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var txtMessageOther: TextView = itemView.findViewById(R.id.txtMessageOther)
        private var txtDateTime: TextView = itemView.findViewById(R.id.txtDateTime)

        fun bindItems(chatModel: MessagesModel) {
            txtMessageOther.text = chatModel.message
           /* if (!TextUtils.isEmpty(chatModel.timeStamp)) {
                txtDateTime.text = chatModel.timeStamp?.toLong()?.let { Utility.getDate(it) }
            }*/
        }

    }


    class MyMessagesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var txtMessage: TextView = itemView.findViewById(R.id.txtMessage)
        private var txtDateTime: TextView = itemView.findViewById(R.id.txtDateTime)
        fun bindItems(chatModel: MessagesModel) {
            txtMessage.text = chatModel.message
            /*if (!TextUtils.isEmpty(chatModel.timeStamp)) {
                txtDateTime.text = chatModel.timeStamp?.toLong()?.let { Utility.getDate(it) }
            }*/
        }
    }
}