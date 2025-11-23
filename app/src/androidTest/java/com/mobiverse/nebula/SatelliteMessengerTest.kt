package com.mobiverse.nebula

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.firebase.auth.FirebaseAuth
import com.mobiverse.nebula.data.AppDatabase
import com.mobiverse.nebula.satellite.SatelliteMessenger
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class SatelliteMessengerTest {

    private lateinit var db: AppDatabase
    private lateinit var messenger: SatelliteMessenger
    private lateinit var auth: FirebaseAuth

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Use an in-memory database for testing
        db = AppDatabase.getInstance(context)
        messenger = SatelliteMessenger(context)
        auth = FirebaseAuth.getInstance()

        // Sign in anonymously for testing
        runBlocking {
            auth.signInAnonymously().addOnCompleteListener {
                if (!it.isSuccessful) {
                    throw IOException("Firebase anonymous sign-in failed", it.exception)
                }
            }
        }
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        // Clear the database after each test
        db.clearAllTables()
        db.close()
        auth.signOut()
    }

    @Test
    @Throws(Exception::class)
    fun testSendMessage_storesMessageInDbAsPending() = runBlocking {
        // 1. Create a dummy file to "send"
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val dummyPcmFile = File.createTempFile("test_audio", ".pcm", context.cacheDir)
        dummyPcmFile.writeBytes(ByteArray(1024))

        val testRecipientId = "test_recipient_123"

        // 2. Call the method to be tested
        messenger.sendVoiceMessageAsync(dummyPcmFile, testRecipientId)

        // 3. Verify the result
        // We need to wait a bit for the async operations to complete
        Thread.sleep(1000) 

        val pendingMessages = db.messageDao().getPendingMessages()

        // There should be one pending message
        assertEquals(1, pendingMessages.size)

        val message = pendingMessages[0]
        assertNotNull(message)
        assertEquals("PENDING", message.status)
        assertEquals(testRecipientId, message.receiverId)
        assertNotNull(message.voicePath)
    }
}
