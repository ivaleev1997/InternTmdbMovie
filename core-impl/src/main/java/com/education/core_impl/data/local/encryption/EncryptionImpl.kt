package com.education.core_impl.data.local.encryption

import android.content.Context
import android.os.Build
import android.security.KeyPairGeneratorSpec
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.annotation.RequiresApi
import com.education.core_api.BLANK_STR
import java.math.BigInteger
import java.security.*
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject
import javax.security.auth.x500.X500Principal


class EncryptionImpl @Inject constructor(
    context: Context
) : Encryption {
    companion object {
        private const val KEYS_SPEC_ITERATION_COUNT_512 = 512
        private const val SALT_SIZE_256 = 256
        private const val IV_SIZE = 16
        private const val SECRET_KEY_FACTORY_ALGORITHM = "PBKDF2withHmacSHA1and8BIT"
        private const val AES_CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding"
        private const val RSA_CIPHER_ALGORITHM = "RSA/ECB/PKCS1Padding"
        private const val RSA_MODE = "RSA"
        private const val ANDROID_OPENSSL = "AndroidOpenSSL"

        private const val KEY_ALIAS = "key_alias"
        private const val ANDROID_KEY_STORE = "AndroidKeyStore"

        private fun base64EncodeToString(data: ByteArray): String = Base64.encodeToString(data, Base64.NO_WRAP)

        private fun base64DecodeFromString(data: String): ByteArray = Base64.decode(data, Base64.NO_WRAP)
    }

    init {
        val ks = initKeyStore()

        if (!ks.containsAlias(KEY_ALIAS)) {
            createKeys(context)
        }
    }

    override fun encrypt(password: String, data: String): String {
        return try {
            val salt = createSalt()
            val ivSpec = createIv()
            val keySpec = createSecretKeySpec(password, salt)

            val cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM)
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)
            val encrypted = cipher.doFinal(data.toByteArray(Charsets.UTF_8))

            val resultArray = ByteArray(IV_SIZE + encrypted.size + SALT_SIZE_256)
            ivSpec.iv.copyInto(resultArray)
            encrypted.copyInto(resultArray,
                IV_SIZE
            )
            salt.copyInto(resultArray, IV_SIZE + encrypted.size)

            base64EncodeToString(resultArray)
        } catch (e: Exception) {
            BLANK_STR
        }
    }

    private fun getPrivateKeyEntry(keyStore: KeyStore, keyAlias: String): KeyStore.PrivateKeyEntry? {
        return keyStore.getEntry(keyAlias, null) as KeyStore.PrivateKeyEntry?
    }

    override fun decrypt(password: String, data: String): String {
        return try {
            val dataByteArray = base64DecodeFromString(data)
            val iv = dataByteArray.copyOfRange(0,
                IV_SIZE
            )
            val salt = dataByteArray.copyOfRange(dataByteArray.size - SALT_SIZE_256, dataByteArray.size)

            val encryptedData = dataByteArray.copyOfRange(IV_SIZE, dataByteArray.size - SALT_SIZE_256)
            val keySpec = createSecretKeySpec(password, salt)
            val ivSpec = IvParameterSpec(iv)

            val cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM)
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)
            val decrypted = cipher.doFinal(encryptedData)

            String(decrypted, Charsets.UTF_8)
        } catch (e: Exception) {
            BLANK_STR
        }
    }

    override fun encrypt(data: String): String {
        return try {
            val ks = initKeyStore()

            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                val publicKey: PublicKey? = getPrivateKeyEntry(ks, KEY_ALIAS)?.certificate?.publicKey

                val cipher: Cipher = Cipher.getInstance(RSA_CIPHER_ALGORITHM, ANDROID_OPENSSL)
                cipher.init(Cipher.ENCRYPT_MODE, publicKey)

                base64EncodeToString(cipher.doFinal(data.toByteArray(Charsets.UTF_8)))
            } else {
                val secretKey = ks.getKey(KEY_ALIAS, null)
                val cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM)

                cipher.init(Cipher.ENCRYPT_MODE, secretKey)
                val encrypted = cipher.doFinal(data.toByteArray(Charsets.UTF_8))
                val resultArray = ByteArray(IV_SIZE + encrypted.size)

                cipher.iv.copyInto(resultArray)
                encrypted.copyInto(resultArray,
                    IV_SIZE
                )

                base64EncodeToString(resultArray)
            }
        } catch (e: Exception) {
            BLANK_STR
        }
    }

    override fun decrypt(data: String): String {
        val ks = initKeyStore()
        return if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            try {
                val privateKey: PrivateKey? = getPrivateKeyEntry(ks, KEY_ALIAS)?.privateKey
                val cipher: Cipher = Cipher.getInstance(RSA_CIPHER_ALGORITHM, ANDROID_OPENSSL)
                cipher.init(Cipher.DECRYPT_MODE, privateKey)

                String(cipher.doFinal(base64DecodeFromString(data)), Charsets.UTF_8)
            } catch (e: Exception) {
                BLANK_STR
            }
        } else {
            try {
                val encryptedByteArray = base64DecodeFromString(data)
                val secretKey = ks.getKey(KEY_ALIAS, null)
                val iv = encryptedByteArray.copyOfRange(0, IV_SIZE)
                val encryptedData = encryptedByteArray.copyOfRange(IV_SIZE, encryptedByteArray.size)

                val cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM)

                cipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(iv))
                val decrypted = cipher.doFinal(encryptedData)

                String(decrypted, Charsets.UTF_8)
            } catch (e: Exception) {
                BLANK_STR
            }
        }
    }

    private fun createSalt(): ByteArray {
        val random = SecureRandom()
        val salt = ByteArray(SALT_SIZE_256)
        random.nextBytes(salt)

        return salt
    }

    private fun createIv(): IvParameterSpec {
        val ivRandom = SecureRandom()
        val iv = ByteArray(IV_SIZE)
        ivRandom.nextBytes(iv)

        return IvParameterSpec(iv)
    }

    private fun createSecretKeySpec(password: String, salt: ByteArray): SecretKeySpec {
        val pbKeySpec = PBEKeySpec(password.toCharArray(), salt,
            KEYS_SPEC_ITERATION_COUNT_512,
            SALT_SIZE_256
        )
        val secretKeyFactory = SecretKeyFactory.getInstance(SECRET_KEY_FACTORY_ALGORITHM)
        val keyBytes = secretKeyFactory.generateSecret(pbKeySpec).encoded

        return SecretKeySpec(keyBytes, "AES")
    }

    private fun initKeyStore(): KeyStore {
        val keyStore = KeyStore.getInstance(ANDROID_KEY_STORE)
        keyStore.load(null)

        return keyStore
    }

    private fun createKeys(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            createAesKey(KEY_ALIAS)
        } else {
            createRsaKeys(context, KEY_ALIAS)
        }
    }

    private fun createRsaKeys(context: Context, alias: String) {
        val start: Calendar = GregorianCalendar()
        val end: Calendar = GregorianCalendar()
        end.add(Calendar.YEAR, 30)

        val spec =
            KeyPairGeneratorSpec.Builder(context)
                .setAlias(alias)
                .setSubject(X500Principal("CN=$alias"))
                .setSerialNumber(BigInteger.valueOf(Math.abs(alias.hashCode()).toLong()))
                .setStartDate(start.time).setEndDate(end.time)
                .build()

        val kpGenerator: KeyPairGenerator =
            KeyPairGenerator.getInstance(
                RSA_MODE,
                ANDROID_KEY_STORE
            )
        kpGenerator.initialize(spec)
        kpGenerator.generateKeyPair()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun createAesKey(alias: String) {
        val keyGenerator =
            KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE)
        keyGenerator.init(
            KeyGenParameterSpec.Builder(
                alias,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .build()
        )
        keyGenerator.generateKey()
    }
}