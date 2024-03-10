package com.casecode.testing.di.app

import com.casecode.di.app.FirebaseModule
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.every
import io.mockk.mockk

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [FirebaseModule::class],
)
object TestFirebaseModule {
    private const val HOST = "127.0.0.1"
    private const val AUTH_PORT = 9099
    private const val FIRESTORE_PORT = 8080

    @Provides
    fun provideFirebaseAuthMockk(): FirebaseAuth {
        if (Firebase.auth.currentUser != null) {
            return Firebase.auth.also { it.useEmulator(HOST, AUTH_PORT) }
        } else {
            val mockFirebase = mockk<FirebaseAuth>()
            every { mockFirebase.currentUser?.uid } answers { "Test uid" }
            return mockFirebase
        }
    }

    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore = Firebase.firestore.also { it.useEmulator(HOST, FIRESTORE_PORT) }

    @Provides
    fun provideFirebaseStorage(): FirebaseStorage = Firebase.storage.also { mockk() }

    @Provides
    fun provideSignInClient() = mockk<SignInClient>()

    @Provides
    fun provideSignInRequest(): BeginSignInRequest {
        return mockk<BeginSignInRequest>()
    }
}