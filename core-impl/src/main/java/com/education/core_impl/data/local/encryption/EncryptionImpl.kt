package com.education.core_impl.data.local.encryption

import android.content.Context
import com.education.core_api.BLANK_STR
import com.education.core_api.extension.base64DecodeFromString
import com.education.core_api.extension.base64EncodeToString
import com.google.crypto.tink.Aead
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.aead.AeadKeyTemplates
import com.google.crypto.tink.config.TinkConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import javax.inject.Inject
import kotlin.text.Charsets.UTF_8

class EncryptionImpl @Inject constructor(
    context: Context
) : Encryption {
    companion object {
        private const val MASTER_KEY_URI = "android-keystore://my_master_key_id"
        private const val KEYS_PREFS_FILE = "keys_prefs"
        private const val USER_NAME_KEY_SET = "user_name_keyset_name"
        private const val USER_CREDENTIALS_KEY_SET = "credentials_keyset"
    }

    private val userCredentialsKeySetManager: AndroidKeysetManager
    private val userNameKeySetManager: AndroidKeysetManager
    init {
        TinkConfig.register()
        val keySetBuilder = AndroidKeysetManager.Builder()
            .withKeyTemplate(AeadKeyTemplates.AES128_GCM)
            .withMasterKeyUri(MASTER_KEY_URI)

        userCredentialsKeySetManager = keySetBuilder
            .withSharedPref(context, USER_CREDENTIALS_KEY_SET, KEYS_PREFS_FILE)
            .build()

        userNameKeySetManager = keySetBuilder
            .withSharedPref(context, USER_NAME_KEY_SET, KEYS_PREFS_FILE)
            .build()
    }

    override fun encrypt(password: String, data: String): String {
        return encrypt(data, userCredentialsKeySetManager, password.toByteArray())
    }

    override fun decrypt(password: String, data: String): String {
        return decrypt(data, userCredentialsKeySetManager, password.toByteArray())
    }

    override fun encrypt(data: String): String {
        return encrypt(data, userNameKeySetManager, null)
    }

    override fun decrypt(data: String): String {
        return decrypt(data, userNameKeySetManager, null)
    }

    private fun decrypt(data: String, keySetManager: AndroidKeysetManager, password: ByteArray?): String {
        AeadConfig.register()
        return try {
            val dataByteArray = data.base64DecodeFromString()
            val aead = keySetManager.keysetHandle.getPrimitive(Aead::class.java)

            val result = aead.decrypt(dataByteArray, password)

            String(result, UTF_8)
        } catch (e: Exception) {
            BLANK_STR
        }
    }

    private fun encrypt(data: String, keySetManager: AndroidKeysetManager, password: ByteArray?): String {
        AeadConfig.register()
        return try {
            val aead = keySetManager.keysetHandle.getPrimitive(Aead::class.java)
            val result = aead.encrypt(data.toByteArray(), password)

            result.base64EncodeToString()
        } catch (e: Exception) {
            BLANK_STR
        }
    }
}
