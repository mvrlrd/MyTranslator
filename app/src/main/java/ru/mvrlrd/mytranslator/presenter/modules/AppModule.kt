package ru.mvrlrd.mytranslator.presenter.modules

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import ru.mvrlrd.mytranslator.model.datasource.retrofit.ApiHelper
import ru.mvrlrd.mytranslator.presenter.MainViewModel
import javax.inject.Singleton

@Module
class AppModule(private val app: Application) {
    @Provides
    @Singleton
    fun provideContext(): Context = app
}

@Module
class PresenterModule {
    @Provides
    @Singleton
    fun provideContext(): MainViewModel = MainViewModel(apiHelper = ApiHelper())
}

@Module
class ApiHelperModule {
    @Provides
    @Singleton
    fun provideApiHelper(): ApiHelper = ApiHelper()
}