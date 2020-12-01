import org.gradle.api.JavaVersion

object Config{
    const val application_id = "ru.mvrlrd.mytranslator"
    const val compile_sdk =30
    const val min_sdk = 22
    const val target_sdk = 30
    val java_version = com.google.gson.internal.JavaVersion.VERSION_1_8
}

object Releases {
    const val version_name = "1.0"
    val version_code : Int = getVersionName(version_name)

    fun getVersionName(version : String){
        val parts = version.split(".")
        return parts[0].toInt()*1000 + parts[1]*10
    }
}

object Modules{
    const val app = ":app"
}


object Versions {
    //Design
    const val appcompat = "1.2.0"
    const val material = "1.2.1"         //You should not use the com.android.support and com.google.android.material dependencies in your app at the same time
    const val constraintlayout = "2.0.2"
    const val cardView = "1.0.0"
    const val recyclerView = "1.1.0"

    //Kotlin
    const val stdlib = "1.3.72"
    const val core = "1.4.0"
    const val coroutinesCore = "1.3.9"
    const val coroutinesAndroid = "1.3.9"

    //Retrofit
    const val retrofit = "2.9.0"
    const val converterGson = "2.9.0"
    const val interceptor = ""
    const val adapterCoroutines = "1.0.0" //jakewharton_retrofit_rxjava_adapter:
    const val okHttpClient = "3.12.1"

    //Koin
    const val koinAndroid = "2.0.1"
    const val koinViewModel = "2.0.1"

    //Coil
    const val coil = "0.9.5"
    const val coilBase = "0.9.5"

    //Room
    const val roomRuntime = "2.2.5"
    const val roomCompiler = "2.2.5"
    const val roomKtx = "2.2.5"
    const val roomTesting = "2.2.5"

    //RxJava
    const val rxAndroid = "2.1.1"
    const val rxJava = "2.2.9"

    //Tools
    const val legacy_support_v4 = "1.0.0"

//lifeCircle
    const val lifecircleCompiler = "2.2.0"
    const val lifecircleExtension = "2.2.0"
    const val lifecircleViewModelKtx = "2.2.0"

    //Test
    const val junit = "4.13"
    const val ext_junit ="1.1.2"
    const val espresso ="3.3.0"
}
object Tools{
    const val support = "androidx.legacy:legacy-support-v4:${Versions.legacy_support_v4}"
}

object Design{
    const val appcompat = "com.androidx.appcompat:appcompat:${Versions.appcompat}"
    const val material = "com.google.android.material:material:${Versions.material}"
    const val cardView = "androidx.cardview:cardview:${Versions.cardView}"
    const val constraintlayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val recyclerView = "androidx.recyclerview:recyclerview:${Versions.recyclerView}"
}




object Kotlin{
    const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.stdlib}"
    const val core = "com.androidx.core:core-ktx:${Versions.core}"
    const val coroutinesCore = "rg.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutinesCore}"
    const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutinesAndroid}"
}

object Retrofit{
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val converterGson = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
    const val interceptor = ""
    const val adapterCoroutines = "com.jakewharton.retrofit:retrofit2-rxjava2-adapter:${Versions.adapterCoroutines}" //jakewharton_retrofit_rxjava_adapter:
    const val okHttpClient = "com.squareup.okhttp3:logging-interceptor:${Versions.okHttpClient}"
}
object Koin{
    const val koinAndroid ="org.koin:koin-android:${Versions.koinAndroid}"
    const val koinViewModel ="org.koin:koin-androidx-viewmodel:${Versions.koinViewModel}"
}
object Coil {
    const val coil = "io.coil-kt:coil:${Versions.coil}"
    const val coilBase = "io.coil-kt:coil-base:${Versions.coilBase}"
}

object Room{
    const val roomRuntime = "androidx.room:room-runtime:${Versions.roomRuntime}"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.roomCompiler}"
    const val roomKtx = "androidx.room:room-ktx:${Versions.roomKtx}"
    const val roomTesting = "androidx.room:room-testing:${Versions.roomTesting}"
}

object RxJava{
    const val rxAndroid = "io.reactivex.rxjava2:rxandroid:${Versions.rxAndroid}"
    const val rxJava = "io.reactivex.rxjava2:rxjava:${Versions.rxJava}"
}

object LifeCircle{
    const val lifecircleCompiler = "androidx.lifecycle:lifecycle-compiler:${Versions.lifecircleCompiler}"
        const val lifecircleExtension = "androidx.lifecycle:lifecycle-extensions:${Versions.lifecircleCompiler}"
        const val lifecircleViewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecircleCompiler}"
}

object Testing {
    const val junit = "junit:junit:${Versions.junit}"
    const val ext_junit = "androidx.test.ext:junit:${Versions.ext_junit}"
    const val espresso_core = "androidx.test.espresso:espresso-core:${Versions.espresso}"
}























