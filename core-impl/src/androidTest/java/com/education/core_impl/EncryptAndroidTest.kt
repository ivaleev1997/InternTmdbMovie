package com.education.core_impl

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.education.core_impl.data.local.encryption.Encryption
import com.education.core_impl.data.local.encryption.EncryptionImpl
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EncryptAndroidTest {


    private val appContext = InstrumentationRegistry.getInstrumentation().targetContext
    private val encryption: Encryption = EncryptionImpl(appContext)

    @Test
    fun encryptDecryptWithSamePasswordsTest() {
        val password = "7777"
        val expected = "hello"

        val encrypted = encryption.encrypt(password, expected)
        val actual = encryption.decrypt(password, encrypted)

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun encryptDecryptWithDifferentPasswordsTest() {
        val password1 = "7777"
        val password2 = "9543"
        val dataToEncrypt = "hello"
        val expected = ""

        val encrypted = encryption.encrypt(password1, dataToEncrypt)
        val actual = encryption.decrypt(password2, encrypted)

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun encryptDecryptWithoutPassword() {
        val dataToEncrypt = "hello"

        val encrypted = encryption.encrypt(dataToEncrypt)
        val actual = encryption.decrypt(encrypted)

        Assert.assertEquals(dataToEncrypt, actual)
    }
}