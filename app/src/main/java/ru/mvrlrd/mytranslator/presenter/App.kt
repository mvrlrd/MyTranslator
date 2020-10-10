package ru.mvrlrd.mytranslator.presenter

import android.app.Application
import ru.mvrlrd.mytranslator.presenter.modules.AppModule

class App : Application() {
    lateinit var appComponent: AppComponent


    override fun onCreate() {
        super.onCreate()
        appComponent = initDagger(this)
    }

    private fun initDagger(app: App): AppComponent =
        DaggerAppComponent.builder()
            .appModule(AppModule(app))
            .build()
}