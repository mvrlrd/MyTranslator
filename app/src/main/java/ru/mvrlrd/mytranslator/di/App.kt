package ru.mvrlrd.mytranslator.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.mvrlrd.mytranslator.di.modules.appModule
import ru.mvrlrd.mytranslator.di.modules.appModule2
import ru.mvrlrd.mytranslator.di.modules.retrofitModule

class App : Application() {
    override fun onCreate() {
        super.onCreate()


        startKoin {
            androidContext(this@App)
            modules(listOf(appModule, appModule2, retrofitModule))

        }
    }
}

