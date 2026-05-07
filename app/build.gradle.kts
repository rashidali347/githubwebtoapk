import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.text.SimpleDateFormat
import java.util.Date

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    // پلے اسٹور کی ضرورت کے مطابق API لیول 34
    compileSdk = 34
    namespace = "com.trustrium.mining" // آپ کی کمپنی کا یونیک پیکیج نیم

    defaultConfig {
        applicationId = "com.trustrium.mining"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"

        multiDexEnabled = true
        vectorDrawables.useSupportLibrary = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // یہاں Trustrium کی تفصیلات سیٹ کر دی گئی ہیں
        resValue("string", "url_web", "https://app.trustrium.com")
        resValue("string", "app_name", "Trustrium Mining")
        
        // یہ لائن TWA (Trusted Web Activity) کے لیے ضروری ہے
        manifestPlaceholders["assetStatements"] = """
            [{
              "relation": ["delegate_permission/common.handle_all_urls"],
              "target": {
                "namespace": "android_app",
                "package_name": "com.trustrium.mining",
                "sha256_cert_fingerprints": [""] 
              }
            }]
        """.trimIndent()
    }

    buildTypes {
        release {
            isMinifyEnabled = true 
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            
            // عارضی طور پر ڈیبگ سائننگ استعمال کر رہے ہیں تاکہ GitHub بلڈ فیل نہ ہو
            signingConfig = signingConfigs.getByName("debug")
        }
        
        debug {
            applicationIdSuffix = ".debug"
            isDebuggable = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
        resValues = true
    }
}

dependencies {
    // Android Standard Libraries
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    
    // TWA Library (Trusted Web Activity) - ویب سائٹ کو ایپ میں بدلنے کے لیے
    implementation("com.google.androidbrowserhelper:androidbrowserhelper:2.5.0")

    // Background tasks (مائننگ ایپس کے لیے اکثر ضروری ہوتی ہے)
    implementation("androidx.work:work-runtime-ktx:2.9.0")

    // Testing Libraries
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
