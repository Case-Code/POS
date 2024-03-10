package com.casecode.domain.repository

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthService {
    val currentUserId: String
    val currentUser: Flow<FirebaseUser?>
    // val hasUserUID:Boolean
}