package com.example.admin.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.messaging.FirebaseMessaging

object FirebaseProvider {
    private const val NON_DEFAULT_DATABASE_ID = "ai-studio-mastershoppingce-cf7e61c0-96c0-455b-888b-f1589cced8ca"

    val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    val firestore: FirebaseFirestore by lazy {
        // Explicitly using the named non-default database ID as requested
        FirebaseFirestore.getInstance(NON_DEFAULT_DATABASE_ID)
    }

    val storage: FirebaseStorage by lazy {
        FirebaseStorage.getInstance()
    }

    val messaging: FirebaseMessaging by lazy {
        FirebaseMessaging.getInstance()
    }
}
