plugins {
    alias(libs.plugins.dailyfilm.android.library)
    alias(libs.plugins.dailyfilm.android.kotlin)
}

android {
    namespace = "com.dailyfilm.core.mvi"
}

dependencies {
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
}