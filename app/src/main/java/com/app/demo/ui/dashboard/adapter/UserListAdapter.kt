package com.app.demo.ui.dashboard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.demo.R
import com.app.demo.model.user.UserModel
import com.app.demo.ui.dashboard.activity.DashboardActivity

class UserListAdapter(
    val context: DashboardActivity,
    var usersArrayList: ArrayList<UserModel>
) : RecyclerView.Adapter<UserListAdapter.UserViewHolder>() {
    private var layoutInflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = layoutInflater.inflate(R.layout.item_user_list, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.txtUserName.text = context.getString(
            R.string.concat_string,
            usersArrayList[position].firstName, usersArrayList[position].lastName
        )

        holder.itemView.setOnClickListener {
            context.redirectChatActivity(position)
        }
    }

    override fun getItemCount(): Int {
        return usersArrayList.size
    }


    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtUserName: TextView = itemView.findViewById(R.id.txtUserName)

    }
}