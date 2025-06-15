plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.jetbrains.kotlin.serialization) apply false
    // Dagger - Hilt
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    // Firebase
    alias(libs.plugins.google.gms.google.services) apply false
}