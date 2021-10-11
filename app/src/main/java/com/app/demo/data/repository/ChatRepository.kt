package com.app.demo.data.repository

import com.app.demo.utils.Constant
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import javax.inject.Inject

class ChatRepository @Inject constructor() {

    private var firestoreDB = FirebaseFirestore.getInstance()

    fun getChatMessageRepository(docId: String): Query {
        return firestoreDB.collection(Constant.TABLE_MESSAGES)
            .document(docId)
            .collection(Constant.TABLE_MESSAGE_COLLECTION)
            .orderBy(Constant.FIELD_TIMESTAMP, Query.Direction.ASCENDING)
    }


    fun getUserList(): CollectionReference {
        return firestoreDB.collection(Constant.TABLE_USER)
    }

    fun getSendMessagePath(docId: String): CollectionReference {
        return firestoreDB.collection(Constant.TABLE_MESSAGES).document(docId)
            .collection(Constant.TABLE_MESSAGE_COLLECTION)
    }
}