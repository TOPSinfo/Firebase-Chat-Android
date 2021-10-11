package com.app.demo.ui.dashboard.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.demo.core.BaseActivity
import com.app.demo.databinding.ActivityDashboardBinding
import com.app.demo.network.Status
import com.app.demo.ui.authentication.login.activity.LoginActivity
import com.app.demo.model.user.UserModel
import com.app.demo.ui.chat.activity.ChatActivity
import com.app.demo.ui.dashboard.viewModel.DashboardViewModel
import com.app.demo.ui.dashboard.adapter.UserListAdapter
import com.app.demo.utils.toast
import com.google.firebase.auth.FirebaseAuth

class DashboardActivity : BaseActivity() {

    private val dashboardViewModel: DashboardViewModel by viewModels()
    private lateinit var binding: ActivityDashboardBinding
    private var userListAdapter: UserListAdapter? = null

    private var userArrayList = ArrayList<UserModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    /**
     * Initializing ui and call all method
     */
    private fun initUI() {

        userArrayList = ArrayList()

        setAdapter()
        getUsersList()
        setObserver()

        binding.imgLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }
    }

    /**
     * Set up observer
     */
    private fun setObserver() {

        dashboardViewModel.userDataResponse.observe(this, {
            when (it.status) {
                Status.LOADING -> {
                    showProgress(this)
                }
                Status.SUCCESS -> {
                    hideProgress()
                    it.data?.let { user ->
                        userArrayList.addAll(user)
                        setAdapter()
                    }
                }
                Status.ERROR -> {
                    hideProgress()
                    it.message?.let { it1 -> toast(it1) }
                }
            }
        })
    }

    /**
     * Getting registered users list
     */
    private fun getUsersList() {
        dashboardViewModel.getUsersList()
    }

    /**
     * Set user adapter
     */
    private fun setAdapter() {

        binding.rvUserList.layoutManager = LinearLayoutManager(this)
        userListAdapter = UserListAdapter(this, userArrayList)
        binding.rvUserList.adapter = userListAdapter

    }

    /**
     * Redirect to chat activity after click on user
     */
    fun redirectChatActivity(position: Int) {
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("user_id", userArrayList[position].uid)
        intent.putExtra(
            "user_name",
            userArrayList[position].firstName + " " + userArrayList[position].lastName
        )
        startActivity(intent)
    }
}