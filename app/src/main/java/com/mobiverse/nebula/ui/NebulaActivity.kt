package com.mobiverse.nebula.ui

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.firebase.auth.FirebaseAuth
import com.jakewharton.timber.Timber

class NebulaActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private var hasAudioPermission by mutableStateOf(false)

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            hasAudioPermission = true
            Timber.i("RECORD_AUDIO permission granted.")
        } else {
            Timber.w("RECORD_AUDIO permission denied.")
            // Optionally, you can show a rationale to the user here.
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        auth = FirebaseAuth.getInstance()
        Timber.plant(Timber.DebugTree())

        // Request the permission
        requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    if (hasAudioPermission) {
                        // Permission granted, show the main chat screen
                        ChatScreen()
                    } else {
                        // Permission not granted, show a placeholder
                        Box(contentAlignment = Alignment.Center) {
                            Text("Please grant microphone permission to use this feature.")
                        }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        signInAnonymously()
    }

    private fun signInAnonymously() {
        if (auth.currentUser == null) {
            auth.signInAnonymously()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Timber.d("signInAnonymously:success, UID: ${auth.currentUser?.uid}")
                    } else {
                        Timber.w(task.exception, "signInAnonymously:failure")
                    }
                }
        }
    }
}
