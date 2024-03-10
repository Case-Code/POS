import com.android.build.gradle.internal.tasks.databinding.DataBindingGenBaseClassesTask
import org.gradle.configurationcache.extensions.capitalized
import org.jetbrains.kotlin.gradle.tasks.AbstractKotlinCompileTool

plugins {
    alias(libs.plugins.pos.android.application)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.pos.android.hilt)
    alias(libs.plugins.pos.android.firebase)
    alias(libs.plugins.navigation.safeargs.kotlin)
}

android {

    defaultConfig {
        applicationId = "com.casecode.pos"
        versionCode = com.casecode.pos.Configuration.versionCode
        versionName = com.casecode.pos.Configuration.versionName

        resourceConfigurations.addAll(listOf("en", "ar"))
        testInstrumentationRunner = "com.casecode.testing.PosTestRunner"
    }

    buildTypes {
        debug {
            isPseudoLocalesEnabled = true
            enableAndroidTestCoverage = false
            isDebuggable = true
            isMinifyEnabled = false
        }
        val release by getting {
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("debug")

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    if (project.hasProperty("debug")) {
        splits.abi.isEnable = false
        splits.density.isEnable = false
        aaptOptions.cruncherEnabled = false
    }

    @Suppress("UnstableApiUsage")
    testOptions {

        animationsDisabled = true
        packaging {
            jniLibs {
                useLegacyPackaging = true
            }
        }
        unitTests {

            isIncludeAndroidResources = true
        }
    }

    hilt {
        enableAggregatingTask = true
    }

    buildFeatures {

        dataBinding = true
        viewBinding = true
        buildConfig = true
    }
    lint {
        abortOnError = true
    }

    packaging {

        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
            excludes.add("/META-INF/NOTICE.md")
            excludes.add("/META-INF/licenses/**")
            excludes.add("META-INF/LICENSE.md")
            excludes.add("META-INF/LICENSE-notice.md")
            excludes.add("DebugProbesKt.bin")
        }
    }

    namespace = "com.casecode.pos"
}

androidComponents {

    onVariants(selector().all()) { variant ->
        afterEvaluate {
            val dataBindingTask =
                project.tasks.findByName("dataBindingGenBaseClasses" + variant.name.capitalized()) as? DataBindingGenBaseClassesTask
            if (dataBindingTask != null) {
                project.tasks.getByName("ksp" + variant.name.capitalized() + "Kotlin") {
                    (this as AbstractKotlinCompileTool<*>).setSource(dataBindingTask.sourceOutFolder)
                }
            }
        }
    }
}

dependencies {

    implementation(projects.data)
    implementation(projects.domain)
    implementation(projects.di)

    testImplementation(projects.domain)
    testImplementation(projects.data)
    testImplementation(projects.di)
    testImplementation(projects.testing)

    androidTestImplementation(projects.domain)
    androidTestImplementation(projects.testing)

    // AndroidX
    implementation(libs.core)
    // implementation(libs.activity)
    // implementation(libs.fragment.ktx)
    implementation(libs.appcompat)
    implementation(libs.recyclerview)
    implementation(libs.slidingpanelayout)
    implementation(libs.window)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.livedata)
    implementation(libs.lifecycle.runtime)
    // UI tools
    implementation(libs.material)
    implementation(libs.android.stepper)
    implementation(libs.coil)

    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    // service
    implementation(libs.budiyev.barcode)
    implementation(libs.zxing.android.embedded)

    // coroutines
    implementation(libs.kotlinx.coroutines.android)
    debugCompileOnly(libs.kotlinx.coroutines.debug)
    // Debug tools
    // debugImplementation(libs.leakcanary)
    implementation(libs.timber)
    // ******* UNIT TESTING ******************************************************
    // use for testing live data
    testImplementation(libs.core.testing)

    // jvm test - Hilt
    kspTest(libs.hilt.compiler)

    // assertion
    testImplementation(libs.test.hamcrest)
    testImplementation(libs.test.hamcrest.library)

    // mockito with kotlin
    testImplementation(libs.test.mockk)

    // coroutines unit test
    testImplementation(libs.coroutines.test)
    testImplementation(libs.coroutines.android.test)
    // Once https://issuetracker.google.com/127986458 is fixed this can be testImplementation
    debugImplementation(libs.fragment.testing)
    /*    implementation(libs.test.core)
       implementation(libs.test.ext.junit) */
    // ******* ANDROID TESTING ***************************************************
    implementation(libs.test.espresso.idlingResource)

    androidTestImplementation(libs.window.testing)

    // Resolve conflicts between main and test APK:
    // androidTestImplementation(libs.appcompat)
    androidTestImplementation(libs.material)
    //  androidTestImplementation(libs.androidx.annotation)
    androidTestImplementation(libs.firebase.testlab)
    androidTestImplementation(libs.test.core)
    androidTestImplementation(libs.test.core.ktx)
    androidTestImplementation(libs.test.runner)
    androidTestImplementation(libs.test.ext.junit)
    androidTestImplementation(libs.test.ext.junit.ktx)
    androidTestImplementation(libs.test.monitor)
    androidTestImplementation(libs.test.orchestrator)
    androidTestImplementation(libs.test.rules)
    androidTestImplementation(libs.core.testing)

    androidTestImplementation(libs.test.hamcrest)
    androidTestImplementation(libs.test.hamcrest.library)

    androidTestImplementation(libs.mockk.android)
    androidTestImplementation(libs.mockk.agent)
    // androidTestImplementation(libs.test.mockk)
    androidTestImplementation(libs.navigation.testing)

    androidTestImplementation(libs.coil.test)
    //  testImplementation(libs.fragment.testing)
    androidTestImplementation(libs.test.espresso.core)
    androidTestImplementation(libs.test.espresso.idlingResource)
    androidTestImplementation(libs.test.espresso.idling.concurrent)
    androidTestImplementation(libs.test.espresso.accessibility) {
        exclude(module = "protobuf-lite")
    }
    androidTestImplementation(libs.test.espresso.contrib) {
        exclude(module = "protobuf-lite")
    }

    // AndroidX Test - Hilt testing
    kspAndroidTest(libs.hilt.compiler)
    androidTestImplementation(libs.hilt.android.testing)

    // implementation(kotlin("reflect"))
    //  androidTestImplementation(kotlin("reflect"))
}

tasks.withType<Test>().configureEach {
    maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).takeIf { it > 0 } ?: 1
}