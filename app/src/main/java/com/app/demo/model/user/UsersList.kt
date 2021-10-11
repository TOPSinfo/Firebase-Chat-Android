package com.app.demo.model.user

import com.app.demo.model.chat.MessagesModel
import com.app.demo.utils.Constant
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

object UsersList {

    /**
     * make array list of order from firestore snapshot
     */

    fun getUserArrayList(
        querySnapshot: QuerySnapshot,
        userId: String
    ): ArrayList<UserModel> {
        val userArrayList = ArrayList<UserModel>()
        for (doc in querySnapshot.documents) {
            val userModel = UserModel()

            doc.get(Constant.FIELD_UID)?.let {
                userModel.uid = it.toString()
            }
            doc.get(Constant.FIELD_EMAIL)?.let {
                userModel.email = it.toString()
            }
            doc.get(Constant.FIELD_LAST_NAME)?.let {
                userModel.lastName = it.toString()
            }
            doc.get(Constant.FIELD_FIRST_NAME)?.let {
                userModel.firstName = it.toString()
            }
            doc.get(Constant.FIELD_PHONE)?.let {
                userModel.phone = it.toString()
            }
            if (doc.get(Constant.FIELD_UID) != userId) {
                userArrayList.add(userModel)
            }

        }
        return userArrayList
    }


    fun getMessagesModel(
        querySnapshot: QueryDocumentSnapshot
    ): MessagesModel {

        val messagesModel = MessagesModel()

        querySnapshot.get(Constant.FIELD_MESSAGE)?.let {
            messagesModel.message = it.toString()
        }
        querySnapshot.get(Constant.FIELD_SENDER_ID)?.let {
            messagesModel.senderId = it.toString()
        }
        querySnapshot.get(Constant.FIELD_RECEIVER_ID)?.let {
            messagesModel.receiverId = it.toString()
        }
        querySnapshot.get(Constant.FIELD_TIMESTAMP)?.let {
            messagesModel.timeStamp = it.toString()
        }

        return messagesModel
    }

    fun getUserModel(
        querySnapshot: QueryDocumentSnapshot
    ): UserModel {

        val messagesModel = UserModel()

        querySnapshot.get(Constant.FIELD_UID)?.let {
            messagesModel.uid = it.toString()
        }
        querySnapshot.get(Constant.FIELD_EMAIL)?.let {
            messagesModel.email = it.toString()
        }
        querySnapshot.get(Constant.FIELD_LAST_NAME)?.let {
            messagesModel.lastName = it.toString()
        }
        querySnapshot.get(Constant.FIELD_FIRST_NAME)?.let {
            messagesModel.firstName = it.toString()
        }

        querySnapshot.get(Constant.FIELD_PHONE)?.let {
            messagesModel.phone = it.toString()
        }

        return messagesModel
    }
}