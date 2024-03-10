plugins {
    alias(libs.plugins.pos.android.library)
    alias(libs.plugins.pos.android.hilt)
    alias(libs.plugins.secrets)
}
android {
    buildFeatures {
        buildConfig = true
    }
    namespace = "com.casecode.pos.di"
}
secrets {
    defaultPropertiesFileName = "secrets.defaults.properties"
}
dependencies {
    api(projects.data)
    api(projects.domain)
    api(libs.firebase.firestore.ktx)
    api(libs.firebase.auth.ktx)
    api(libs.firebase.storage.ktx)

    implementation(libs.play.services.auth)
}