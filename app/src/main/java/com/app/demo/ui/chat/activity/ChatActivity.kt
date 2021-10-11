package com.app.demo.ui.chat.activity

import android.os.Bundle
import android.text.TextUtils
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.demo.R
import com.app.demo.core.BaseActivity
import com.app.demo.databinding.ActivityChatBinding
import com.app.demo.network.Status
import com.app.demo.ui.chat.adapter.ChatAdapter
import com.app.demo.ui.chat.viewModel.ChatViewModel
import com.app.demo.model.chat.MessagesModel
import com.app.demo.utils.toast

class ChatActivity : BaseActivity() {

    private val chatViewModel: ChatViewModel by viewModels()
    private lateinit var binding: ActivityChatBinding
    private var otherUserId: String? = null
    private var chatAdapter: ChatAdapter? = null
    private val messageList = ArrayList<MessagesModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    /**
     * Initialize ui and call method
     */
    private fun initUI() {

        otherUserId = intent.getStringExtra("user_id").toString()
        binding.txtUserName.text = intent.getStringExtra("user_name")


        setChatAdapter()
        setObserver()
        setClickListener()
        getChatMessagesList()

    }

    /**
     * Set up observer
     */
    private fun setObserver() {

        chatViewModel.messageDataResponse.observe(this, {
            when (it.status) {
                Status.LOADING -> {
                    showProgress(this)
                }
                Status.SUCCESS -> {
                    hideProgress()
                    it.data?.let { user ->
                        messageList.addAll(user)
                        if (messageList.size > 1) {
                            setChatAdapter()
                        } else {
                            chatAdapter?.notifyDataSetChanged()
                        }
                    }
                }
                Status.ERROR -> {
                    hideProgress()
                    it.message?.let { it1 -> toast(it1) }
                }
            }
        })

        chatViewModel.sendMessagesResponse.observe(this, {
            when (it.status) {
                Status.LOADING -> {
                    showProgress(this)
                }
                Status.SUCCESS -> {
                    hideProgress()
                    binding.edMessage.setText("")
                }
                Status.ERROR -> {
                    hideProgress()
                    it.message?.let { it1 -> toast(it1) }
                }
            }
        })
    }

    /**
     * Initialize click listener
     */
    private fun setClickListener() {

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }

        binding.rlSend.setOnClickListener {
            sendMessage()
        }
    }

    /**
     * Get chat messages list
     */
    private fun getChatMessagesList() {
        messageList.clear()
        chatViewModel.getMessagesList(otherUserId.toString())
    }


    /**
     * Send text message to other user
     */
    private fun sendMessage() {
        if (TextUtils.isEmpty(binding.edMessage.text.toString().trim())) {
            toast(getString(R.string.please_enter_message))
        } else {
            chatViewModel.sendMessage(
                otherUserId.toString(),
                binding.edMessage.text.toString().trim()
            )
            binding.edMessage.setText("")
        }
    }

    /**
     * Set chat messages adapter
     */
    private fun setChatAdapter() {

        binding.rvChatMessageList.layoutManager = LinearLayoutManager(this)
        chatAdapter = ChatAdapter(this, messageList)
        binding.rvChatMessageList.adapter = chatAdapter
        binding.rvChatMessageList.scrollToPosition(messageList.size - 1)

    }
}