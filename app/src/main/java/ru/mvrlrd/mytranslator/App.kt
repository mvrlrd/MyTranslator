package ru.mvrlrd.mytranslator

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.mvrlrd.mytranslator.presenter.modules.appModule
import ru.mvrlrd.mytranslator.presenter.modules.retrofitModule

class App : Application() {
    override fun onCreate() {
        super.onCreate()


        startKoin {
            androidContext(this@App)
            modules(listOf(appModule, retrofitModule))

        }
    }
}

