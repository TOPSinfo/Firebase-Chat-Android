package com.app.demo.ui.dashboard.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.demo.data.repository.ChatRepository
import com.app.demo.model.user.UserModel
import com.app.demo.model.user.UsersList
import com.app.demo.network.NetworkHelper
import com.app.demo.network.Resource
import com.app.demo.utils.Constant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {


    private val _userDataResponse: MutableLiveData<Resource<ArrayList<UserModel>>> =
        MutableLiveData()
    val userDataResponse: LiveData<Resource<ArrayList<UserModel>>> get() = _userDataResponse


    /**
     * Getting registered user list from firebase
     */

    fun getUsersList() = viewModelScope.launch {
        if (networkHelper.isNetworkConnected()) {
            _userDataResponse.value = Resource.loading(null)

            chatRepository.getUserList()
                .addSnapshotListener(object : EventListener<QuerySnapshot?> {

                    override fun onEvent(
                        value: QuerySnapshot?,
                        error: FirebaseFirestoreException?
                    ) {
                        if (error != null) {
                            return
                        }

                        val chatList = ArrayList<UserModel>()

                        for (dc in value?.documentChanges!!) {
                            when (dc.type) {

                                DocumentChange.Type.ADDED -> {
                                    val messageModel = UsersList.getUserModel(dc.document)
                                    if (messageModel.uid != FirebaseAuth.getInstance().currentUser?.uid.toString()) {
                                        chatList.add(messageModel)
                                    }
                                }
                                DocumentChange.Type.MODIFIED -> {
                                }
                                DocumentChange.Type.REMOVED -> {

                                }
                            }
                        }

                        if (chatList.size > 0) {
                            _userDataResponse.postValue(Resource.success(chatList))
                        }
                    }
                })
        } else _userDataResponse.postValue(
            Resource.error(
                Constant.MSG_NO_INTERNET_CONNECTION,
                null
            )
        )
    }

}