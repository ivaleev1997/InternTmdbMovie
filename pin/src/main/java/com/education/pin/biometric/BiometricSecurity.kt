package com.education.pin.biometric

import android.annotation.TargetApi
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.Fragment
import com.education.core_api.BLANK_STR
import com.education.core_api.extension.base64DecodeFromString
import com.education.core_api.extension.base64EncodeToString
import com.education.pin.R
import timber.log.Timber
import java.security.KeyStore
import java.security.SecureRandom
import java.util.concurrent.Executor
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import kotlin.text.Charsets.UTF_8

@TargetApi(23)
class BiometricSecurity(
                        private val isFirstSetup: Boolean,
                        fragment: Fragment,
                        executor: Executor
) {
    companion object {
        private const val FINGERPRINT_KEY_ALIAS = "fingerprint_master_key_alias"
        private const val IV_SIZE_16 = 16
    }

    lateinit var authCallback: (BiometricAuthResult) -> Unit

    lateinit var masterPinKey: String

    private val biometricPrompt = BiometricPrompt(fragment, executor,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(
                errorCode: Int,
                errString: CharSequence
            ) {
                super.onAuthenticationError(errorCode, errString)
                authCallback.invoke(BiometricAuthResult(BiometricAuthStatus.ERROR))
            }

            override fun onAuthenticationSucceeded(
                result: BiometricPrompt.AuthenticationResult
            ) {
                super.onAuthenticationSucceeded(result)
                val masterKey = try {
                    if (isFirstSetup) {
                        // Для первого раза шифруем пин, чтобы далее его сохранить
                        val encrypted =
                            result.cryptoObject?.cipher?.doFinal(masterPinKey.toByteArray(UTF_8))

                        if (encrypted != null && encrypted.isNotEmpty()) {
                            val resultAndIv = ByteArray(encrypted.size + IV_SIZE_16)
                            encrypted.copyInto(resultAndIv)
                            result.cryptoObject?.cipher?.iv?.copyInto(resultAndIv, encrypted.size)
                            resultAndIv.base64EncodeToString()
                        } else
                            BLANK_STR
                    } else {
                        // Этот блок уже для расшифровки зашифрованного ранее пин-кода
                        val masterPinKeyBytes = masterPinKey.base64DecodeFromString()
                        val ivBytes = ByteArray(IV_SIZE_16)
                        val encryptedBytes = ByteArray(masterPinKeyBytes.size - IV_SIZE_16)
                        masterPinKeyBytes.copyInto(
                            ivBytes,
                            startIndex = masterPinKeyBytes.size - IV_SIZE_16
                        )
                        masterPinKeyBytes.copyInto(
                            encryptedBytes,
                            endIndex = masterPinKeyBytes.size - IV_SIZE_16
                        )

                        val decrypted = result.cryptoObject?.cipher?.doFinal(encryptedBytes)
                            ?: BLANK_STR.toByteArray(
                                UTF_8
                            )

                        String(decrypted, UTF_8)
                    }
                } catch (e: Exception) {
                    BLANK_STR
                }

                authCallback.invoke(BiometricAuthResult(BiometricAuthStatus.SUCCESS, masterKey))
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                authCallback.invoke(BiometricAuthResult(BiometricAuthStatus.FAILED))
            }
        })

    init {
        if (isFirstSetup) {
            generateSecretKey(KeyGenParameterSpec.Builder(
                FINGERPRINT_KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .setRandomizedEncryptionRequired(false)
                .build())
        }
    }

    private val promptInfo: BiometricPrompt.PromptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle(fragment.resources.getString(R.string.biometric_title))
        .setSubtitle(fragment.resources.getString(R.string.biometric_subtitle))
        .setNegativeButtonText(fragment.resources.getString(R.string.biometric_negative_button_text))
        .build()

    fun authenticate(masterPinKeyStr: String) {
        masterPinKey = masterPinKeyStr
        try {
            val cipher = getCipher()
            val secretKey = getSecretKey()
            if (isFirstSetup) {
                val iv = createIv()
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv)
            }
            else {
                cipher.init(Cipher.DECRYPT_MODE, secretKey, getIvFromMasterPinKey(masterPinKey))
            }
            biometricPrompt.authenticate(promptInfo,
                BiometricPrompt.CryptoObject(cipher))
        } catch (e: Exception) {
            Timber.e(e)
            authCallback.invoke(BiometricAuthResult(BiometricAuthStatus.CONFIG_ERROR))
        }
    }

    private fun generateSecretKey(keyGenParameterSpec: KeyGenParameterSpec) {
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        keyGenerator.init(keyGenParameterSpec)
        keyGenerator.generateKey()
    }

    private fun getSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)

        return keyStore.getKey(FINGERPRINT_KEY_ALIAS, null) as SecretKey
    }

    private fun getCipher(): Cipher {
        return Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                + KeyProperties.BLOCK_MODE_CBC + "/"
                + KeyProperties.ENCRYPTION_PADDING_PKCS7)
    }

    private fun createIv(): IvParameterSpec {
        val ivRandom = SecureRandom()
        val iv = ByteArray(IV_SIZE_16)
        ivRandom.nextBytes(iv)

        return IvParameterSpec(iv)
    }

    private fun getIvFromMasterPinKey(pinKey: String): IvParameterSpec {
        val masterPinKeyBytes = pinKey.base64DecodeFromString()
        val ivBytes = ByteArray(IV_SIZE_16)
        masterPinKeyBytes.copyInto(ivBytes, startIndex = masterPinKeyBytes.size - IV_SIZE_16)

        return IvParameterSpec(ivBytes)
    }
}