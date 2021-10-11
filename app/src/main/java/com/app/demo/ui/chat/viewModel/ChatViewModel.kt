package com.app.demo.ui.chat.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.demo.data.repository.ChatRepository
import com.app.demo.model.chat.MessagesModel
import com.app.demo.model.user.UsersList
import com.app.demo.network.NetworkHelper
import com.app.demo.network.Resource
import com.app.demo.utils.Constant
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {


    private val _messagesResponse: MutableLiveData<Resource<ArrayList<MessagesModel>>> =
        MutableLiveData()
    val messageDataResponse: LiveData<Resource<ArrayList<MessagesModel>>> get() = _messagesResponse

    private val _sendMessagesResponse: MutableLiveData<Resource<Boolean>> =
        MutableLiveData()
    val sendMessagesResponse: LiveData<Resource<Boolean>> get() = _sendMessagesResponse


    /**
     * Get chat messages list and single message also on runtime
     */
    fun getMessagesList(otherUserId: String) = viewModelScope.launch {

        if (networkHelper.isNetworkConnected()) {
            val docId = getCurrentDocumentId(otherUserId)

            chatRepository.getChatMessageRepository(docId)
                .addSnapshotListener(object : EventListener<QuerySnapshot?> {

                    override fun onEvent(
                        value: QuerySnapshot?,
                        error: FirebaseFirestoreException?
                    ) {
                        if (error != null) {
                            return
                        }

                        val chatList = ArrayList<MessagesModel>()

                        for (dc in value?.documentChanges!!) {
                            when (dc.type) {

                                DocumentChange.Type.ADDED -> {
                                    println("ABC::Added::" + dc.document.data)
                                    val messageModel = UsersList.getMessagesModel(dc.document)
                                    chatList.add(messageModel)
                                }
                                DocumentChange.Type.MODIFIED -> println("ABC::Modified::" + dc.document.data)
                                DocumentChange.Type.REMOVED -> println("ABC::Removed::" + dc.document.data)
                            }
                        }

                        if (chatList.size > 0) {
                            _messagesResponse.postValue(Resource.success(chatList))
                        }
                    }
                })
        } else _messagesResponse.postValue(
            Resource.error(
                Constant.MSG_NO_INTERNET_CONNECTION,
                null
            )
        )
    }


    /**
     * Send message to other user
     */
    fun sendMessage(otherUserId: String, messageText: String) = viewModelScope.launch {
        if (networkHelper.isNetworkConnected()) {
            val doc = chatRepository.getSendMessagePath(getCurrentDocumentId(otherUserId))

            val data = hashMapOf(
                Constant.FIELD_MESSAGE to messageText,
                Constant.FIELD_RECEIVER_ID to otherUserId,
                Constant.FIELD_SENDER_ID to FirebaseAuth.getInstance().currentUser?.uid.toString(),
                Constant.FIELD_TIMESTAMP to Timestamp(Date()),
            )

            doc.document().set(data).addOnSuccessListener {
                _sendMessagesResponse.postValue(Resource.success(true))
            }.addOnFailureListener {
                _sendMessagesResponse.postValue(Resource.success(false))
            }
        } else _sendMessagesResponse.postValue(
            Resource.error(
                Constant.MSG_NO_INTERNET_CONNECTION,
                null
            )
        )
    }


    /**
     * Get document id of both user of chat
     */
    private fun getCurrentDocumentId(otherUserId: String): String {
        val myUserId = FirebaseAuth.getInstance().currentUser?.uid.toString()
        return if (myUserId < otherUserId) {
            myUserId + "" + otherUserId
        } else {
            otherUserId + myUserId
        }
    }

}