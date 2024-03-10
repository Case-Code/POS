plugins {
    alias(libs.plugins.pos.android.library)
    alias(libs.plugins.pos.android.hilt)
}

android {
    namespace = "com.casecode.pos.testing"

    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
            excludes.add("/META-INF/NOTICE.md")
            excludes.add("/META-INF/licenses/**")
            excludes.add("META-INF/LICENSE.md")
            excludes.add("META-INF/LICENSE-notice.md")
            excludes.add("META-INF/DEPENDENCIES")
            excludes.add("DebugProbesKt.bin")
        }
    }
}

dependencies {

    api(projects.domain)
    api(projects.data)
    api(projects.di)

    // use for testing live data
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.storage.ktx)

    api(libs.core.testing)
    api(libs.coroutines.test)
    implementation(libs.test.mockk)
    api(libs.test.runner)

    implementation(libs.hilt.android.testing)
    implementation(libs.test.espresso.idlingResource)
    implementation(libs.play.services.auth)
}