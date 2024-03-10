plugins {
    alias(libs.plugins.pos.android.library)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.casecode.pos.domain"

    @Suppress("UnstableApiUsage")
    testOptions {
        unitTests {

            isReturnDefaultValues = true
        }
    }

}

dependencies {

    testApi(projects.testing)
    api(libs.firebase.firestore.ktx)
    api(libs.firebase.auth.ktx)

    testApi(libs.coroutines.test)

}
