package ru.mvrlrd.mytranslator.presenter

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.mvrlrd.mytranslator.presenter.modules.appModule

class App : Application() {
    override fun onCreate() {
        super.onCreate()


        startKoin {
            // declare used Android context
            androidContext(this@App)
            // declare modules
            modules(appModule)
        }
    }
}

