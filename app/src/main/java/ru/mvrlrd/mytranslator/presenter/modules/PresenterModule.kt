package ru.mvrlrd.mytranslator.presenter.modules

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import ru.mvrlrd.mytranslator.presenter.App
import ru.mvrlrd.mytranslator.presenter.MainViewModel
import javax.inject.Singleton

@Module
class PresenterModule {
    @Provides
    @Singleton
    fun provideContext():MainViewModel = MainViewModel()
}


@Module
class AppModule(private val app: Application) {
    @Provides
    @Singleton
    fun provideContext(): Context = app
}