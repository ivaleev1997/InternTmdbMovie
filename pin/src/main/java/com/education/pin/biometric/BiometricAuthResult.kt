package com.education.pin.biometric

data class BiometricAuthResult(
    val biometricAuthStatus: BiometricAuthStatus,
    val masterPinKey: String? = null
)