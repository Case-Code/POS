plugins {
   alias(libs.plugins.pos.android.library)
}
android {
   namespace = "com.casecode.pos.data"
   
   @Suppress("UnstableApiUsage")
   testOptions {
      unitTests {
         isReturnDefaultValues = true
      }
   }
   
}

dependencies {
   
   api(projects.domain)
    implementation(libs.firebase.storage.ktx)
    testApi(projects.domain)
   testApi(projects.testing)
   
   //Coroutines
   implementation(libs.kotlinx.coroutines.services)
   api(libs.kotlinx.coroutines.android)
   api(libs.firebase.auth.ktx)
   implementation(libs.play.services.auth)
   api(libs.hilt.android)
   implementation(libs.zxing.generate.barcode)
   
   
   testImplementation(libs.test.mockk)
   
   testApi(libs.coroutines.test)
   testApi(libs.hilt.android.testing)

}
