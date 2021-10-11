package com.app.demo.model.chat

data class MessagesModel(
    var senderId: String? = null,
    var receiverId: String? = null,
    var timeStamp: String? = null,
    var message: String? = null,
)
